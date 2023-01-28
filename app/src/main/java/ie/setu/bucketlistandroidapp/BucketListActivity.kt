package ie.setu.bucketlistandroidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import timber.log.Timber
import timber.log.Timber.i
import ie.setu.bucketlistandroidapp.databinding.ActivityBucketlistBinding

class BucketListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBucketlistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBucketlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.guestButton.setOnClickListener {
            val guestButtonText = getString(R.string.guest_button_text)
            Toast.makeText(applicationContext, guestButtonText, Toast.LENGTH_LONG).show()
        }
        Timber.plant(Timber.DebugTree())
        i("Bucketlist Activity started..")
    }

}