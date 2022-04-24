package com.kubrayildirim.catbreeds.di

import android.app.Application
import androidx.room.Room
import com.kubrayildirim.catbreeds.data.local.BreedDatabase
import com.kubrayildirim.catbreeds.data.local.BreedsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application,
        callback: BreedDatabase.Callback
    ): BreedDatabase {
        return Room.databaseBuilder(application, BreedDatabase::class.java, "breed_table")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideBreedDao(db: BreedDatabase): BreedsDao {
        return db.getArticleDao()
    }
}