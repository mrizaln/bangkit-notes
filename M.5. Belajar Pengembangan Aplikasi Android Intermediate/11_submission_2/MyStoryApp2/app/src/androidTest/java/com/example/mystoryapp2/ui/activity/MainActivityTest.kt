package com.example.mystoryapp2.ui.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.mystoryapp2.R
import com.example.mystoryapp2.util.EspressoIdlingResource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    // @formatter:off
    private val nonexistentEmail    = "bbb.bb@bb.bb"
    private val nonexistentPassword = "bbbbbbbb"

    private val existedEmail        = "bb.bb@bb.bb"          // I already made this account
    private val existedPassword     = "bbbbbbbb"
    private val existedPasswordTypo = "bbbbbbbn"
    // @formatter:on

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    /**
     * Test the login and logout action.
     *
     * The application may have a stored token inside it from previous launch if the developer/tester
     * or previous test attempted to login beforehand.
     */
    @Test
    fun loginAndLogout() {
        Intents.init()

        // if session token exist (in pref) then logout
        try {
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
            onView(withText(R.string.action_logout_title)).check(matches(isDisplayed()))
                .perform(click())
            onView(withText(R.string.dialog_confirm)).check(matches(isDisplayed())).perform(click())
        } catch (e: NoMatchingViewException) {
            // no session token
        }

        // login with non-existent account/email
        onView(withId(R.id.ed_login_email)).perform(clearText(), typeText(nonexistentEmail))
        onView(withId(R.id.ed_login_password)).perform(clearText(), typeText(nonexistentPassword), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())

        // login with existed account/email but invalid password
        onView(withId(R.id.ed_login_email)).perform(clearText(), typeText(existedEmail))
        onView(withId(R.id.ed_login_password)).perform(clearText(), typeText(existedPasswordTypo), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())

        // login with existed account/email (success)
        onView(withId(R.id.ed_login_email)).perform(clearText(), typeText(existedEmail))
        onView(withId(R.id.ed_login_password)).perform(clearText(), typeText(existedPassword), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())

        // logout
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(R.string.action_logout_title)).perform(click())
        onView(withText(R.string.dialog_confirm)).check(matches(isDisplayed())).perform(click())

        Intents.release()
    }

    /**
     * Open maps
     *
     * The session token may be removed from previous test, login may be needed
     */
    @Test
    fun openMapsThenRandomHighlightThenRefresh() {
        Intents.init()

        // if session token not exist
        try {
            onView(withId(R.id.ed_login_email)).perform(clearText(), typeText(existedEmail))
            onView(withId(R.id.ed_login_password)).perform(clearText(), typeText(existedPassword))
            onView(withId(R.id.btn_login)).perform(click())
        } catch (e: NoMatchingViewException) {
            // already login
        }

        // open maps activity
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(R.string.action_view_maps_title)).perform(click())
        intended(hasComponent(MapsActivity::class.java.name))

        Thread.sleep(3000)      // let the animation finish

        // highlights random mark
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(R.string.action_random_marker_title)).perform(click())

        Thread.sleep(3000)      // let the animation finish

        // refresh
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(R.string.action_refresh_maps_title)).perform(click())

        Thread.sleep(3000)      // let the animation finish

        Intents.release()
    }
}