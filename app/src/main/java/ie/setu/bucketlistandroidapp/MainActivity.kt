package ie.setu.bucketlistandroidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ie.setu.bucketlistandroidapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.guestButton.setOnClickListener {
            val guestButtonText = getString(R.string.guest_button_text)
            Toast.makeText(applicationContext, guestButtonText, Toast.LENGTH_LONG).show()
        }
    }

}