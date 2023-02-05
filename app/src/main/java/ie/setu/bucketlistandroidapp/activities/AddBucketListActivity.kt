package ie.setu.bucketlistandroidapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ie.setu.bucketlistandroidapp.databinding.ActivityAddbucketlistBinding
import ie.setu.bucketlistandroidapp.models.ExperienceModel
import timber.log.Timber.i
import java.util.*

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
                i("Title added correctly: ${experience.title}")
            }
            else {
                i("Title field is empty")
            }

            experience.category = binding.experienceCategory.text.toString()
            if (experience.category.isNotEmpty()) {
                i("Category added correctly: ${experience.category}")
            }
            else {
                i("Category field is empty")
            }

            // Exception handling because if field is empty, the app crashes.
            try {
                experience.priority = binding.experiencePriority.text.toString().toInt()
            } catch (e: Exception) {
                experience.priority = 0
            }
            if (experience.priority != 0) {
                i("Priority added correctly: ${experience.priority}")
            }
            else {
                i("Priority field is empty or equal to 0")
            }

            experience.location = binding.experienceLocation.text.toString()
            if (experience.location.isNotEmpty()) {
                i("Location added correctly: ${experience.location}")
            }
            else {
                i("Location field is empty")
            }

            experience.image = binding.experienceImage.text.toString()
            if (experience.image.isNotEmpty()) {
                i("Image added correctly: ${experience.image}")
            }
            else {
                i("Image field is empty")
            }

            // Exception handling because if field is empty, the app crashes.
            try {
                experience.cost = binding.experienceCost.text.toString().toDouble()
            } catch (e: Exception) {
                experience.cost = 0.00
            }
            if (experience.cost >= 0.00) {
                i("Cost added correctly: ${experience.cost}")
            }
            else {
                i("Cost field needs to be a positive double")
            }






            experience.achieved = binding.experienceAchieved.text.toString().toBoolean()
            if (experience.achieved) {
                i("Achieved field is set to: True")
            }
            else {
                i("Achieved field is set to: False")
            }





            if (experience.title.isNotEmpty() && experience.category.isNotEmpty() && experience.priority != 0){
                experiences.add(ExperienceModel(experience.title, experience.category, experience.priority, experience.location, experience.image, experience.cost, experience.achieved))
                i("$experiences")
            }
            else {
                Snackbar
                    .make(it,"Required fields are not yet completed", Snackbar.LENGTH_LONG)
                    .show()
                i("Required fields not yet completed...")
            }
        }

    }
}