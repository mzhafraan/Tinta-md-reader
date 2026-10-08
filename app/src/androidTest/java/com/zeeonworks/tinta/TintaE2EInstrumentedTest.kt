package com.zeeonworks.tinta

import android.net.Uri
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.zeeonworks.tinta.data.model.DocumentArchive
import com.zeeonworks.tinta.data.preferences.ArchivePreferences
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class TintaE2EInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val preferences = ArchivePreferences(context)

        // Write a test dispatch markdown file to internal files dir
        val sampleFile = File(context.filesDir, "Gutenberg_Chronicle.md")
        sampleFile.writeText(
            """
            # The Gutenberg Chronicle
            
            > "An archive preserved in ink is an archive preserved forever."
            
            This is an editorial test dispatch verifying Tinta reading capabilities.
            
            ```kotlin
            val ink = "Charcoal"
            ```
            """.trimIndent()
        )

        val archive = DocumentArchive(
            id = "gutenberg_test",
            uriString = Uri.fromFile(sampleFile).toString(),
            title = "The Gutenberg Chronicle",
            lastOpenedEpoch = System.currentTimeMillis(),
            wordCount = 28,
            snippet = "This is an editorial test dispatch verifying Tinta reading capabilities.",
            localFilePath = sampleFile.absolutePath
        )

        runBlocking {
            preferences.saveOrUpdateArchive(archive)
        }
    }

    @Test
    fun archivesGrid_displaysClipping_andNavigatesToReader() {
        // Verify Archive card title is displayed in The Archives
        composeTestRule.onNodeWithText("The Gutenberg Chronicle").assertIsDisplayed()

        // Click on the archive card
        composeTestRule.onNodeWithText("The Gutenberg Chronicle").performClick()

        // Wait and verify Reader screen elements
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("THE GAZETTE • EDITORIAL DISPATCH").assertIsDisplayed()
        composeTestRule.onNodeWithText("The Gutenberg Chronicle").assertIsDisplayed()
    }
}
