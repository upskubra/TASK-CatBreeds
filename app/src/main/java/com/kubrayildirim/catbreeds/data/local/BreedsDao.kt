package com.kubrayildirim.catbreeds.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kubrayildirim.catbreeds.data.model.Breed

@Dao
interface BreedsDao {
    @Query("SELECT * FROM breed_table")
    fun getBreeds(): LiveData<List<Breed>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreed(breed: Breed): Long

    @Delete
    suspend fun deleteBreed(breed: Breed)
}