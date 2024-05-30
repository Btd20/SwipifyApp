package com.example.swipifyapp

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.mikephil.charting.charts.BarChart
import org.hamcrest.CoreMatchers.allOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


@RunWith(AndroidJUnit4::class)
class ReportsActivityTest {

    @get:Rule
    val intentsRule = IntentsTestRule(ReportsActivity::class.java)

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testBackToMainButton() {
        onView(withId(R.id.btnBackToMain))
            .perform(click())

        // Assert that the MainActivity is launched
        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun testStoragePermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                intentsRule.activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }

        assertEquals(
            PackageManager.PERMISSION_GRANTED,
            ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        )
    }

    @Test
    fun testGetBitmapFromView() {
        val view = intentsRule.activity.createBarChart(10, 5)
        val bitmap = intentsRule.activity.getBitmapFromView(view)

        // Check if bitmap is not null
        assert(bitmap != null)

        // Check if bitmap dimensions match view dimensions
        assertEquals(view.width, bitmap.width)
        assertEquals(view.height, bitmap.height)
    }

    @Test
    fun testSavePdfWithImage() {
        val activity = intentsRule.activity
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val filePath = activity.getExternalFilesDir(null)?.absolutePath + "/test.pdf"

        activity.savePdfWithImage(filePath, bitmap, "Test PDF")

        // Check if file is saved successfully
        val file = File(filePath)
        assertTrue(file.exists())

        // Delete the file after the test
        file.delete()
    }
}
