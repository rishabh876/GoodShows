package com.rishabh.goodshows.showDetailsActivity

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson
import com.rishabh.goodshows.MockResponses
import com.rishabh.goodshows.R
import com.rishabh.goodshows.assertions.RecyclerViewItemCountAssertion
import com.rishabh.goodshows.models.TvShow
import com.rishabh.goodshows.showDetailsActivity.view.ShowDetailsActivity
import org.hamcrest.Matchers.greaterThan
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
class ShowDetailsActivityTests {

    private lateinit var tvShow: TvShow
    @JvmField
    @Rule
    var activityRule: IntentsTestRule<ShowDetailsActivity> = object : IntentsTestRule<ShowDetailsActivity>(ShowDetailsActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            tvShow = Gson().fromJson(MockResponses.TV_SHOW_JSON, TvShow::class.java)
            return ShowDetailsActivity.getMyIntent(targetContext, tvShow)
        }
    }

    /**
     * This test will check if the values of tv show is populated on the UI.
     */
    @Test
    fun testPopulatedViews() {
        hasDescendant(withText(tvShow.name)).matches(withId(R.id.title_tv))
        hasDescendant(withText(tvShow.voteAverage.toString())).matches(withId(R.id.star_rating_tv))
        hasDescendant(withText(Locale(tvShow.originalLanguage).displayLanguage)).matches(withId(R.id.language_tv))
        hasDescendant(withText(tvShow.firstAirDate!!.split("-")[0])).matches(withId(R.id.year_tv))
        hasDescendant(withText(tvShow.overview)).matches(withId(R.id.desc_tv))

        onView(withId(R.id.similar_rv)).check(matches(isDisplayed()))
        onView(withId(R.id.similar_label_tv)).check(matches(isDisplayed()))
    }

    /**
     * This test will check if the recyclerview is visible and then scroll multiple times
     * to check pagination is happening. At the end if the number of items in RecyclerView are
     * greater than one page then test passes
     */
    @Test
    fun testSimilarShowsPagination() {
        onView(withId(R.id.similar_rv)).check(matches(isDisplayed()))
        waitForSeconds(100)
        onView(withId(R.id.similar_rv)).perform(ViewActions.swipeLeft())
        waitForSeconds(100)
        onView(withId(R.id.similar_rv)).perform(ViewActions.swipeLeft())
        waitForSeconds(100)
        onView(withId(R.id.similar_rv)).perform(ViewActions.swipeLeft())
        waitForSeconds(100)
        onView(withId(R.id.similar_rv)).perform(ViewActions.swipeLeft())
        waitForSeconds(100)
        onView(withId(R.id.similar_rv)).perform(ViewActions.swipeLeft())
        onView(withId(R.id.similar_rv))
                .check(RecyclerViewItemCountAssertion.withItemCount(greaterThan(20)))
    }

    /**
     * This test will check if the Show details activity is opened when
     * an item from similar shows list is clicked
     */
    @Test
    fun testSimilarShowsClick() {
        onView(withId(R.id.similar_rv)).check(matches(isDisplayed()))
        waitForSeconds(100)
        onView(withId(R.id.similar_rv)).perform(ViewActions.swipeLeft())
        onView(withId(R.id.similar_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))

        Intents.intended(IntentMatchers.hasComponent(ShowDetailsActivity::class.java.name))
    }

    private fun waitForSeconds(time: Long) {
        try {
            Thread.sleep(time)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

}
