package com.walidkh.android.asteroidradar.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.walidkh.android.asteroidradar.database.AsteroidRadarDataBase
import com.walidkh.android.asteroidradar.repository.AsteroidRadarRepository
import retrofit2.HttpException

class RefreshDataWork (applicationContext: Context, params: WorkerParameters)
    : CoroutineWorker(applicationContext, params) {

    companion object {
        const val WORK_NAME = "AsteroidRadarRefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = AsteroidRadarDataBase.getInstance(applicationContext)
        val repository = AsteroidRadarRepository(database)

        return try {
            repository.refreshAsteroids()
            repository.refreshPictureOfTheDay()
            Result.success()
        } catch (exception: HttpException) {
            Result.retry()
        }
    }

}