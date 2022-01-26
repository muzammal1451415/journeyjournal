package com.smart.id.journeyjournal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class LoginRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)


    }

    fun onClickLoginButton(view: View) {
        startActivity(Intent(this,Login::class.java))
    }
    fun onClickRegistration(view: View) {
        startActivity(Intent(this,Registration::class.java))
    }
    fun showToast(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}