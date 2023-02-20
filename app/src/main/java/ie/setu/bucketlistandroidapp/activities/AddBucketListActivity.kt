package ie.setu.bucketlistandroidapp.activities

import android.os.Bundle
import android.app.DatePickerDialog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import ie.setu.bucketlistandroidapp.R
import ie.setu.bucketlistandroidapp.databinding.ActivityAddbucketlistBinding
import ie.setu.bucketlistandroidapp.main.MainApp
import ie.setu.bucketlistandroidapp.models.ExperienceModel
import ie.setu.bucketlistandroidapp.utils.writeToJSON
import timber.log.Timber.i
import java.util.*

class AddBucketListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddbucketlistBinding
    // class member
    private var experience = ExperienceModel()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // takes an XML file as input and builds the View objects from it
        binding = ActivityAddbucketlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // This will enable a back button in our activity's actionbar
        /* References: https://www.youtube.com/watch?v=NXLHVF06AZ4
        https://stackoverflow.com/questions/72634225/onbackpressed-deprecated-what-is-the-alternative
        https://www.geeksforgeeks.org/how-to-create-an-alert-dialog-box-in-android/ */
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Gson instance for JSON
        val gson = Gson()

        app = application as MainApp
        i("Add Activity started...")

        // Calendar for calendar picker
        /* References: https://developer.android.com/reference/android/app/DatePickerDialog
        https://developer.android.com/reference/java/util/Calendar
        https://www.youtube.com/watch?v=LMPmybCTKDA */
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        binding.buttonDatePicker.setOnClickListener {
            val datePicker = DatePickerDialog(this,
                { _, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    /* Setting to TextView. Month starts at index 0 for January, so I gave it
                     a +1 so that 1=January */
                    binding.experienceDueDate.text = resources.getString(R.string.dateFormat, selectedYear, selectedMonth + 1, selectedDay)
                    val calendar = Calendar.getInstance()
                    calendar.set(selectedYear, selectedMonth, selectedDay)
                    val selectedDate = calendar.time
                    experience.dueDate = selectedDate
                },
                year,
                month,
                day
            )
            datePicker.show()
        }

        binding.btnAdd.setOnClickListener {
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



            // Adding experienceModel to experiences ArrayList
            if (experience.title.isNotEmpty() && experience.category.isNotEmpty() && experience.priority != 0){
                app.experiences.add(ExperienceModel(experience.title, experience.category, experience.priority, experience.location, experience.cost, experience.dueDate, experience.achieved))
                // Calling function to write to JSON file
                writeToJSON(experience, gson, applicationContext)

                val successfulAddButton = getString(R.string.button_successfulAdd)
                Toast.makeText(applicationContext, successfulAddButton, Toast.LENGTH_LONG).show()
                // TODO: this for loop is temporary, just for debugging
                for (exp in app.experiences){
                   i(exp.toString())
                }
                for (i in app.experiences.indices)
                { i("Experience[$i]:${this.app.experiences[i]}") }

                setResult(RESULT_OK)
                finish()
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