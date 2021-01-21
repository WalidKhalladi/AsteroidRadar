package com.walidkh.android.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.walidkh.android.asteroidradar.database.dao.AsteroidRadarDao
import com.walidkh.android.asteroidradar.database.dao.PictureOfTheDayDao
import com.walidkh.android.asteroidradar.model.Asteroid
import com.walidkh.android.asteroidradar.model.PictureOfDay
import com.walidkh.android.asteroidradar.utils.Constants.DATABASE_NAME

@Database(entities = [Asteroid::class, PictureOfDay::class], version = 1)
abstract class AsteroidRadarDataBase: RoomDatabase() {

    abstract fun getAsteroidRadarDao(): AsteroidRadarDao
    abstract fun getPictureOfTheDayDao(): PictureOfTheDayDao

    companion object {
        private lateinit var INSTANCE: AsteroidRadarDataBase

        fun getInstance(context: Context) : AsteroidRadarDataBase {

            synchronized(AsteroidRadarDataBase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AsteroidRadarDataBase::class.java,
                        DATABASE_NAME).build()
                }
            }
            return INSTANCE
        }
    }
}