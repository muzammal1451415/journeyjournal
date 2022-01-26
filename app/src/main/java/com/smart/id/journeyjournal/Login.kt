package com.smart.id.journeyjournal

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.smart.id.journeyjournal.utils.JourneyJournalSharedPreferences
import com.smart.id.journeyjournal.utils.Utils
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    private var mAuth:FirebaseAuth? = null
    private var progressDialog:ProgressDialog? = null
    private var sharedpreferences:JourneyJournalSharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("Please wait...")
        mAuth = FirebaseAuth.getInstance()
        sharedpreferences = JourneyJournalSharedPreferences(this,Utils.login)


    }

    fun onClickLoginButton(view: View) {
        val email = et_email.text.toString()
        val password = et_password.text.toString()
        if(verifyFields(email,password))
        loginUser(email,password)
    }
   fun verifyFields(email:String?, password:String?):Boolean{
        if(email.isNullOrEmpty()){
            showToast("email cannot be empty")
            return false
        }
        if(password.isNullOrEmpty()){
            showToast("Password cannot be empty")
            return false
        }
        return true
    }

    private fun loginUser(email:String, password:String){
        progressDialog?.show()
        mAuth?.signInWithEmailAndPassword(email,password)?.addOnCompleteListener {
            progressDialog?.dismiss()
            if(it.isSuccessful){
                sharedpreferences?.saveBooleanValue(Utils.isLoggedIn,true)
                startActivity(Intent(this,Feeds::class.java))
            }
            else{
                showToast("Error: "+it.exception?.message?:"Some thing went wrong")
            }
        }?.addOnFailureListener {
            progressDialog?.dismiss()
            showToast("Error: "+it.message)
        }
    }

    fun showToast(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}