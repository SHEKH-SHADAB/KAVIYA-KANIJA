package com.example.kavyakanija.ui.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kavyakanija.data.model.Poem
import com.example.kavyakanija.data.model.Poet
import com.example.kavyakanija.data.repository.PoetryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DiscoveryUiState(
    val poets: List<Poet> = emptyList(),
    val poems: List<Poem> = emptyList(),
    val searchQuery: String = "",
    val selectedGenre: String? = null,
    val genres: List<String> = emptyList(),
    val filteredPoems: List<Poem> = emptyList(),
    val featuredPoem: Poem? = null,
    val isLoading: Boolean = true
)

class DiscoveryViewModel(
    private val repository: PoetryRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedGenre = MutableStateFlow<String?>(null)

    val uiState: StateFlow<DiscoveryUiState> = combine(
        repository.allPoets,
        repository.allPoems,
        _searchQuery,
        _selectedGenre
    ) { poets, poems, query, genre ->
        val genres = poems.map { it.genre }.distinct()
        
        val filtered = poems.filter { poem ->
            val matchesQuery = if (query.isEmpty()) true else {
                poem.title.contains(query, ignoreCase = true) || 
                poem.content.contains(query, ignoreCase = true) ||
                poem.genre.contains(query, ignoreCase = true)
            }
            val matchesGenre = if (genre == null) true else poem.genre == genre
            
            matchesQuery && matchesGenre
        }

        DiscoveryUiState(
            poets = poets,
            poems = poems,
            searchQuery = query,
            selectedGenre = genre,
            genres = genres,
            filteredPoems = filtered,
            featuredPoem = poems.firstOrNull(), // Simplified logic for featured
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DiscoveryUiState()
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onGenreSelected(genre: String?) {
        _selectedGenre.value = if (_selectedGenre.value == genre) null else genre
    }

    fun toggleFavorite(poemId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(poemId, isFavorite)
        }
    }
}
