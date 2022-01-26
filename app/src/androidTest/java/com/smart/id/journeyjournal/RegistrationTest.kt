package com.smart.id.journeyjournal

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistrationTest {
    @Test
    fun when_successful_registrationDetails() {
        val activityScenario = ActivityScenario.launch(Registration::class.java)
        activityScenario.onActivity {
            val email = "example@gmail.com"
            val password = "12345678"
            val confirmPassword = "12345678"
            Assert.assertEquals(it.verifyFields(email,password,confirmPassword),true)
        }


    }

    @Test
    fun when_email_is_null_registrationDetails() {
        val activityScenario = ActivityScenario.launch(Registration::class.java)
        activityScenario.onActivity {
            val email = null
            val password = "12345678"
            val confirmPassword = "12345678"
            Assert.assertEquals(it.verifyFields(email,password,confirmPassword),false)
        }


    }

    @Test
    fun when_password_is_null_registrationDetails() {
        val activityScenario = ActivityScenario.launch(Registration::class.java)
        activityScenario.onActivity {
            val email = "example@gmail.com"
            val password = null
            val confirmPassword = "12345678"
            Assert.assertEquals(it.verifyFields(email,password,confirmPassword),false)
        }


    }


    @Test
    fun when_confirmPassword_is_null_registrationDetails() {
        val activityScenario = ActivityScenario.launch(Registration::class.java)
        activityScenario.onActivity {
            val email = "example@gmail.com"
            val password = "12345678"
            val confirmPassword = null
            Assert.assertEquals(it.verifyFields(email,password,confirmPassword),false)
        }


    }


    @Test
    fun when_email_is_empty_registrationDetails() {
        val activityScenario = ActivityScenario.launch(Registration::class.java)
        activityScenario.onActivity {
            val email = ""
            val password = "12345678"
            val confirmPassword = "12345678"
            Assert.assertEquals(it.verifyFields(email,password,confirmPassword),false)
        }


    }

    @Test
    fun when_password_is_empty_registrationDetails() {
        val activityScenario = ActivityScenario.launch(Registration::class.java)
        activityScenario.onActivity {
            val email = "example@gmail.com"
            val password = ""
            val confirmPassword = "12345678"
            Assert.assertEquals(it.verifyFields(email,password,confirmPassword),false)
        }


    }


    @Test
    fun when_confirmPassword_is_empty_registrationDetails() {
        val activityScenario = ActivityScenario.launch(Registration::class.java)
        activityScenario.onActivity {
            val email = "example@gmail.com"
            val password = "12345678"
            val confirmPassword = ""
            Assert.assertEquals(it.verifyFields(email,password,confirmPassword),false)
        }


    }

    @Test
    fun when_password_and_confirmedPassword_mismatched_registrationDetails() {
        val activityScenario = ActivityScenario.launch(Registration::class.java)
        activityScenario.onActivity {
            val email = "example@gmail.com"
            val password = "12345678"
            val confirmPassword = "1241424"
            Assert.assertEquals(it.verifyFields(email,password,confirmPassword),false)
        }


    }

}