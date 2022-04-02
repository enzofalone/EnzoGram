package com.efalone.enzogram.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.efalone.enzogram.LoginActivity
import com.efalone.enzogram.MainActivity
import com.efalone.enzogram.Post
import com.efalone.enzogram.R
import com.parse.ParseFile
import com.parse.ParseQuery
import com.parse.ParseUser
import java.io.File

class ComposeFragment : Fragment() {
    private val photoFileName = "photo.jpg"
    private var photoFile: File? = null

    lateinit var ivPreview: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivPreview = view.findViewById<ImageView>(R.id.imageView)

        //set onClickListeners and setup logic
        view.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            //send post to server without an image

            //get the description that they have inputted
            val description = view.findViewById<EditText>(R.id.etDescription).text.toString()
            //get current user that is submitting the post
            val user = ParseUser.getCurrentUser()

            if(photoFile != null) {
                submitPost(description, user, photoFile!!)
            } else {
                // Print error log message
                Log.e(MainActivity.TAG, "An error has occurred posting the image")
                // show a toast to the user
                Toast.makeText(requireContext(),"An error has occurred posting the picture", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.btnProfilePic).setOnClickListener {
            //Set picture taken as a profile picture for the user

            //get current user
            val user = ParseUser.getCurrentUser()

            if(photoFile != null) {
                submitProfilePicture(photoFile!!)

            } else {
                // Print error log message
                Log.e(MainActivity.TAG, "An error has occurred setting the image as profile picture")
                // show a toast to the user
                Toast.makeText(requireContext(),"An error has occurred setting the profile picture!", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {

            //log out the user
            ParseUser.logOut()
            val currentUser = ParseUser.getCurrentUser() // this will now be null

            if(currentUser == null) {
                Log.i(MainActivity.TAG,"User successfully logged out")
            } else {
                Log.i(MainActivity.TAG, "error occurred while logging out")
            }
        }

        view.findViewById<Button>(R.id.btnTakePicture).setOnClickListener {
            // Launch camera so user can take a picture
            onLaunchCamera()
        }



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
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MainActivity.TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(MainActivity.TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    // Send a Post object to our Parse server
    fun submitProfilePicture(photoFile: File) {
        // on some click or some loading we need to wait for...
        val pb = view?.findViewById<View>(R.id.pbLoading) as ProgressBar
        pb.visibility = ProgressBar.VISIBLE

        val user = ParseUser.getCurrentUser()//.put("picture", ParseFile(photoFile))
        user.put("picture",ParseFile(photoFile!!))
        user.saveInBackground() { exception ->
            if(exception != null) {
                Log.e(TAG, "Error updating profile picture")
            } else {
                Toast.makeText(requireContext(), "Profile picture successfully updated!", Toast.LENGTH_SHORT).show()
            }
            //make progressbar invisible after processing task
            pb.visibility = ProgressBar.INVISIBLE
        }
    }

    // Send a Post object to our Parse server
    fun submitPost(description: String, user: ParseUser, photoFile: File) {
        // on some click or some loading we need to wait for...
        val pb = view?.findViewById<View>(R.id.pbLoading) as ProgressBar
        pb.visibility = ProgressBar.VISIBLE


        // Create Post object
        val post = Post()
        post.setDescription(description)
        post.setImage(ParseFile(photoFile))
        post.setUser(user)
        post.saveInBackground { exception ->
            if(exception != null) {
                //Something went wrong
                Log.e(MainActivity.TAG, "Error while posting!")
                exception.printStackTrace()
                // show to the user if an error happened
                Toast.makeText(requireContext(),"An error has occurred while submitting!",Toast.LENGTH_SHORT).show()
            } else {
                Log.i(MainActivity.TAG, "Post successfully saved")
                // Show the user the post has been successfully submitted
                Toast.makeText(requireContext(), "Post successfully submitted!", Toast.LENGTH_SHORT).show()

                // Resetting the EditText field to be empty
                view?.findViewById<EditText>(R.id.etDescription)?.setText("")
                // Reset the ImageView to empty
                view?.findViewById<ImageView>(R.id.imageView)?.setImageResource(android.R.color.transparent)
            }
            //make progressbar invisible after processing task
            pb.visibility = ProgressBar.INVISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun goToLogin() {
        //create intent and start
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        //close this activity so user can't go back
        activity?.finish()
    }

    companion object {
        const val TAG = "ComposeFragment"
        private const val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    }
}