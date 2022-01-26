package com.smart.id.journeyjournal

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.smart.id.journeyjournal.data.Feed
import com.smart.id.journeyjournal.data.LocationLatLngs
import com.smart.id.journeyjournal.utils.Utils
import kotlinx.android.synthetic.main.activity_add_feed.*
import kotlinx.android.synthetic.main.activity_feed_details.*
import kotlinx.android.synthetic.main.activity_feed_details.editText_title
import kotlinx.android.synthetic.main.activity_feed_details.edit_text_description
import kotlinx.android.synthetic.main.activity_feed_details.iv_feed_image
import java.io.IOException
import java.util.*


class FeedDetails : AppCompatActivity() {

    var filePath: Uri? = null
    var PICK_IMAGE_REQUEST = 11

    var normalLayout:LinearLayout ? = null
    var editLayout:LinearLayout? = null
    var titleTextView:TextView ? = null
    var descriptionTextView:TextView? = null
    var titleEditText:EditText? = null
    var descriptionEditText:EditText? = null
    var saveButton: Button?  = null
    // instance for firebase storage and StorageReference
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    var database: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var progressDialog:ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_details)
        if(intent.hasExtra("feed")){
            val feed = Gson().fromJson(intent.getStringExtra("feed"),Feed::class.java)
            tv_title_top.text = feed.feedTitle
            tv_title.text = feed.feedTitle
            tv_description.text = feed.feedDescription
            Glide.with(this)
                .load(feed.imageUrl)
                .placeholder(R.drawable.add_image)
                .into(iv_feed_image)
            editText_title.setText(feed.feedTitle)
            edit_text_description.setText(feed.feedDescription)
        }
        progressDialog = ProgressDialog(this)
        storage = FirebaseStorage.getInstance(Utils.storageURL)
        storageReference = storage?.getReference()
        database = FirebaseDatabase.getInstance(Utils.databaseReferenceURL)
        databaseReference = database?.getReference("Feeds")

        normalLayout = findViewById(R.id.ll_normal_layout)
        editLayout = findViewById(R.id.ll_edit_layout)
        titleEditText = findViewById(R.id.editText_title)
        descriptionEditText = findViewById(R.id.edit_text_description)
        titleTextView = findViewById(R.id.tv_title)
        descriptionTextView = findViewById(R.id.tv_description)
        saveButton = findViewById(R.id.btn_save)

    }

    fun onClickDelete(view: View) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Warning")
        //set message for alert dialog
        builder.setMessage("Do you really want to delete this feed?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            dialogInterface.dismiss()
            deleteFeed()
        }

        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            dialogInterface.dismiss()

        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    private fun deleteFeed(){
        if(intent.hasExtra("feed")) {
            progressDialog?.setMessage("Deleting feed...")
            progressDialog?.show()
            val feed = Gson().fromJson(intent.getStringExtra("feed"), Feed::class.java)
            databaseReference?.child(feed.id)?.removeValue()?.addOnSuccessListener {
                progressDialog?.dismiss()
                showToast("Feed deleted successfully")
            startActivity(Intent(this,Feeds::class.java))
            }?.addOnFailureListener {
                progressDialog?.dismiss()
                showToast("Failed"+it.message)
            }
        }else{
            showToast("Something went wrong")
        }
    }

    fun onClickEdit(view: View) {
        normalLayout?.visibility = View.GONE
        editLayout?.visibility = View.VISIBLE
        saveButton?.visibility = View.VISIBLE
    }

    fun onClickSave(view: View) {
        normalLayout?.visibility = View.VISIBLE
        editLayout?.visibility = View.GONE
        saveButton?.visibility = View.GONE
        updateFeed()

    }

    fun updateFeed(){
        if(intent.hasExtra("feed")) {
            val feed = Gson().fromJson(intent.getStringExtra("feed"), Feed::class.java)
            val title = editText_title.text.toString()
            val description = edit_text_description.text.toString()
            if (verifyFields(title, description)) {
                if (filePath == null) {
                    simpleUpdate(Feed(feed.id,feed.imageUrl,title,description, LocationLatLngs(73.3232,74.3232)))
                } else {
                    updateWithImageUploading(Feed(feed.id,feed.imageUrl,title,description,LocationLatLngs(73.3232,74.3232)))
                }
            }
        }else{
            showToast("Something went wrong")
        }

    }

    private fun simpleUpdate(feed:Feed){
        progressDialog?.setMessage("Updating feed...")
        databaseReference?.child(feed.id)?.setValue(feed)
                ?.addOnSuccessListener {
                    progressDialog?.dismiss()
                    Toast.makeText(this, "Feed updated successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,Feeds::class.java))
                }?.addOnFailureListener {
                    progressDialog?.dismiss()
                    Toast.makeText(this, "Failed: " + it.message, Toast.LENGTH_SHORT).show()
                }
    }
    private fun updateWithImageUploading(feed:Feed){
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
            filePath?.let { imageUri ->
                ref?.putFile(imageUri)
                        ?.addOnSuccessListener { it ->// Image uploaded successfully
                            // Dismiss dialog
                            ref.downloadUrl.addOnSuccessListener {
                                progressDialog.setMessage("Updating feed...")
                                val std = feed
                                std.imageUrl = it.toString()
                                databaseReference?.child(feed.id)?.setValue(feed)
                                        ?.addOnSuccessListener {
                                            progressDialog.dismiss()
                                            Toast
                                                    .makeText(this,
                                                            "Feed created successfully",
                                                            Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                            startActivity(Intent(this, Feeds::class.java))

                                        }?.addOnFailureListener {
                                            progressDialog.dismiss()
                                            Toast
                                                    .makeText(
                                                            this,
                                                            "Failed: " + it.message,
                                                            Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                        }
                            }


                        }
                        ?.addOnFailureListener { e -> // Error, Image not uploaded
                            progressDialog.dismiss()
                            Toast
                                    .makeText(
                                            this,
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

     fun verifyFields(title:String?, description:String?):Boolean{
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

    fun onClickBack(view: View) {
        onBackPressed()
    }
    fun showToast(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

    fun onClickImage(view: View) {
        selectImage()
    }


}


