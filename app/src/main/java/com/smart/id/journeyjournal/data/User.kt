package com.smart.id.journeyjournal.data

import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("userEmail") var email:String = ""
    @SerializedName("password") var password:String = ""
    constructor()
    constructor(username:String,password:String){
        this.email = username
        this.password = password
    }
}