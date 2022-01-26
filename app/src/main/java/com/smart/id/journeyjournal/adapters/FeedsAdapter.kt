package com.smart.id.journeyjournal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smart.id.journeyjournal.R
import com.smart.id.journeyjournal.data.Feed

internal class FeedsAdapter(private var feedList: List<Feed>, private val listener:FeedAdapterClickListener) :
    RecyclerView.Adapter<FeedsAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(feed:Feed){
            view.findViewById<TextView>(R.id.tv_title).text = feed.feedTitle
            val feedImage = view.findViewById<ImageView>(R.id.iv_feed_image)
            Glide.with(view.context)
                .load(feed.imageUrl)
                .placeholder(R.drawable.add_image)
                .into(feedImage)
        }
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_row, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = feedList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener.onItemClick(position,item)
        }
    }
    override fun getItemCount(): Int {
        return feedList.size
    }
}
interface FeedAdapterClickListener{
    fun onItemClick(position:Int,feed:Feed)
}