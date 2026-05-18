package com.example.kavyakanija.data.repository

import com.example.kavyakanija.data.local.PoetryDao
import com.example.kavyakanija.data.model.Poem
import com.example.kavyakanija.data.model.Poet
import kotlinx.coroutines.flow.Flow

class PoetryRepository(private val poetryDao: PoetryDao) {

    val allPoets: Flow<List<Poet>> = poetryDao.getAllPoets()
    val allPoems: Flow<List<Poem>> = poetryDao.getAllPoems()
    val favoritePoems: Flow<List<Poem>> = poetryDao.getFavoritePoems()

    fun getPoetById(poetId: Int): Flow<Poet?> = poetryDao.getPoetById(poetId)

    fun getPoemsByPoet(poetId: Int): Flow<List<Poem>> = poetryDao.getPoemsByPoet(poetId)

    fun getPoemById(poemId: Int): Flow<Poem?> = poetryDao.getPoemById(poemId)

    suspend fun toggleFavorite(poemId: Int, isFavorite: Boolean) {
        poetryDao.updateFavoriteStatus(poemId, isFavorite)
    }

    fun searchPoems(query: String): Flow<List<Poem>> = poetryDao.searchPoems(query)
}
