package com.example.kavyakanija.ui.kanija

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kavyakanija.data.model.Poem
import com.example.kavyakanija.data.repository.PoetryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class KanijaUiState(
    val favoritePoems: List<Poem> = emptyList(),
    val isLoading: Boolean = true
)

class KanijaViewModel(
    private val repository: PoetryRepository
) : ViewModel() {

    val uiState: StateFlow<KanijaUiState> = repository.favoritePoems
        .map { KanijaUiState(favoritePoems = it, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = KanijaUiState()
        )

    fun toggleFavorite(poemId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(poemId, isFavorite)
        }
    }
}
