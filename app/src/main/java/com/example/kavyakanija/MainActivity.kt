package com.example.kavyakanija

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.kavyakanija.data.repository.PoetryRepository
import com.example.kavyakanija.ui.discovery.DiscoveryListContent
import com.example.kavyakanija.ui.discovery.DiscoveryViewModel
import com.example.kavyakanija.ui.kanija.KanijaScreen
import com.example.kavyakanija.ui.kanija.KanijaViewModel
import com.example.kavyakanija.ui.login.LoginScreen
import com.example.kavyakanija.ui.navigation.Screen
import com.example.kavyakanija.ui.reader.ReaderScreen
import com.example.kavyakanija.ui.reader.ReaderViewModel
import com.example.kavyakanija.ui.theme.KavyaKanijaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = (application as KavyaKanijaApplication).repository

        setContent {
            KavyaKanijaTheme {
                KavyaKanijaApp(repository)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun KavyaKanijaApp(repository: PoetryRepository) {
    val context = LocalContext.current
    val backStack = rememberNavBackStack(Screen.Login)
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()
    
    // Shared ViewModels
    val discoveryViewModel: DiscoveryViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DiscoveryViewModel(repository) as T
            }
        }
    )

    val kanijaViewModel: KanijaViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return KanijaViewModel(repository) as T
            }
        }
    )

    val currentScreen = backStack.lastOrNull() ?: Screen.Login

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentScreen == Screen.Discovery || currentScreen == Screen.Favorites) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = currentScreen == Screen.Discovery,
                        onClick = { 
                            if (currentScreen != Screen.Discovery) {
                                backStack.clear()
                                (backStack as MutableList<NavKey>).add(Screen.Discovery)
                            }
                        },
                        icon = { Icon(Icons.Default.Search, contentDescription = null) },
                        label = { Text("ಹುಡುಕು") }
                    )
                    NavigationBarItem(
                        selected = currentScreen == Screen.Favorites,
                        onClick = { 
                            if (currentScreen != Screen.Favorites) {
                                backStack.clear()
                                (backStack as MutableList<NavKey>).add(Screen.Favorites)
                            }
                        },
                        icon = { Icon(Icons.Default.AutoStories, contentDescription = null) },
                        label = { Text("ಕಣಜ") }
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        NavDisplay<NavKey>(
            backStack = backStack,
            sceneStrategy = listDetailStrategy,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            onBack = {
                if (backStack.isNotEmpty()) {
                    backStack.removeAt(backStack.size - 1)
                }
            },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Screen.Login> {
                    LoginScreen(
                        onLoginSuccess = {
                            backStack.clear()
                            (backStack as MutableList<NavKey>).add(Screen.Discovery)
                        }
                    )
                }

                entry<Screen.Discovery>(
                    metadata = ListDetailSceneStrategy.listPane(
                        detailPlaceholder = {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("ಓದಲು ಕವಿತೆಯನ್ನು ಆರಿಸಿ", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    )
                ) {
                    DiscoveryListContent(
                        viewModel = discoveryViewModel,
                        onPoemClick = { id ->
                            (backStack as MutableList<NavKey>).add(Screen.PoemDetail(id))
                        }
                    )
                }

                entry<Screen.Favorites>(
                    metadata = ListDetailSceneStrategy.listPane(
                        detailPlaceholder = {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("ಓದಲು ಕವಿತೆಯನ್ನು ಆರಿಸಿ", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    )
                ) {
                    KanijaScreen(
                        viewModel = kanijaViewModel,
                        onPoemClick = { id ->
                            (backStack as MutableList<NavKey>).add(Screen.PoemDetail(id))
                        }
                    )
                }

                entry<Screen.PoemDetail>(
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { key ->
                    val readerViewModel: ReaderViewModel = viewModel(
                        key = "ReaderViewModel_${key.id}",
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                @Suppress("UNCHECKED_CAST")
                                return ReaderViewModel(
                                    application = (context.applicationContext as android.app.Application),
                                    poemId = key.id,
                                    repository = repository
                                ) as T
                            }
                        }
                    )
                    ReaderScreen(
                        viewModel = readerViewModel,
                        onBack = {
                            if (backStack.isNotEmpty()) {
                                backStack.removeAt(backStack.size - 1)
                            }
                        }
                    )
                }
            }
        )
    }
}
