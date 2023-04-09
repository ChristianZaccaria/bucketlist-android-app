package ie.setu.bucketlistandroidapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import ie.setu.bucketlistandroidapp.R
import timber.log.Timber.i
import ie.setu.bucketlistandroidapp.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // takes an XML file as input and builds the View objects from it
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* This is used to check if the user has already logged in, this way we have an open session and
        the user no longer needs to login each time they open the app, unless they logout. */
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            openBucketlistActivity()
        }

        binding.toLoginButton.setOnClickListener {
            val toLoginButtonText = "Login"
            Toast.makeText(applicationContext, toLoginButtonText, Toast.LENGTH_LONG).show()
            i("Continue to login screen...")
            openLoginActivity()
        }

        // When the guestButton is pressed, a toast message is displayed and the main
        // bucketlist activity is opened.
        binding.guestButton.setOnClickListener {
            val guestButtonText = getString(R.string.guest_button_text)
            Toast.makeText(applicationContext, guestButtonText, Toast.LENGTH_LONG).show()
            // Logging info shown in Logcat
            i("Continue as Guest button pressed...")
            // Calling function to open the main activity.
            openBucketlistActivity()
        }
        i("Welcome Activity started...")


    }

    // Function that opens the bucketlist activity when called.
    // Idea inspired from the following YT video: https://www.youtube.com/watch?v=bgIUdb-7Rqo
    private fun openBucketlistActivity() {
        val intent = Intent(this, ListBucketListActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}