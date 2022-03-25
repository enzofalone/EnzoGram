package com.efalone.enzogram

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.parse.ParseFile
import com.parse.ParseQuery
import com.parse.ParseUser
import java.io.File


/**
 * Let user create a post by taking a photo with their camera
 */

class MainActivity : AppCompatActivity() {
    private val photoFileName = "photo.jpg"
    private var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // hide action bar
        supportActionBar?.hide()

        findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            //send post to server without an image

            //get the description that they have inputted
            val description = findViewById<EditText>(R.id.etDescription).text.toString()
            //get current user that is submitting the post
            val user = ParseUser.getCurrentUser()

            if(photoFile != null) {
                submitPost(description, user, photoFile!!)
            } else {
                // Print error log message
                Log.e(TAG, "An error has occurred posting the image")
                // show a toast to the user
                Toast.makeText(this,"An error has occurred posting the picture", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnLogout).setOnClickListener {

            //log out the user
            ParseUser.logOut()
            val currentUser = ParseUser.getCurrentUser() // this will now be null

            if(currentUser == null) {
                Log.i(TAG,"User successfully logged out")
                goToLogin()
            } else {
                Log.i(TAG, "error occurred while logging out")
            }
        }

        findViewById<Button>(R.id.btnTakePicture).setOnClickListener {
            // Launch camera so user can take a picture
            onLaunchCamera()
        }

//        queryPosts()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                val ivPreview: ImageView = findViewById(R.id.imageView)
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun goToLogin() {
        //create intent and start
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        //close this activity so user can't go back
        finish()
    }

    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, Companion.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    // Send a Post object to our Parse server
    fun submitPost(description: String, user: ParseUser, photoFile: File) {
        // on some click or some loading we need to wait for...
        val pb = findViewById<View>(R.id.pbLoading) as ProgressBar
        pb.visibility = ProgressBar.VISIBLE


        // Create Post object
        val post = Post()
        post.setDescription(description)
        post.setImage(ParseFile(photoFile))
        post.setUser(user)
        post.saveInBackground { exception ->
            if(exception != null) {
                //Something went wrong
                Log.e(TAG, "Error while posting!")
                exception.printStackTrace()
                // show to the user if an error happened
                Toast.makeText(this,"An error has occurred while submitting!",Toast.LENGTH_SHORT).show()
            } else {
                Log.i(TAG, "Post successfully saved")
                // Show the user the post has been successfully submitted
                Toast.makeText(this, "Post successfully submitted!", Toast.LENGTH_SHORT).show()

                // Resetting the EditText field to be empty
                findViewById<EditText>(R.id.etDescription).setText("")
                // Reset the ImageView to empty
                findViewById<ImageView>(R.id.imageView).setImageResource(android.R.color.transparent)
            }
            //make progressbar invisible after processing task
            pb.visibility = ProgressBar.INVISIBLE
        }
    }

    // Query for all posts in our server
    fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // Find all Post objects
        query.include(Post.KEY_USER)

        query.findInBackground { posts, e ->
            if (e != null) {
                //Something went wrong
                Log.e(TAG, "Error fetching posts")
            } else {
                if (posts != null) {
                    for (post in posts) {
                        Log.i(TAG, "Post: " + post.getDescription())
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
        private const val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    }
}