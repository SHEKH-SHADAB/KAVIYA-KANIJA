package com.example.kavyakanija.ui.reader

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kavyakanija.data.model.Poem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    viewModel: ReaderViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    
    val readerTheme = uiState.readerTheme
    val backgroundColor = if (readerTheme == ReaderTheme.Default) MaterialTheme.colorScheme.background else readerTheme.backgroundColor
    val textColor = if (readerTheme == ReaderTheme.Default) MaterialTheme.colorScheme.onBackground else readerTheme.textColor

    var showThemePicker by remember { mutableStateOf(false) }
    var showFontSizePicker by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()

    val displayTitle = if (uiState.isTranslated && !uiState.poem?.titleEnglish.isNullOrEmpty()) {
        uiState.poem?.titleEnglish ?: ""
    } else {
        uiState.poem?.title ?: ""
    }

    val displayContent = if (uiState.isTranslated && !uiState.poem?.contentEnglish.isNullOrEmpty()) {
        uiState.poem?.contentEnglish ?: ""
    } else {
        uiState.poem?.content ?: ""
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = "POEM", 
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 4.sp,
                            fontWeight = FontWeight.Black
                        )
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = textColor)
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        uiState.poem?.let { viewModel.toggleFavorite(!it.isFavorite) }
                    }) {
                        Icon(
                            imageVector = if (uiState.poem?.isFavorite == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (uiState.poem?.isFavorite == true) Color.Red else textColor
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = backgroundColor.copy(alpha = 0.9f),
                    titleContentColor = textColor
                )
            )
        },
        bottomBar = {
            InteractivePlayerBar(
                isPlaying = uiState.isPlaying,
                isTranslated = uiState.isTranslated,
                onPlayPause = viewModel::togglePlayPause,
                onToggleTranslation = viewModel::toggleTranslation,
                onFontSizeClick = { showFontSizePicker = true },
                onThemeClick = { showThemePicker = true },
                hasTranslation = uiState.poem?.titleEnglish != null,
                themeColor = MaterialTheme.colorScheme.primary,
                textColor = textColor
            )
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            uiState.poem?.let { poem ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 32.dp, vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Animated Genre Badge
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = poem.genre.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Title
                    Text(
                        text = displayTitle,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center,
                            letterSpacing = (-1).sp,
                            lineHeight = 46.sp
                        ),
                        color = textColor
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    )
                    
                    Spacer(modifier = Modifier.height(64.dp))
                    
                    Text(
                        text = displayContent,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = (uiState.fontSize + 2).sp,
                            lineHeight = (uiState.fontSize * 1.8).sp,
                            textAlign = TextAlign.Center,
                            fontStyle = if (uiState.isTranslated) FontStyle.Italic else FontStyle.Normal
                        ),
                        color = textColor.copy(alpha = 0.9f),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(150.dp))
                }
            }
        }

        if (showThemePicker) {
            ModalBottomSheet(
                onDismissRequest = { showThemePicker = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                ThemePickerContent(
                    currentTheme = uiState.readerTheme,
                    onThemeSelected = {
                        viewModel.updateTheme(it)
                        showThemePicker = false
                    }
                )
            }
        }

        if (showFontSizePicker) {
            ModalBottomSheet(
                onDismissRequest = { showFontSizePicker = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                FontSizePickerContent(
                    currentSize = uiState.fontSize,
                    onSizeChanged = viewModel::updateFontSize
                )
            }
        }
    }
}

@Composable
fun ThemePickerContent(
    currentTheme: ReaderTheme,
    onThemeSelected: (ReaderTheme) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "Choose Theme",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ReaderTheme.values().forEach { theme ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onThemeSelected(theme) }
                ) {
                    val borderStroke = if (currentTheme == theme) {
                        androidx.compose.foundation.BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
                    } else {
                        androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    }
                    
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = CircleShape,
                        color = if (theme == ReaderTheme.Default) MaterialTheme.colorScheme.background else theme.backgroundColor,
                        border = borderStroke
                    ) {
                        if (currentTheme == theme) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = if (theme == ReaderTheme.Dark) Color.White else MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = theme.name,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun FontSizePickerContent(
    currentSize: Float,
    onSizeChanged: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "Adjust Text Size",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = { if (currentSize > 12f) onSizeChanged(currentSize - 2f) },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease")
            }
            
            Slider(
                value = currentSize,
                onValueChange = onSizeChanged,
                valueRange = 12f..36f,
                modifier = Modifier.weight(1f),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
            
            IconButton(
                onClick = { if (currentSize < 36f) onSizeChanged(currentSize + 2f) },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Increase")
            }
        }
        Text(
            text = "${currentSize.toInt()} sp",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun InteractivePlayerBar(
    isPlaying: Boolean,
    isTranslated: Boolean,
    onPlayPause: () -> Unit,
    onToggleTranslation: (Boolean) -> Unit,
    onFontSizeClick: () -> Unit,
    onThemeClick: () -> Unit,
    hasTranslation: Boolean,
    themeColor: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .navigationBarsPadding()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(40.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
            tonalElevation = 8.dp,
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Play Button
                val playButtonScale by animateFloatAsState(
                    targetValue = if (isPlaying) 1.1f else 1f,
                    animationSpec = if (isPlaying) infiniteRepeatable(
                        animation = tween(1000),
                        repeatMode = RepeatMode.Reverse
                    ) else spring(),
                    label = "playScale"
                )

                IconButton(
                    onClick = onPlayPause,
                    modifier = Modifier.size(64.dp)
                ) {
                    Surface(
                        color = themeColor,
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .graphicsLayer {
                                scaleX = playButtonScale
                                scaleY = playButtonScale
                            }
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // Controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onFontSizeClick) {
                        Icon(Icons.Default.FormatSize, null, tint = MaterialTheme.colorScheme.onSurface)
                    }
                    IconButton(onClick = onThemeClick) {
                        Icon(Icons.Default.Palette, null, tint = MaterialTheme.colorScheme.onSurface)
                    }
                    
                    if (hasTranslation) {
                        Surface(
                            onClick = { onToggleTranslation(!isTranslated) },
                            color = if (isTranslated) themeColor else Color.Transparent,
                            shape = RoundedCornerShape(20.dp),
                            border = if (!isTranslated) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                text = if (isTranslated) "EN" else "KN",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isTranslated) Color.White else themeColor
                            )
                        }
                    }
                }
            }
        }
    }
}
