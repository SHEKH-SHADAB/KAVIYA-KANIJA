package com.example.kavyakanija.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poets")
data class Poet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val era: String, // e.g., "Navodaya", "Navya", "Dalita-Bandaya"
    val bio: String,
    val imageUrl: String? = null
)
