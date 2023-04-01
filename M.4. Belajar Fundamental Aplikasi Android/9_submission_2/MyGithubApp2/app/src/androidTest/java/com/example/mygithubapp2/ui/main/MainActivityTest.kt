package com.example.mygithubapp2.ui.main

import android.view.KeyEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.example.mygithubapp2.R
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun searchUser() {
        // in MainActivity
        onView(withId(R.id.app_bar_search))
            .perform(click())

        val queryTerm = listOf("mrizaln", "lunozt", "cyannyan", "torvalds", "0", "-", "z")
        for (query in queryTerm) {
            onView(withHint(R.string.search_hint))
                .perform(clearText(), typeText(query), pressKey(KeyEvent.KEYCODE_ENTER))

            Thread.sleep(1000)      // wait for RecyclerView to update
            val recyclerView = onView(withId(R.id.rv_users))
            recyclerView.perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
            )

            // in UserDetailActivity
            Thread.sleep(1000)      // wait for data to load
            onView(withId(R.id.menu_favorite))
                .perform(click())
                .perform(pressBack())
        }
        onView(withHint(R.string.search_hint))
            .perform(clearText(), typeText("adskfljhdaifugewpafuhdsofiusgafpiuabspfub"), pressKey(KeyEvent.KEYCODE_ENTER))

        Thread.sleep(1000)
    }

    @Test
    fun listFavorites() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(R.string.title_menu_favorite_list))
            .perform(click())
        Thread.sleep(1000)      // some wait time so we can see the result
    }

    @Test
    fun toggleTheme() {
        repeat(3) {
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
            onView(withText(R.string.title_menu_settings))
                .perform(click())
            onView(withId(R.id.switch_theme))
                .perform(click())
                .perform(pressBack())

            Thread.sleep(1000)
        }
    }
}