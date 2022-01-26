package com.smart.id.journeyjournal

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_email
import kotlinx.android.synthetic.main.activity_login.et_password
import kotlinx.android.synthetic.main.activity_registration.*

class Registration : AppCompatActivity() {
    private var mAuth:FirebaseAuth ? = null
    private var progressDialog: ProgressDialog ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("Please wait...")
        mAuth = FirebaseAuth.getInstance()



    }
    fun verifyFields(email:String?, password:String?,confirmPassword:String?):Boolean{
        if(email.isNullOrEmpty()){
            showToast("email cannot be empty")
            return false
        }
        if(password.isNullOrEmpty()){
            showToast("Password cannot be empty")
            return false
        }
        if(confirmPassword.isNullOrEmpty()){
            showToast("Confirm password cannot be empty")
            return false
        }
        if(!password.equals(confirmPassword)){
            showToast("Password and confirm password are mismatched")
            return false
        }
        return true
    }

    private fun createNewUser(email:String, password:String){
        progressDialog?.show()
        mAuth?.createUserWithEmailAndPassword(email,password)?.addOnSuccessListener {
            progressDialog?.dismiss()
            showToast("User Registered successfully")
            startActivity(Intent(this,Login::class.java))
            finishAffinity()
            finish()

        }?.addOnFailureListener {
            progressDialog?.dismiss()
            showToast("Error: ${it.message}")
        }
    }

    fun onClickRegister(view: View) {
        val email = et_email.text.toString()
        val password = et_password.text.toString()
        val confirmPassword = et_password_confirm.text.toString()

        if(verifyFields(email,password,confirmPassword)){
            createNewUser(email,password)
        }
    }

    fun showToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}