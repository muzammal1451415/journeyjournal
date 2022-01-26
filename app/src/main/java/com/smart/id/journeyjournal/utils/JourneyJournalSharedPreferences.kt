package com.smart.id.journeyjournal.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class JourneyJournalSharedPreferences(context: Context, name: String){
    var context:Context
    var name:String
    var sp:SharedPreferences
    var et:SharedPreferences.Editor
    init{
        this.context=context
        this.name=name
        sp=this.context.getSharedPreferences(this.name, MODE_PRIVATE)
        et=sp.edit()
    }

    fun getStringValue(key: String?): String? {
        return sp.getString(key, "N/A")
    }

    fun removeValue(key: String?) {
        et.remove(key).apply()
    }
    fun saveBooleanValue(key: String?, value: Boolean) {
        et.putBoolean(key, value).apply()
    }

    fun getBooleanValue(key: String?): Boolean {
        return sp.getBoolean(key, false)
    }


    fun saveIntValue(key: String?, value: Int) {
        et.putInt(key, value).apply()
    }

    fun getIntValue(key: String?): Int {
        return sp.getInt(key, 0)
    }

    fun saveStringValue(key: String?, value: String?) {
        et.putString(key, value).apply()
    }
    fun reg(listener:SharedPreferences.OnSharedPreferenceChangeListener){
        sp.registerOnSharedPreferenceChangeListener(listener)
    }

}

