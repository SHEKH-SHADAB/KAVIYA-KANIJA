package com.example.kavyakanija.ui.discovery

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kavyakanija.data.model.Poem
import com.example.kavyakanija.ui.theme.EmeraldGreen
import com.example.kavyakanija.ui.theme.PremiumGradientEnd
import com.example.kavyakanija.ui.theme.PremiumGradientStart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryListContent(
    viewModel: DiscoveryViewModel,
    onPoemClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Hero Section
            item {
                HeroSection(
                    featuredPoem = uiState.featuredPoem,
                    onClick = { uiState.featuredPoem?.let { onPoemClick(it.id) } }
                )
            }

            // Sticky Header with Search and Filters
            item {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(top = 24.dp, bottom = 8.dp)
                ) {
                    SearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = viewModel::onSearchQueryChange,
                        onSearch = {},
                        active = false,
                        onActiveChange = {},
                        placeholder = { Text("Title, Poet or Keyword...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = SearchBarDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp)
                        )
                    ) { }

                    Spacer(modifier = Modifier.height(16.dp))

                    GenreFilterChips(
                        genres = uiState.genres,
                        selectedGenre = uiState.selectedGenre,
                        onGenreSelected = viewModel::onGenreSelected
                    )
                }
            }

            // Poet Discovery Section
            item {
                Text(
                    text = "Famous Poets",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.poets) { poet ->
                        PoetDiscoveryCard(poet = poet)
                    }
                }
            }

            // Poem List Title
            item {
                Text(
                    text = if (uiState.selectedGenre != null) "${uiState.selectedGenre} Poems" else "Latest Poems",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)
                )
            }

            // Poem List
            if (uiState.filteredPoems.isEmpty() && !uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No matches found", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            } else {
                items(uiState.filteredPoems, key = { it.id }) { poem ->
                    Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                        PoemCard(
                            poem = poem,
                            onClick = { onPoemClick(poem.id) },
                            onFavoriteToggle = { viewModel.toggleFavorite(poem.id, !poem.isFavorite) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PoemList(
    poems: List<Poem>,
    onPoemClick: (Poem) -> Unit,
    onFavoriteToggle: (Int, Boolean) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(poems, key = { it.id }) { poem ->
            PoemCard(
                poem = poem,
                onClick = { onPoemClick(poem) },
                onFavoriteToggle = { onFavoriteToggle(poem.id, !poem.isFavorite) }
            )
        }
    }
}

@Composable
fun HeroSection(
    featuredPoem: Poem?,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hero")
    val animOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(20000, easing = LinearEasing), RepeatMode.Reverse),
        label = "offset"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp)
            .clip(RoundedCornerShape(bottomStart = 56.dp, bottomEnd = 56.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(PremiumGradientStart, PremiumGradientEnd, EmeraldGreen),
                    start = androidx.compose.ui.geometry.Offset(animOffset, 0f),
                    end = androidx.compose.ui.geometry.Offset(animOffset + 800f, 800f)
                )
            )
            .clickable(onClick = onClick)
    ) {
        // Sophisticated decorative overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f)),
                        center = androidx.compose.ui.geometry.Offset(500f, 0f),
                        radius = 1000f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Surface(
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "FEATURED KAVITE",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            featuredPoem?.let {
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-1).sp,
                        lineHeight = 44.sp
                    ),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it.content.take(120) + "...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = 2,
                    lineHeight = 26.sp
                )
            }
        }
    }
}

@Composable
fun GenreFilterChips(
    genres: List<String>,
    selectedGenre: String?,
    onGenreSelected: (String?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            FilterChip(
                selected = selectedGenre == null,
                onClick = { onGenreSelected(null) },
                label = { Text("All") },
                shape = CircleShape,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = Color.White
                )
            )
        }
        items(genres) { genre ->
            FilterChip(
                selected = selectedGenre == genre,
                onClick = { onGenreSelected(genre) },
                label = { Text(genre) },
                shape = CircleShape,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

@Composable
fun PoetDiscoveryCard(poet: com.example.kavyakanija.data.model.Poet) {
    Surface(
        modifier = Modifier
            .width(160.dp)
            .height(220.dp),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), Color.Transparent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoStories,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = poet.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = poet.era,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun PoemCard(
    poem: Poem,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val themeColor = remember(poem.categoryColor) {
        try {
            if (poem.categoryColor != null) Color(android.graphics.Color.parseColor(poem.categoryColor))
            else primaryColor
        } catch (e: Exception) {
            primaryColor
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (isPressed) 4.dp else 12.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = themeColor.copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = onClick,
        interactionSource = interactionSource
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Refined Icon Container
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(themeColor.copy(alpha = 0.15f), themeColor.copy(alpha = 0.05f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoStories,
                    contentDescription = null,
                    tint = themeColor,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = themeColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = poem.genre.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = themeColor,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = poem.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = poem.content,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(
                onClick = onFavoriteToggle,
                modifier = Modifier.background(Color.Transparent, CircleShape)
            ) {
                AnimatedContent(
                    targetState = poem.isFavorite,
                    transitionSpec = {
                        (scaleIn() + fadeIn() togetherWith scaleOut() + fadeOut())
                    },
                    label = "fav"
                ) { isFav ->
                    Icon(
                        imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFav) Color(0xFFE91E63) else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
