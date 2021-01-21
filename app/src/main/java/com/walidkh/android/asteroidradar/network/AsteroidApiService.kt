package com.walidkh.android.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.walidkh.android.asteroidradar.model.Asteroid
import com.walidkh.android.asteroidradar.model.PictureOfDay
import com.walidkh.android.asteroidradar.utils.Constants.API_KEY
import com.walidkh.android.asteroidradar.utils.Constants.BASE_URL
import kotlinx.coroutines.Deferred
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

enum class AsteroidDateApiFilter {
    SHOW_ALL,
    SHOW_TODAY,
    SHOW_WEEK
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface AsteroidApiService {

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(@Query("api_key") key: String) : String

    @GET("planetary/apod")
    suspend fun getPhotoOfTheDay(@Query("api_key") key: String) : PictureOfDay

    @GET("neo/rest/v1/feed?api_key=$API_KEY")
    suspend fun getTodayAsteroids(@Query("start_date") startDate: String, @Query("api_key") key: String) : String

    @GET("neo/rest/v1/feed?api_key=$API_KEY")
    suspend fun getWeekAsteroids(@Query("start_date") startDate: String, @Query("end_date") endDate: String, @Query("api_key") key: String) : String

}

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}
