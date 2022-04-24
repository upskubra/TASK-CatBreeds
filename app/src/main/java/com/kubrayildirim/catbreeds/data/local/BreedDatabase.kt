package com.kubrayildirim.catbreeds.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kubrayildirim.catbreeds.data.model.Breed
import com.kubrayildirim.catbreeds.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Breed::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BreedDatabase : RoomDatabase() {

    abstract fun getArticleDao(): BreedsDao

    class Callback @Inject constructor(
        private val database: Provider<BreedDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()
}