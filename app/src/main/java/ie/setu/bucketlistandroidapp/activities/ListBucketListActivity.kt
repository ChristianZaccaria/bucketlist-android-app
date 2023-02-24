package ie.setu.bucketlistandroidapp.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.bucketlistandroidapp.R
import ie.setu.bucketlistandroidapp.adapters.ExperienceAdapter
import ie.setu.bucketlistandroidapp.adapters.ExperienceListener
import ie.setu.bucketlistandroidapp.databinding.ActivityListBucketListBinding
import ie.setu.bucketlistandroidapp.main.MainApp
import ie.setu.bucketlistandroidapp.models.ExperienceModel
import timber.log.Timber.i

class ListBucketListActivity : AppCompatActivity(), ExperienceListener {

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
        binding.recyclerView.adapter = ExperienceAdapter(app.experiences.findAll(), this)

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

    override fun onExperienceClick(experience: ExperienceModel) {
        val launcherIntent = Intent(this, AddBucketListActivity::class.java)
        launcherIntent.putExtra("experience_edit", experience)
        getResult.launch(launcherIntent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.experiences.findAll().size)
            } else if (it.resultCode == Activity.RESULT_CANCELED) { // Making use of RESULT_CANCELED as a flag to notify the adapter that the item has been removed
                (binding.recyclerView.adapter)?.notifyDataSetChanged()
            }
        }
}
