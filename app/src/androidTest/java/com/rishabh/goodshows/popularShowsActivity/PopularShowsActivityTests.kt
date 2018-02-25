package com.rishabh.goodshows.popularShowsActivity

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeUp
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.rishabh.goodshows.R
import com.rishabh.goodshows.assertions.RecyclerViewItemCountAssertion
import com.rishabh.goodshows.popularShowsActivity.view.PopularTvShowsActivity
import com.rishabh.goodshows.showDetailsActivity.view.ShowDetailsActivity
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PopularShowsActivityTests {

    @JvmField
    @Rule
    var activityRule: IntentsTestRule<PopularTvShowsActivity> = IntentsTestRule(PopularTvShowsActivity::class.java)

    @Test
    fun checkPagination() {
        onView(withId(R.id.shows_list_rv)).check(matches(isDisplayed()))
        waitForSeconds(50)
        onView(withId(R.id.shows_list_rv)).perform(swipeUp())
        onView(withId(R.id.shows_list_rv)).perform(swipeUp())
        onView(withId(R.id.shows_list_rv)).perform(swipeUp())
        onView(withId(R.id.shows_list_rv)).perform(swipeUp())
        onView(withId(R.id.shows_list_rv)).perform(swipeUp())
        onView(withId(R.id.shows_list_rv)).perform(swipeUp())
        onView(withId(R.id.shows_list_rv)).perform(swipeUp())
        onView(withId(R.id.shows_list_rv)).perform(swipeUp())
        onView(withId(R.id.shows_list_rv))
                .check(RecyclerViewItemCountAssertion.withItemCount(Matchers.greaterThan(20)))
    }

    @Test
    fun checkItemClick() {
        onView(withId(R.id.shows_list_rv)).check(matches(isDisplayed()))
        waitForSeconds(100)
        onView(withId(R.id.shows_list_rv)).perform(swipeUp())
        waitForSeconds(100)
        onView(withId(R.id.shows_list_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        intended(hasComponent(ShowDetailsActivity::class.java.name))
    }

    private fun waitForSeconds(time: Long) {
        try {
            Thread.sleep(time)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

}
