package ie.setu.bucketlistandroidapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ie.setu.bucketlistandroidapp.databinding.ActivityRegisterBinding
import timber.log.Timber.i

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // takes an XML file as input and builds the View objects from it
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (email.isBlank() || password.isBlank() || username.isBlank()) {
                Toast.makeText(this, "Email/Password/Username cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                registerUser(email, password, username)
            }
        }

        // When the text link is pressed, a toast message is displayed and the login activity is opened.
        binding.btnToLoginActivity.setOnClickListener {
            val btnToLoginText = "Login"
            Toast.makeText(applicationContext, btnToLoginText, Toast.LENGTH_SHORT).show()
            // Logging info shown in Logcat
            i("Continue to login activity...")
            // Calling function to open the login activity.
            openLoginActivity()
        }

    }


    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Register user to firebase function
    // References: https://www.youtube.com/watch?v=IDsR0lkKPtI
    private fun registerUser(email: String, password: String, username: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val randomCode = (100000..999999).random() // Used in case the username already exists in our db
                    val db = Firebase.firestore
                    db.collection("users").document(email)
                        .set(mapOf("username" to "$username-$randomCode"))

                    val intent = Intent(this, ListBucketListActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Invalid email / email already exists",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }
}