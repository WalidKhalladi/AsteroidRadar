package com.walidkh.android.asteroidradar.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.walidkh.android.asteroidradar.model.Asteroid

@Dao
interface AsteroidRadarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAsteroids(vararg asteroid: Asteroid)

    @Query("SELECT * FROM asteroid ORDER BY close_approach_date")
    fun getAllAsteroidLiveData(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid WHERE close_approach_date=:today")
    fun getTodayAsteroids(today: String): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid WHERE close_approach_date BETWEEN :today AND :endDate ORDER BY close_approach_date")
    fun getWeekAsteroids(today: String, endDate: String): LiveData<List<Asteroid>>
}