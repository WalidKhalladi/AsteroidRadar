package com.walidkh.android.asteroidradar.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "PictureOfDay")
data class PictureOfDay(

    @ColumnInfo(name = "mediaType")
    @Json(name = "media_type") val mediaType: String,

    @ColumnInfo(name = "title")
    val title: String,

    @PrimaryKey
    val url: String


): Parcelable