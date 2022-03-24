package com.efalone.enzogram

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.parse.ParseUser


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Check if there's a user logged in already
        //If true, begin MainActivity directly
        if(ParseUser.getCurrentUser() != null) {
            goToMainActivity()
        }

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            //collect data in editText fields
            val username = findViewById<EditText>(R.id.etUsername).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()
            //send data to server to check
            loginUser(username, password)
        }

        findViewById<Button>(R.id.btnSignUp).setOnClickListener {
            //collect data in editText fields
            val username = findViewById<EditText>(R.id.etUsername).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()
            //send data to server to create
            signUpUser(username, password)
        }
    }

    private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(username,password) { user, e ->
            if (user != null) {
                //notify user
                Toast.makeText(this,"Logged in successfully!", Toast.LENGTH_SHORT).show()
                //test purposes
                Log.i(TAG, "Successfully logged in user")
                //lead the user to the main segment of the application
                goToMainActivity()
            } else {
                //notify user
                Toast.makeText(this,"Error login in!", Toast.LENGTH_SHORT).show()
                Log.i(TAG,"User Login failure!")
                e.printStackTrace()
            }
        }
    }

    private fun signUpUser(username: String, password: String) {
        // Create the ParseUser
        val user = ParseUser()

        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) {
                // User has successfully signed up

                //Navigate the user to the MainActivity
                goToMainActivity()
                //Show a toast to indicate user successfully signed up for an account
                Toast.makeText(this,"Account created successfully!", Toast.LENGTH_SHORT).show()

            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                e.printStackTrace()
                Toast.makeText(this, "An error has occurred while creating your account!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMainActivity() {
        //create intent and start
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        //close LoginActivity so user can't go back but exit the application
        finish()
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}