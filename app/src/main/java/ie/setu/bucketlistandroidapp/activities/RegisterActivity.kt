package ie.setu.bucketlistandroidapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ie.setu.bucketlistandroidapp.databinding.ActivityRegisterBinding
import timber.log.Timber.i

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // takes an XML file as input and builds the View objects from it
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}