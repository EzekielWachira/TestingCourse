package com.plcoding.testingcourse.shopping.domain

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource


internal class ShoppingCartTest {

    private lateinit var cart: ShoppingCart

    @BeforeEach
    fun setUp() {
        cart = ShoppingCart()
    }

    @Test
    fun `Add multiple products, total sum is correct` () {
        // Given
        val product = Product(
            id = 0,
            name = "Milk",
            price = 7.0
        )

        cart.addProduct(product, 5)

        //ACTION
        val priceSum = cart.getTotalCost()

        //ASSERTION
        assertThat(priceSum).isEqualTo(35.0)
    }

    //run test case using number of values
    @ParameterizedTest
    @ValueSource(
        ints = [1, 2, 3, 4, 5]
    )
    fun `Add multiple products, total sum is correct, (parameterized)` (quantity: Int) {
        // Given
        val product = Product(
            id = 0,
            name = "Milk",
            price = 7.0
        )

        cart.addProduct(product, quantity)

        //ACTION
        val priceSum = cart.getTotalCost()

        //ASSERTION
        assertThat(priceSum).isEqualTo(quantity * 7.0)
    }

    @ParameterizedTest
    @CsvSource(
        "3, 21.0",
        "2, 14.0",
        "0, 0.0",
        "10, 70.0",
        "7, 49.0"
    )
    fun `Add multiple products, total sum is correct, (parameterized, with CSV)` (
        quantity: Int,
        expectedPriceSum: Double
    ) {
        // Given
        val product = Product(
            id = 0,
            name = "Milk",
            price = 7.0
        )

        cart.addProduct(product, quantity)

        //ACTION
        val priceSum = cart.getTotalCost()

        //ASSERTION
        assertThat(priceSum).isEqualTo(expectedPriceSum)
    }

    // run test case for certain number of times
    @RepeatedTest(100)
    fun `Add product with negative quantity, throws exception`() {
        val product = Product(
            0,
            "Ice Cream",
            4.0
        )

        assertFailure {
            cart.addProduct(product, -3)
        }
    }

     @Test
     fun `Is Valid Product returns invalid for non existing product`() {
         val product = Product(
             0,
             "Ice Cream",
             4.0
         )

         cart.addProduct(product,4)
         val totalPriceSum = cart.getTotalCost()

         assertThat(totalPriceSum).isEqualTo(0.0)
     }

}