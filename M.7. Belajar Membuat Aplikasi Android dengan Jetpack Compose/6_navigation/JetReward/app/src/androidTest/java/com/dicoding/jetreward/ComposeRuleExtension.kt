package com.dicoding.jetreward

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import org.junit.rules.TestRule

object ComposeRuleExtension {
    fun <R : TestRule, A : ComponentActivity> AndroidComposeTestRule<R, A>.onNodeWithStringId(
        @StringRes id: Int,
        substring: Boolean = false,
        ignoreCase: Boolean = false,
        useUnmergedTree: Boolean = false,
    ) = onNodeWithText(activity.getString(id), substring, ignoreCase, useUnmergedTree)

    fun <R : TestRule, A : ComponentActivity> AndroidComposeTestRule<R, A>.onNodeWithStringId(
        @StringRes id: Int,
        vararg formatArgs: Any,
        substring: Boolean = false,
        ignoreCase: Boolean = false,
        useUnmergedTree: Boolean = false,
    ) = onNodeWithText(activity.getString(id, *formatArgs), substring, ignoreCase, useUnmergedTree)

    fun <R : TestRule, A : ComponentActivity> AndroidComposeTestRule<R, A>.onNodeWithContentDescriptionStringId(
        @StringRes id: Int,
        substring: Boolean = false,
        ignoreCase: Boolean = false,
        useUnmergedTree: Boolean = false,
    ) = onNodeWithContentDescription(activity.getString(id), substring, ignoreCase, useUnmergedTree)

    fun <R : TestRule, A : ComponentActivity> AndroidComposeTestRule<R, A>.onNodeWithContentDescriptionStringId(
        @StringRes id: Int,
        vararg formatArgs: Any,
        substring: Boolean = false,
        ignoreCase: Boolean = false,
        useUnmergedTree: Boolean = false,
    ) = onNodeWithContentDescription(activity.getString(id, *formatArgs), substring, ignoreCase, useUnmergedTree)
}
