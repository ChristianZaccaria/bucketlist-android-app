package ie.setu.bucketlistandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import timber.log.Timber
import timber.log.Timber.i
import ie.setu.bucketlistandroidapp.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // When the guestButton is pressed, a toast message is displayed and the main
        // bucketlist activity is opened.
        binding.guestButton.setOnClickListener {
            val guestButtonText = getString(R.string.guest_button_text)
            Toast.makeText(applicationContext, guestButtonText, Toast.LENGTH_LONG).show()
            // Logging info shown in Logcat
            i("Continue as Guest button pressed...")
            i("Main Bucketlist Activity started...")
            // Calling function to open the main activity.
            openBucketlistActivity()
        }
        Timber.plant(Timber.DebugTree())
        i("Welcome Activity started...")


    }

    // Function that opens the bucketlist activity when called.
    // Idea inspired from the following YT video: https://www.youtube.com/watch?v=bgIUdb-7Rqo
    private fun openBucketlistActivity() {
        val intent = Intent(this, BucketlistActivity::class.java)
        startActivity(intent)
    }

}