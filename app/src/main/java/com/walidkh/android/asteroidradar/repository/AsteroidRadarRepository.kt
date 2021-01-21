package com.walidkh.android.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.walidkh.android.asteroidradar.database.AsteroidRadarDataBase
import com.walidkh.android.asteroidradar.model.Asteroid
import com.walidkh.android.asteroidradar.model.PictureOfDay
import com.walidkh.android.asteroidradar.network.AsteroidApi
import com.walidkh.android.asteroidradar.network.parseAsteroidsJsonResult
import com.walidkh.android.asteroidradar.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRadarRepository (private val database: AsteroidRadarDataBase) {

    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val today: String
        get() = formatter.format(Date())

    private fun weekDateFromToday(): String {
        val date = Calendar.getInstance()
        date.add(Calendar.DATE, +7)
        val weekFromNow = date.time

        return formatter.format(weekFromNow)
    }

    private val weekFromToday: String
        get() =  weekDateFromToday()

    val asteroids: LiveData<List<Asteroid>> =
        database.getAsteroidRadarDao().getAllAsteroidLiveData()

    val asteroidsOfToday: LiveData<List<Asteroid>> =
        database.getAsteroidRadarDao().getTodayAsteroids(today)

    val asteroidsOfTheWeek: LiveData<List<Asteroid>> =
        database.getAsteroidRadarDao().getWeekAsteroids(today, weekFromToday)

    val pictureOfTheDay: LiveData<PictureOfDay> =
        database.getPictureOfTheDayDao().getPictureOfTheDayLiveData()

    /**
     * Refresh the videos stored in the offline cache.
     *
     * To load the asteroids, observe [asteroids]
     */
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val jsonResult = AsteroidApi.retrofitService.getAsteroids(Constants.API_KEY)
                val jsonObject = JSONObject(jsonResult)
                val asteroidList = parseAsteroidsJsonResult(jsonResult = jsonObject)

                database.getAsteroidRadarDao().insertAllAsteroids(*asteroidList.toTypedArray())
            } catch (exception: Throwable) {
                Log.e(TAG, "Exception: ${exception.message}")
            }
        }
    }

    /**
     * Refresh the picture of the day stored in the offline cache
     *
     * To load the picture, observe [pictureOfTheDay]
     */
    suspend fun refreshPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            try {
                val pictureOfTheDay = AsteroidApi.retrofitService.getPhotoOfTheDay(Constants.API_KEY)
                database.getPictureOfTheDayDao().insertPictureOfTheDay(pictureOfTheDay)
            } catch (exception: Throwable) {
                Log.e(TAG, "Exception: ${exception.message}")
            }
        }
    }

    companion object {
        private const val TAG = "AsteroidRadarRepository"
    }

}