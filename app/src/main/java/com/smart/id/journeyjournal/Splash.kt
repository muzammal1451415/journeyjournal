package com.smart.id.journeyjournal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.smart.id.journeyjournal.utils.JourneyJournalSharedPreferences
import com.smart.id.journeyjournal.utils.Utils

class Splash : AppCompatActivity() {
    private var sharedPreferences:JourneyJournalSharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreferences = JourneyJournalSharedPreferences(this,Utils.login)
        Handler().postDelayed({
           if(sharedPreferences?.getBooleanValue(Utils.isLoggedIn) == true) {
               startActivity(Intent(this,Feeds::class.java))
               finish()
               finishAffinity()
           }else{
               startActivity(Intent(this,LoginRegister::class.java))
               finish()
               finishAffinity()
           }
        },2000L)
    }

}