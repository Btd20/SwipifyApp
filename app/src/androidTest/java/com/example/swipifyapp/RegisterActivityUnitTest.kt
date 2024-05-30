package com.example.swipifyapp

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterActivityUnitTest {

    private lateinit var scenario: ActivityScenario<RegisterActivity>

    @Before
    fun setUp() {
        // Launch the activity under test
        scenario = ActivityScenario.launch(RegisterActivity::class.java)
    }

    @Test
    fun testRegister_Successful() {
        scenario.onActivity { registerActivity ->
            // Mock EditText inputs
            registerActivity.usernameEditText.setText("testUser")
            registerActivity.passwordEditText.setText("TestPassword1")
            registerActivity.confirmPasswordEditText.setText("TestPassword1")

            registerActivity.register()

            Handler(Looper.getMainLooper()).postDelayed({
                assertTrue(registerActivity.isFinishing)
            }, 1000)
        }
    }

    @Test
    fun testRegister_PasswordMismatch() {
        scenario.onActivity { registerActivity ->
            registerActivity.usernameEditText.setText("testUser")
            registerActivity.passwordEditText.setText("TestPassword1")
            registerActivity.confirmPasswordEditText.setText("TestPassword1")

            registerActivity.register()

            assertTrue(registerActivity.passwordRequirementsTextView.visibility == View.VISIBLE)
        }
    }

    @Test
    fun testRegister_EmptyFields() {
        scenario.onActivity { registerActivity ->
            registerActivity.usernameEditText.setText("")
            registerActivity.passwordEditText.setText("")
            registerActivity.confirmPasswordEditText.setText("")

            registerActivity.register()

            assertFalse(registerActivity.isFinishing)
        }
    }

    @Test
    fun testRegister_InvalidPassword() {
        scenario.onActivity { registerActivity ->
            registerActivity.usernameEditText.setText("testUser")
            registerActivity.passwordEditText.setText("weakpass")
            registerActivity.confirmPasswordEditText.setText("weakpass")

            registerActivity.register()

            assertTrue(registerActivity.passwordRequirementsTextView.visibility == View.VISIBLE)
        }
    }
}
