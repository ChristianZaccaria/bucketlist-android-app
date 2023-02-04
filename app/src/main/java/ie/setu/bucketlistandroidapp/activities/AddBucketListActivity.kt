package ie.setu.bucketlistandroidapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import ie.setu.bucketlistandroidapp.databinding.ActivityAddbucketlistBinding
import ie.setu.bucketlistandroidapp.models.ExperienceModel
import timber.log.Timber.i

class BucketlistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddbucketlistBinding
    // class member
    private var experience = ExperienceModel()
    private val experiences = ArrayList<ExperienceModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // takes an XML file as input and builds the View objects from it
        binding = ActivityAddbucketlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        i("Add Activity started...")

        binding.btnAdd.setOnClickListener() {
            experience.title = binding.experienceTitle.text.toString()
            if (experience.title.isNotEmpty()) {
                experiences.add(experience)
                i("Title added correctly: ${experience.title}")
                i("$experiences")
            }
            else {
                Snackbar
                    .make(it,"Please enter a Title", Snackbar.LENGTH_LONG)
                    .show()
                i("Title field is empty")
            }
        }

    }
}