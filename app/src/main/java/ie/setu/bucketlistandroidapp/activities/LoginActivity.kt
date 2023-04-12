package ie.setu.bucketlistandroidapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import ie.setu.bucketlistandroidapp.databinding.ActivityLoginBinding
import timber.log.Timber.i

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    @SuppressLint("ThrowableNotAtBeginning")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // takes an XML file as input and builds the View objects from it
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()


        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Email/Password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Firebase authentication check
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
                    openBucketlistActivity()
                } else {
                    i("signInWithEmail failed: %s", task.exception)
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }


        // When the text link is pressed, a toast message is displayed and the register activity is opened.
        binding.btnToRegActivity.setOnClickListener {
            val btnToRegText = "Register"
            Toast.makeText(applicationContext, btnToRegText, Toast.LENGTH_SHORT).show()
            // Logging info shown in Logcat
            i("Continue to register activity...")
            // Calling function to open the login activity.
            openRegisterActivity()
        }

    }

    private fun openBucketlistActivity() {
        val intent = Intent(this, ListBucketListActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun openRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}