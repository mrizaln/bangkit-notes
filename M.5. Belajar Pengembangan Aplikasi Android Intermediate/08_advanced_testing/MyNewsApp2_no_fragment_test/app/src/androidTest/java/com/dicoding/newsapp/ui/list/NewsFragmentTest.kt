package com.dicoding.newsapp.ui.list

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.newsapp.JsonConverter
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import com.dicoding.newsapp.R
import com.dicoding.newsapp.data.remote.retrofit.ApiConfig
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

// NOT WORKING!!!!!!!!!!!

@RunWith(AndroidJUnit4::class)
class NewsFragmentTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getHeadlineNews_Success() {
        val bundle = Bundle()
        bundle.putString(NewsFragment.ARG_TAB, NewsFragment.TAB_NEWS)
        launchFragmentInContainer<NewsFragment>(bundle, R.style.Theme_News)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.rv_news))
            .check(matches(isDisplayed()))
        onView(withText("Inti Bumi Mendingin Lebih Cepat, Pertanda Apa? - detikInet"))
            .check(matches(isDisplayed()))
    }
}