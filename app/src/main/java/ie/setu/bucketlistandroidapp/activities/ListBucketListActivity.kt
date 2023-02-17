package ie.setu.bucketlistandroidapp.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.bucketlistandroidapp.R
import ie.setu.bucketlistandroidapp.adapters.ExperienceAdapter
import ie.setu.bucketlistandroidapp.databinding.ActivityListBucketListBinding
import ie.setu.bucketlistandroidapp.main.MainApp
import timber.log.Timber.i

class ListBucketListActivity : AppCompatActivity() {

    // lateinit used to overrule null safety checks
    lateinit var app: MainApp
    private lateinit var binding: ActivityListBucketListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBucketListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        // layoutManager used for positioning items
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        // Adapter used to bind data to Views such as the RecyclerView
        binding.recyclerView.adapter = ExperienceAdapter(app.experiences)

        binding.fabBtn.setOnClickListener {
            val addButtonText = R.string.button_openAddActivity
            Toast.makeText(applicationContext, addButtonText, Toast.LENGTH_SHORT).show()
            // Logging info shown in Logcat
            i("Add button pressed...")
            // Calling function to open the main activity.
            val intent = Intent(this, AddBucketListActivity::class.java)
            // Launches the activity and awaits for the activity to complete (Get an 'OK' result), then updates the UI
            getResult.launch(intent)
        }
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.experiences.size)
            }
        }
}
