package com.smart.id.journeyjournal

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.smart.id.journeyjournal.data.Feed
import com.smart.id.journeyjournal.data.LocationLatLngs
import com.smart.id.journeyjournal.utils.Utils
import kotlinx.android.synthetic.main.activity_add_feed.*
import java.io.IOException
import java.util.*

class AddFeed : AppCompatActivity() {
    var filePath: Uri? = null
    var PICK_IMAGE_REQUEST = 11

    // instance for firebase storage and StorageReference
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    var database: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_feed)
        storage = FirebaseStorage.getInstance(Utils.storageURL)
        database = FirebaseDatabase.getInstance(Utils.databaseReferenceURL)
        databaseReference = database?.getReference("Feeds")
        storageReference = storage?.getReference()

    }

    fun onClickSave(view: View) {
        val title= editText_title.text.toString()
        val description = edit_text_description.text.toString()
        if(verifyFields(filePath,title,description)){
            createFeed(
                Feed(
                    "",
                    "",
                    title,
                    description,
                    LocationLatLngs(73.3232,74.3232)
                )
            )
        }

    }

     fun verifyFields(fileUri:Uri?, title:String?, description:String?):Boolean{
        if(fileUri == null){
           // showToast("Please select image for Feed")
            Toast.makeText(this@AddFeed,"Please select image for Feed", Toast.LENGTH_SHORT).show()

            return false
        }
        if(title.isNullOrEmpty()){
            showToast("Title cannot be empty")
            return false
        }
        if(description.isNullOrEmpty()){
            showToast("Description cannot be empty")
            return false
        }
        return true
    }

    fun onClickBack(view: View) {
        onBackPressed()
    }
    private fun selectImage() {

        // Defining Implicit Intent to mobile gallery
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."),
            PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode,
            resultCode,
            data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {

            // Get the Uri of data
            filePath = data.data!!
            try {

                // Setting image on image view using Bitmap
                val bitmap = MediaStore.Images.Media
                    .getBitmap(
                        contentResolver,
                        filePath!!)
                iv_feed_image.setImageBitmap(bitmap)
            } catch (e: IOException) {
                // Log the exception
                e.printStackTrace()
            }
        }
    }
    private fun createFeed(feed:Feed){
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading...")
        progressDialog.show()

        // Defining the child of storageReference
        val ref = storageReference
            ?.child(
                "feedsImages/"
                        + UUID.randomUUID().toString()
            )

        // adding listeners on upload
        // or failure of image
        filePath?.let {imageUri ->
            ref?.putFile(imageUri)
                ?.addOnSuccessListener { it ->// Image uploaded successfully
                    // Dismiss dialog
                    ref.downloadUrl.addOnSuccessListener {
                        progressDialog.setMessage("Adding feed...")
                        val std = feed
                        std.imageUrl = it.toString()
                        val uuid = UUID.randomUUID().toString()
                        std.id = uuid
                        databaseReference?.child(uuid)?.setValue(feed)
                            ?.addOnSuccessListener {
                                progressDialog.dismiss()
                                Toast
                                    .makeText(
                                        this@AddFeed,
                                        "Feed created successfully",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                startActivity(Intent(this@AddFeed,Feeds::class.java))

                            }?.addOnFailureListener {
                                progressDialog.dismiss()
                                Toast
                                    .makeText(
                                        this@AddFeed,
                                        "Failed: " + it.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                            }
                    }
                }
                ?.addOnFailureListener { e -> // Error, Image not uploaded
                    progressDialog.dismiss()
                    Toast
                        .makeText(
                            this@AddFeed,
                            "Failed " + e.message,
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
                ?.addOnProgressListener { taskSnapshot ->

                    // Progress Listener for loading
                    // percentage on the dialog box
                    val progress = (100.0
                            * taskSnapshot.bytesTransferred
                            / taskSnapshot.totalByteCount)
                    progressDialog.setMessage(
                        "Uploaded "
                                + progress.toInt() + "%"
                    )
                }
        }

    }

    fun onClickImage(view: View) {
        selectImage()
    }
    fun showToast(message:String){
        Toast.makeText(applicationContext,message, Toast.LENGTH_SHORT).show()
    }
}