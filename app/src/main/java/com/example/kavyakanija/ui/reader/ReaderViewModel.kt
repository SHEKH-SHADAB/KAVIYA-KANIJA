package com.example.kavyakanija.ui.reader

import android.app.Application
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kavyakanija.data.model.Poem
import com.example.kavyakanija.data.model.Poet
import com.example.kavyakanija.data.repository.PoetryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

enum class ReaderTheme(val backgroundColor: Color, val textColor: Color) {
    Default(Color.Transparent, Color.Unspecified),
    Sepia(Color(0xFFF4ECD8), Color(0xFF5B4636)),
    Parchment(Color(0xFFFCF5E5), Color(0xFF333333)),
    Dark(Color(0xFF1C1B1F), Color(0xFFE6E1E5))
}

data class ReaderUiState(
    val poem: Poem? = null,
    val poet: Poet? = null,
    val fontSize: Float = 18f,
    val readerTheme: ReaderTheme = ReaderTheme.Default,
    val isTranslated: Boolean = false,
    val isLoading: Boolean = true,
    val isPlaying: Boolean = false,
    val isTtsReady: Boolean = false
)

class ReaderViewModel(
    application: Application,
    private val poemId: Int,
    private val repository: PoetryRepository
) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val _fontSize = MutableStateFlow(18f)
    private val _readerTheme = MutableStateFlow(ReaderTheme.Default)
    private val _isTranslated = MutableStateFlow(false)
    private val _isPlaying = MutableStateFlow(false)
    private val _isTtsReady = MutableStateFlow(false)

    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(application, this)
    }

    val uiState: StateFlow<ReaderUiState> = combine(
        repository.getPoemById(poemId),
        _fontSize,
        _readerTheme,
        _isTranslated,
        _isPlaying,
        _isTtsReady
    ) { args ->
        val poem = args[0] as? Poem
        val fontSize = args[1] as Float
        val theme = args[2] as ReaderTheme
        val isTranslated = args[3] as Boolean
        val isPlaying = args[4] as Boolean
        val isTtsReady = args[5] as Boolean

        if (poem != null) {
            ReaderUiState(
                poem = poem,
                poet = null,
                fontSize = fontSize,
                readerTheme = theme,
                isTranslated = isTranslated,
                isPlaying = isPlaying,
                isTtsReady = isTtsReady,
                isLoading = false
            )
        } else {
            ReaderUiState(isLoading = false)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ReaderUiState()
    )

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            _isTtsReady.value = true
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isPlaying.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isPlaying.value = false
                }

                override fun onError(utteranceId: String?) {
                    _isPlaying.value = false
                }
            })
        }
    }

    fun updateFontSize(newSize: Float) {
        _fontSize.value = newSize
    }

    fun updateTheme(newTheme: ReaderTheme) {
        _readerTheme.value = newTheme
    }

    fun toggleTranslation(enabled: Boolean) {
        _isTranslated.value = enabled
        if (_isPlaying.value) {
            stopSpeaking()
        }
    }

    fun toggleFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(poemId, isFavorite)
        }
    }

    fun togglePlayPause() {
        if (_isPlaying.value) {
            stopSpeaking()
        } else {
            val state = uiState.value
            val poem = state.poem ?: return
            val textToSpeak = if (state.isTranslated) poem.contentEnglish else poem.content
            val locale = if (state.isTranslated) Locale.US else Locale("kn", "IN")
            
            if (textToSpeak != null) {
                speak(textToSpeak, locale)
            }
        }
    }

    private fun speak(text: String, locale: Locale) {
        tts?.let {
            it.language = locale
            it.speak(text, TextToSpeech.QUEUE_FLUSH, null, "poem_id_$poemId")
            _isPlaying.value = true
        }
    }

    private fun stopSpeaking() {
        tts?.stop()
        _isPlaying.value = false
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}

