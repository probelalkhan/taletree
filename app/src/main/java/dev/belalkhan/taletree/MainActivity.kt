package dev.belalkhan.taletree

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.belalkhan.taletree.ui.theme.TaleTreeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener { mainViewModel.authState.value != AuthState.Loading }

        setContent {
            TaleTreeTheme {
                val navController = rememberNavController()
                val authState = mainViewModel.authState.collectAsStateWithLifecycle().value
                if (authState != AuthState.Loading) {
                    AppNavigator(navController, authState)
                }
            }
        }
    }
}