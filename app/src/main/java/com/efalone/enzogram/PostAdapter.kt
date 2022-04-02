package com.efalone.enzogram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.TimeFormatter
import com.parse.ParseFile
import com.parse.ParseUser

class PostAdapter(val context: Context, val posts: List<Post>)
    : RecyclerView.Adapter<PostAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        // specify layout file to use for this item
        val view = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView
        val tvDescription: TextView
        val tvLikes: TextView
        val tvTime: TextView
        val ivProfile: ImageView
        val ivPicture: ImageView
        val btnLike: ImageView

        init {
            tvUsername = itemView.findViewById(R.id.tvUsername)
            tvDescription = itemView.findViewById(R.id.tvDescription)
            tvLikes = itemView.findViewById(R.id.tvLikes)
            tvTime = itemView.findViewById(R.id.tvTime)

            ivPicture = itemView.findViewById(R.id.ivPicture)
            ivProfile = itemView.findViewById(R.id.ivProfile)

            btnLike = itemView.findViewById(R.id.ivLike)
        }

        fun bind(post: Post) {
            tvDescription.text = post.getDescription()
            tvUsername.text = post.getUser()?.username
            tvTime.text = TimeFormatter.getTimeDifference(post.createdAt.toString())

            Glide.with(itemView.context).load(post.getImage()?.url)
                .error("Picture not found!")
                .into(ivPicture)


            Glide.with(itemView.context)
                .load(post.getUserProfileImage()?.url)
                .into(ivProfile)
        }
    }
}