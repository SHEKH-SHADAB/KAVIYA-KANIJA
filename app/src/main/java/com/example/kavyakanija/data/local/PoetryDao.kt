package com.example.kavyakanija.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.kavyakanija.data.model.Poem
import com.example.kavyakanija.data.model.Poet
import kotlinx.coroutines.flow.Flow

@Dao
interface PoetryDao {
    @Query("SELECT * FROM poets")
    fun getAllPoets(): Flow<List<Poet>>

    @Query("SELECT * FROM poets WHERE id = :poetId")
    fun getPoetById(poetId: Int): Flow<Poet?>

    @Query("SELECT * FROM poems")
    fun getAllPoems(): Flow<List<Poem>>

    @Query("SELECT * FROM poems WHERE poetId = :poetId")
    fun getPoemsByPoet(poetId: Int): Flow<List<Poem>>

    @Query("SELECT * FROM poems WHERE id = :poemId")
    fun getPoemById(poemId: Int): Flow<Poem?>

    @Query("SELECT * FROM poems WHERE isFavorite = 1")
    fun getFavoritePoems(): Flow<List<Poem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoets(poets: List<Poet>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoems(poems: List<Poem>)

    @Update
    suspend fun updatePoem(poem: Poem)

    @Query("UPDATE poems SET isFavorite = :isFavorite WHERE id = :poemId")
    suspend fun updateFavoriteStatus(poemId: Int, isFavorite: Boolean)

    @Query("SELECT * FROM poems WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    fun searchPoems(query: String): Flow<List<Poem>>
}
