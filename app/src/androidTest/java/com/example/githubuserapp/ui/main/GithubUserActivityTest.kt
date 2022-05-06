package com.example.githubuserapp.ui.main

import android.view.KeyEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.githubuserapp.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GithubUserActivityTest {

    @Before
    fun setup() {
        ActivityScenario.launch(GithubUserActivity::class.java)
    }

    @Test
    fun testSearchResults() {
        onView(withId(R.id.action_search)).perform(click())

        onView(withId(androidx.appcompat.R.id.search_src_text)).check(matches(isDisplayed()))
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(
            typeText("Phillip"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        onView(withId(R.id.rv_search_github_users)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_search_github_users))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, click()
            ))
        onView(withId(R.id.action_add_favorites))
            .perform(click())
        onView(withId(android.R.id.button1))
            .perform(click())
    }

    @Test
    fun testDarkMode() {
        onView(withId(R.id.action_settings)).perform(click())

        onView(withId(R.id.switch_theme)).check(matches(isDisplayed()))
        onView(withId(R.id.switch_theme)).perform(click())
    }

    @Test fun clickRecyclerViewItem() {
        onView(withId(R.id.rv_github_users))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, click()
            ))
        onView(withId(R.id.action_add_favorites))
            .perform(click())
        onView(withId(android.R.id.button1))
            .perform(click())
    }

    @Test fun deleteFromFavorites() {
        onView(withId(R.id.action_favorites))
            .perform(click())
        onView(withId(R.id.rv_favorites))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, click()
            ))
        onView(withId(R.id.action_remove)).check(matches(isDisplayed()))
        onView(withId(R.id.action_remove)).perform(click())
        onView(withId(android.R.id.button1)).perform(click())
        onView(withId(R.id.rv_favorites)).check(matches(isDisplayed()))
    }
}