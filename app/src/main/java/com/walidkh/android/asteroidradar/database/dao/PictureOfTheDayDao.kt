package com.walidkh.android.asteroidradar.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.walidkh.android.asteroidradar.model.PictureOfDay

@Dao
interface PictureOfTheDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfTheDay(pictureOfDay: PictureOfDay)

    @Query("SELECT * FROM PictureOfDay ")
    fun getPictureOfTheDayLiveData(): LiveData<PictureOfDay>
}