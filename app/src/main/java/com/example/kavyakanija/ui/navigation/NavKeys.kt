package com.example.kavyakanija.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {
    @Serializable
    data object Login : Screen

    @Serializable
    data object Discovery : Screen

    @Serializable
    data class PoemDetail(val id: Int) : Screen

    @Serializable
    data object Favorites : Screen
}
