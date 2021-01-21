package com.walidkh.android.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.walidkh.android.asteroidradar.database.AsteroidRadarDataBase
import com.walidkh.android.asteroidradar.model.Asteroid
import com.walidkh.android.asteroidradar.model.PictureOfDay
import com.walidkh.android.asteroidradar.network.AsteroidDateApiFilter
import com.walidkh.android.asteroidradar.repository.AsteroidRadarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(
    application: Application
) : ViewModel() {

    private val _navigateToDetails = MutableLiveData<Asteroid>()
    val navigateToDetails: LiveData<Asteroid>
        get() = _navigateToDetails


    private val database = AsteroidRadarDataBase.getInstance(application)
    private val asteroidsRepository = AsteroidRadarRepository(database)

    init {
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
            asteroidsRepository.refreshPictureOfTheDay()
        }
    }

    val asteroidsList = asteroidsRepository.asteroids
    val asteroidsListOfToday = asteroidsRepository.asteroidsOfToday
    val asteroidsListOfTheWeek = asteroidsRepository.asteroidsOfTheWeek
    val pictureOfTheDay = asteroidsRepository.pictureOfTheDay


    companion object {
        private const val TAG = "MainViewModel"
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToDetails.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToDetails.value = null
    }

    fun getAsteroidsByFilter(filter: AsteroidDateApiFilter): LiveData<List<Asteroid>> {
       return when(filter) {
            AsteroidDateApiFilter.SHOW_TODAY -> asteroidsListOfToday
            AsteroidDateApiFilter.SHOW_WEEK -> asteroidsListOfTheWeek
           else -> asteroidsList
        }
    }
}