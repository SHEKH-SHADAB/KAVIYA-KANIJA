package com.example.kavyakanija.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "poems",
    foreignKeys = [
        ForeignKey(
            entity = Poet::class,
            parentColumns = ["id"],
            childColumns = ["poetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["poetId"])]
)
data class Poem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val poetId: Int,
    val title: String,
    val content: String,
    val genre: String, // e.g., "Nature", "Patriotic", "Philosophical"
    val isFavorite: Boolean = false,
    val categoryColor: String? = null, // Hex color string for the genre/category
    val titleEnglish: String? = null,
    val contentEnglish: String? = null,
    val audioUrl: String? = null
)
