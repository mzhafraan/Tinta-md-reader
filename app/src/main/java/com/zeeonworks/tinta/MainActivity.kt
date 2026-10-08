package com.zeeonworks.tinta

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.zeeonworks.tinta.data.repository.DocumentRepository
import com.zeeonworks.tinta.ui.navigation.Screen
import com.zeeonworks.tinta.ui.navigation.TintaNavGraph
import com.zeeonworks.tinta.ui.theme.TintaTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var incomingDispatchUri by mutableStateOf<Uri?>(null)
    private lateinit var repository: DocumentRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        repository = DocumentRepository(applicationContext)
        incomingDispatchUri = extractIncomingUri(intent)

        setContent {
            TintaTheme {
                val navController = rememberNavController()
                TintaNavGraph(navController = navController)

                val uriToOpen = incomingDispatchUri
                LaunchedEffect(uriToOpen) {
                    if (uriToOpen != null) {
                        lifecycleScope.launch {
                            val result = repository.openAndCacheDocument(uriToOpen)
                            result.onSuccess { docResult ->
                                navController.navigate(Screen.Reader.createRoute(docResult.archive.id))
                            }.onFailure {
                                navController.navigate(Screen.Reader.createRoute(uriToOpen.toString()))
                            }
                            incomingDispatchUri = null
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        incomingDispatchUri = extractIncomingUri(intent)
    }

    private fun extractIncomingUri(intent: Intent?): Uri? {
        if (intent == null) return null
        if (intent.action == Intent.ACTION_VIEW) {
            return intent.data
        }
        return null
    }
}