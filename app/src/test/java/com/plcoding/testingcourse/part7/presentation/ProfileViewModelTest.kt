package com.plcoding.testingcourse.part7.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.plcoding.testingcourse.part7.data.UserRepositoryFake
import com.plcoding.testingcourse.part7.domain.UserRepository
import com.plcoding.testingcourse.util.MainCoroutineExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.lang.Exception

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
class ProfileViewModelTest {

    private lateinit var repository: UserRepositoryFake
    private lateinit var profileViewModel: ProfileViewModel


    @BeforeEach
    fun setUp() {
        repository = UserRepositoryFake()
        profileViewModel = ProfileViewModel(
            repository = repository,
            savedStateHandle = SavedStateHandle(
                initialState = mapOf(
                    "userId" to repository.profileToReturn.user.id
                )
            )
        )
    }

    @Test
    fun `Test loading profile success`() = runTest {
        profileViewModel.loadProfile()

        advanceUntilIdle()

        assertThat(profileViewModel.state.value.profile).isEqualTo(repository.profileToReturn)
        assertThat(profileViewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `Test loading profile error`() = runTest {
        repository.errorToReturn = Exception("Test Exception")
        profileViewModel.loadProfile()

        advanceUntilIdle()

        assertThat(profileViewModel.state.value.profile).isNull()
        assertThat(profileViewModel.state.value.errorMessage).isEqualTo("Test Exception")
        assertThat(profileViewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `test loading state updates`() = runTest {
        profileViewModel.state.test {
            val emission1 = awaitItem()
            assertThat(emission1.isLoading).isFalse()

            profileViewModel.loadProfile()
            val emission2 = awaitItem()
            assertThat(emission2.isLoading).isTrue()

            val emission3 = awaitItem()
            assertThat(emission3.isLoading).isFalse()
        }
    }
}