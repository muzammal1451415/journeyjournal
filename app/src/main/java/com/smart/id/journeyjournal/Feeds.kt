package com.smart.id.journeyjournal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.gson.Gson
import com.smart.id.journeyjournal.adapters.FeedAdapterClickListener
import com.smart.id.journeyjournal.adapters.FeedsAdapter
import com.smart.id.journeyjournal.data.Feed
import com.smart.id.journeyjournal.data.LocationLatLngs
import com.smart.id.journeyjournal.utils.Utils

class Feeds : AppCompatActivity(),FeedAdapterClickListener {
    var database: FirebaseDatabase? = null
    var recyclerView: RecyclerView? = null
    var databaseReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feeds)
        database = FirebaseDatabase.getInstance(Utils.databaseReferenceURL)
        databaseReference = database?.getReference("Feeds")
        recyclerView = findViewById(R.id.rv_feeds)
        getFeeds()
    }
    fun onClickAddButton(view: View){
        startActivity(Intent(this,AddFeed::class.java))
    }
    fun onClickFeed(view:View){
        startActivity(Intent(this,FeedDetails::class.java))
    }

    override fun onItemClick(position: Int,feed:Feed) {
        var jsonFeed = Gson().toJson(feed)
        Intent(this,FeedDetails::class.java).also{
            it.putExtra("feed",jsonFeed)
            startActivity(it)
        }
    }

    private fun getFeeds(){
        databaseReference?.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Feed>()
                snapshot.children.forEach {
                    var std = it.getValue(Feed::class.java)
                    std?.let { feed ->
                        list.add(feed)
                    }
                }
                val adapter = FeedsAdapter(list,this@Feeds)
                val layoutManager = LinearLayoutManager(applicationContext)
                recyclerView?.layoutManager = layoutManager
                recyclerView?.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}
