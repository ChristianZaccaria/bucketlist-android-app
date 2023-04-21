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

        // This will enable a back button in our activity's actionbar
        /* References: https://www.youtube.com/watch?v=NXLHVF06AZ4
        https://stackoverflow.com/questions/72634225/onbackpressed-deprecated-what-is-the-alternative
        https://www.geeksforgeeks.org/how-to-create-an-alert-dialog-box-in-android/ */
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

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
            /* The below helps to clear all activities and tasks and basically start again from welcome screen.
            Else, when logging out, and pressing the back button, the list activity comes up when it shouldn't
            Reference: https://stackoverflow.com/questions/5979171/clear-all-activities-in-a-task */
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
    private val mapIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )    { }
}