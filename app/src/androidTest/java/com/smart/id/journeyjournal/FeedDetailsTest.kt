package com.smart.id.journeyjournal

import android.net.Uri
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeedDetailsTest {

    @Test
    fun when_successful_verifyFields() {
        val activityScenario = ActivityScenario.launch(FeedDetails::class.java)
        activityScenario.onActivity {
            val title = "title"
            val description = "Description"
            Assert.assertEquals(it.verifyFields(title,description),true)
        }


    }

    @Test
    fun when_title_is_null_verifyFields() {
        val activityScenario = ActivityScenario.launch(FeedDetails::class.java)
        activityScenario.onActivity {
            val title = null
            val description = "Description"
            Assert.assertEquals(it.verifyFields(title,description),false)
        }


    }

    @Test
    fun when_description_is_null_verifyFields() {
        val activityScenario = ActivityScenario.launch(FeedDetails::class.java)
        activityScenario.onActivity {
            val title = "title"
            val description = null
            Assert.assertEquals(it.verifyFields(title,description),false)
        }


    }


    @Test
    fun when_title_is_empty_verifyFields() {
        val activityScenario = ActivityScenario.launch(FeedDetails::class.java)
        activityScenario.onActivity {
            val uri = Uri.Builder().build()
            val title = ""
            val description = "Description"
            Assert.assertEquals(it.verifyFields(title,description),false)
        }


    }

    @Test
    fun when_description_is_empty_verifyFields() {
        val activityScenario = ActivityScenario.launch(FeedDetails::class.java)
        activityScenario.onActivity {
            val title = "title"
            val description = ""
            Assert.assertEquals(it.verifyFields(title,description),false)
        }


    }
}