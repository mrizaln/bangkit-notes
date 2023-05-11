package com.example.mycomposetesting.ui

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.mycomposetesting.R
import com.example.mycomposetesting.ui.theme.MyComposeTestingTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class CalculatorAppTest {
//    @get:Rule
//    val composeTestRule = createComposeRule()       // gak bisa ambil resource

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>() // bisa ambil resource

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MyComposeTestingTheme {
                CalculatorApp()
            }
        }
    }

    //    @Test
//    fun calculate_area_of_rectangle_correct(): Unit = with(composeTestRule) {
//        // hardcoded string
//        onNodeWithText("Masukkan panjang").performTextInput("3")
//        onNodeWithText("Masukkan lebar").performTextInput("4")
//        onNodeWithText("Hitung!").performClick()
//        onNodeWithText("Hasil: 12.0").assertExists()
//    }
    @Test
    fun calculate_area_of_rectangle_correct(): Unit = with(composeTestRule) {
        onNodeWithTextId(R.string.enter_length).performTextInput("3")
        onNodeWithTextId(R.string.enter_width).performTextInput("4")
        onNodeWithTextId(R.string.count).performClick()
        onNodeWithTextId(R.string.count, useUnmergedTree = true).assertHasNoClickAction()
        onNodeWithTextId(R.string.result, 12.0).assertExists()
    }

    @Test
    fun wrong_input_not_calculated(): Unit = with(composeTestRule) {
        onNodeWithTextId(R.string.enter_length).performTextInput("..3")
        onNodeWithTextId(R.string.enter_width).performTextInput("4")
        onNodeWithTextId(R.string.count).performClick()
        onNodeWithTextId(R.string.result, 0.0).assertExists()
    }

    private fun <R : TestRule, A : ComponentActivity> AndroidComposeTestRule<R, A>.onNodeWithTextId(
        @StringRes id: Int,
        substring: Boolean = false,
        ignoreCase: Boolean = false,
        useUnmergedTree: Boolean = false,
    ) = this.onNodeWithText(this.activity.getString(id), substring, ignoreCase, useUnmergedTree)

    private fun <R : TestRule, A : ComponentActivity> AndroidComposeTestRule<R, A>.onNodeWithTextId(
        @StringRes id: Int,
        vararg formatArgs: Any,
        substring: Boolean = false,
        ignoreCase: Boolean = false,
        useUnmergedTree: Boolean = false,
    ) = this.onNodeWithText(this.activity.getString(id, *formatArgs), substring, ignoreCase, useUnmergedTree)
}
