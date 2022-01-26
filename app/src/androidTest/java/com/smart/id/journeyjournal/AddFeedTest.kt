package com.smart.id.journeyjournal

import android.content.res.Resources
import android.net.Uri
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.bumptech.glide.load.model.ResourceLoader
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddFeedTest {
    @Test
    fun when_successful_verifyFields() {
        getInstrumentation().runOnMainSync(Runnable {
            val uri = Uri.Builder().build()
            val title = "title"
            val description = "Description"
            Assert.assertEquals(AddFeed().verifyFields(uri,title,description),true)
        })

    }

    @Test
    fun when_uri_is_null_verifyFields() {
        val activityScenario = launch(AddFeed::class.java)
        activityScenario.onActivity {
            val uri = null
            val title = "title"
            val description = "Description"
            Assert.assertEquals(it.verifyFields(uri,title,description),false)
        }


    }

    @Test
    fun when_title_is_null_verifyFields() {
        val activityScenario = launch(AddFeed::class.java)
        activityScenario.onActivity {
            val uri = Uri.Builder().build()
            val title = null
            val description = "Description"
            Assert.assertEquals(it.verifyFields(uri,title,description),false)
        }


    }

    @Test
    fun when_description_is_null_verifyFields() {
        val activityScenario = launch(AddFeed::class.java)
        activityScenario.onActivity {
            val uri = Uri.Builder().build()
            val title = "title"
            val description = null
            Assert.assertEquals(it.verifyFields(uri,title,description),false)
        }


    }


    @Test
    fun when_title_is_empty_verifyFields() {
        val activityScenario = launch(AddFeed::class.java)
        activityScenario.onActivity {
            val uri = Uri.Builder().build()
            val title = ""
            val description = "Description"
            Assert.assertEquals(it.verifyFields(uri,title,description),false)
        }


    }

    @Test
    fun when_description_is_empty_verifyFields() {
        val activityScenario = launch(AddFeed::class.java)
        activityScenario.onActivity {
            val uri = Uri.Builder().build()
            val title = "title"
            val description = ""
            Assert.assertEquals(it.verifyFields(uri,title,description),false)
        }


    }


}