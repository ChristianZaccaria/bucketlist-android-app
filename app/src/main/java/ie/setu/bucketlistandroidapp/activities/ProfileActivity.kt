package ie.setu.bucketlistandroidapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import ie.setu.bucketlistandroidapp.databinding.ActivityProfileBinding
import timber.log.Timber.i

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val signedInUserEmail = FirebaseAuth.getInstance().currentUser?.email
        binding.signedInEmailText.text = signedInUserEmail

        binding.btnViewAllMarkers.setOnClickListener {
            val btnViewAllMarkersText = "Viewing all markers in map..."
            Toast.makeText(applicationContext, btnViewAllMarkersText, Toast.LENGTH_SHORT).show()
            // Logging info shown in Logcat
            i("View All Markers button pressed...")
            // Calling function to open the ExperiencesMaps Activity.
            val launcherIntent = Intent(this, ExperiencesMapsActivity::class.java)
            mapIntentLauncher.launch(launcherIntent)
        }

        binding.logoutButton.setOnClickListener {
            val logoutButtonText = "Logging out..."
            Toast.makeText(applicationContext, logoutButtonText, Toast.LENGTH_SHORT).show()
            // Logging info shown in Logcat
            i("Logout button pressed...")
            // Signing out
            FirebaseAuth.getInstance().signOut()
            hasLoggedIn = false
            // Calling function to open the welcome activity.
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private val mapIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )    { }
}