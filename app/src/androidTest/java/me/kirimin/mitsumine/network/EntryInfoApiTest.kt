package me.kirimin.mitsumine.network

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import junit.framework.Assert

import org.json.JSONException
import org.junit.Test
import org.junit.runner.RunWith

import me.kirimin.mitsumine._common.network.EntryInfoApi

import android.support.test.espresso.matcher.ViewMatchers.assertThat
import me.kirimin.mitsumine._common.domain.model.EntryInfo
import org.hamcrest.Matchers.`is`
import rx.observers.TestSubscriber

@RunWith(AndroidJUnit4::class)
class EntryInfoApiTest {

    @Test
    fun requestTest() {
        val testSubscriber = TestSubscriber<EntryInfo>()
        val url = "http://kirimin.hatenablog.com/entry/20140629/1404039922"
        EntryInfoApi.request(InstrumentationRegistry.getContext(), url).subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        assertThat(testSubscriber.onNextEvents.get(0).url, `is`(url))
    }
}