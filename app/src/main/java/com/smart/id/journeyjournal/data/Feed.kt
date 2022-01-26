package com.smart.id.journeyjournal.data

import com.google.gson.annotations.SerializedName

data class Feed(
    @SerializedName("id") var id:String,
    @SerializedName("imageURL") var imageUrl:String,
    @SerializedName("title") val feedTitle:String,
    @SerializedName("description") val feedDescription:String,
    @SerializedName("location") val location:LocationLatLngs

){
    constructor() : this("","","","",LocationLatLngs(0.0,0.0))
}

data class LocationLatLngs(
    val latitude:Double,
    val longitude:Double
){
    constructor():this(0.0,0.0)
}