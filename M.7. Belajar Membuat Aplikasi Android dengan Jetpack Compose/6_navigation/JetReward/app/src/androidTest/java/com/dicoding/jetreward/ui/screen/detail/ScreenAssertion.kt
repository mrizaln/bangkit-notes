package com.dicoding.jetreward.ui.screen.detail

import androidx.navigation.NavController
import org.junit.Assert

object ScreenAssertion {
    fun NavController.assertCurrentRouteName(expectedRouteName: String) {
        Assert.assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
    }
}
