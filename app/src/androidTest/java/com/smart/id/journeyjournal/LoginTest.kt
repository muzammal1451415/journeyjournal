package com.smart.id.journeyjournal
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {
    @Test
    fun when_successful_loginDetails() {
        val activityScenario = ActivityScenario.launch(Login::class.java)
        activityScenario.onActivity {
            val email = "example@gmail.com"
            val password = "12345678"
            Assert.assertEquals(it.verifyFields(email,password),true)
        }


    }

    @Test
    fun when_email_is_null_loginDetails() {
        val activityScenario = ActivityScenario.launch(Login::class.java)
        activityScenario.onActivity {
            val email = null
            val password = "12345678"
            Assert.assertEquals(it.verifyFields(email,password),false)
        }


    }

    @Test
    fun when_password_is_null_loginDetails() {
        val activityScenario = ActivityScenario.launch(Login::class.java)
        activityScenario.onActivity {
            val email = "example@gmail.com"
            val password = null
            Assert.assertEquals(it.verifyFields(email,password),false)
        }


    }


    @Test
    fun when_email_is_empty_loginDetails() {
        val activityScenario = ActivityScenario.launch(Login::class.java)
        activityScenario.onActivity {
            val email = ""
            val password = "12345678"
            Assert.assertEquals(it.verifyFields(email,password),false)
        }


    }

    @Test
    fun when_password_is_empty_loginDetails() {
        val activityScenario = ActivityScenario.launch(Login::class.java)
        activityScenario.onActivity {
            val email = "example@gmail.com"
            val password = ""
            Assert.assertEquals(it.verifyFields(email,password),false)
        }


    }
}