package com.efalone.enzogram.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.efalone.enzogram.MainActivity
import com.efalone.enzogram.Post
import com.efalone.enzogram.PostAdapter
import com.efalone.enzogram.R
import com.parse.ParseQuery

open class FeedFragment : Fragment() {

    lateinit var postsRecyclerView: RecyclerView
    lateinit var adapter: PostAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    var allPosts: MutableList<Post> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        queryPosts()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postsRecyclerView = view.findViewById<RecyclerView>(R.id.rvPost)
        
        swipeContainer = view.findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {
            queryPosts()
        }

        //create layout for every row in our list
        adapter = PostAdapter(requireContext(), allPosts)
        postsRecyclerView.adapter = adapter

        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        swipeContainer.setColorSchemeResources(R.color.black,
            R.color.white,
            R.color.black,
            R.color.white);


    }

    // Query for all posts in our server
     open fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // Find all Post objects
        query.include(Post.KEY_USER)
        //return posts in descending order (newer to older posts)
        query.addDescendingOrder("createdAt")
        query.setLimit(20)
        query.findInBackground { posts, e ->
            if (e != null) {
                //Something went wrong
                Log.e(TAG, "Error fetching posts")
            } else {
                if (posts != null) {
                    for (post in posts) {
                        Log.i(TAG, "Post: " + post.getDescription())
                    }

                    allPosts.clear()

                    allPosts.addAll(posts)

                    adapter.notifyDataSetChanged()
                    swipeContainer.setRefreshing(false)
                }
            }
        }
    }

    companion object {
        const val TAG = "FeedFragment"
    }
}