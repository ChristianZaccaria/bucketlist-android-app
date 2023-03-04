package ie.setu.bucketlistandroidapp.activities

import android.os.Bundle
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
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

        // numberPicker Reference: https://www.youtube.com/watch?v=zVpH19OlIIU
        // Exception handling because if field is empty, the app crashes.
        val numberPicker: NumberPicker = binding.numberPicker
        numberPicker.minValue = 1
        numberPicker.maxValue = 5
        numberPicker.setOnValueChangedListener { _, _, newPriority ->
            try {
                experience.priority = newPriority
            } catch (e: Exception) {
                experience.priority = 1
            }
        }

        // Switch button for "Achieved" experience status
        // Reference: https://www.youtube.com/watch?v=GAyFASaxAvI
        binding.experienceAchievedSwitch.setOnCheckedChangeListener{ _, onSwitch ->
            experience.achieved = onSwitch
        }



        // Using this to place an error in UI when the title is empty for example.
        // Reference: https://www.youtube.com/watch?v=zRMY6fJeucE&t=232s
        val title: EditText = binding.experienceTitle
        val location: EditText = binding.experienceLocation
        val cost: EditText = binding.experienceCost
        val category: TextInputLayout = binding.experienceCategory



        // Dropdown list for choosing category
        /*References: https://www.youtube.com/watch?v=EBhmRaa8nhE
        * https://www.youtube.com/watch?v=gLNo22h1b8c
        * https://github.com/material-components/material-components-android/issues/2012
        * */
        val items = listOf("Studies", "Holiday", "Work", "Hobby", "Money", "Travel", "Other")
        // adapter that is linking the items and the dropdown list UI.
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        binding.dropdownField.setAdapter(adapter)
        binding.dropdownField.setOnItemClickListener { _, _, position, _ ->
            experience.category = items[position]
            category.error = null
        }

        // If the selected card has the experience_edit flag, we get and set each field in the Add Activity
        if (intent.hasExtra("experience_edit")) {
            @Suppress("DEPRECATION") // Note: If have time, find a "non-deprecated" way of using getParcelable
            experience = intent.extras?.getParcelable("experience_edit")!!
            binding.experienceTitle.setText(experience.title)
            binding.dropdownField.setText(experience.category, false)
            binding.experiencePriority.text = getString(R.string.priority_of_this_experience)
            binding.numberPicker.value = experience.priority
            binding.experienceLocation.setText(experience.location)
            binding.experienceCost.setText(experience.cost.toString())
            val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            if (experience.dueDate == Date(0)) {
                binding.experienceDueDate.text = "N/A"
            } else {
                binding.experienceDueDate.text = formatter.format(experience.dueDate)
            }
            binding.experienceAchievedSwitch.isChecked = experience.achieved
            binding.btnAdd.text = getString(R.string.button_saveExperience)
            // Show the delete button
            binding.btnDelete.visibility = View.VISIBLE
            } else {
                // Hide the delete button
                binding.btnDelete.visibility = View.GONE
            }


        // Delete button with an alert pop-up message
        // Alert message reference: https://stackoverflow.com/questions/23195208/how-to-pop-up-a-dialog-to-confirm-delete-when-user-long-press-on-the-list-item
        binding.btnDelete.setOnClickListener {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("⚠ Wait a second!!")
            alert.setMessage("Are you sure you want to delete this experience?")
            alert.setPositiveButton("YES") { dialog, _ ->
                app.experiences.delete(ExperienceModel(experience.id, experience.title, experience.category, experience.priority, experience.location, experience.cost, experience.dueDate, experience.achieved))
                // Calling function to write to JSON file
                //writeToJSON(experience, gson, applicationContext)
                writeToJSON(app.experiences.findAll(), gson, applicationContext)

                val successfulDeleteButton = getString(R.string.button_successfulDelete)
                Toast.makeText(applicationContext, successfulDeleteButton, Toast.LENGTH_LONG).show()

                setResult(RESULT_OK)
                finish()
                dialog.dismiss()
            }
            alert.setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }
            alert.show()
        }


        // Add button
        binding.btnAdd.setOnClickListener {
            experience.title = binding.experienceTitle.text.toString()
            if (experience.title.isNotEmpty()) {
                i("Title added correctly: ${experience.title}")
            }
            else {
                title.error = "Title can't be empty"
                i("Title field is empty")
            }

            if (experience.category.isNotEmpty()) {
                i("Category added correctly: ${experience.category}")
            }
            else {
                category.error = "Please choose a Category"
                i("Category field is empty")
            }

            if (experience.priority in 1..5) {
                i("Priority added correctly: ${experience.priority}")
            } else {
                i("Priority field is empty or equal to 0")
            }

            experience.location = binding.experienceLocation.text.toString()
            if (experience.location.isNotEmpty()) {
                i("Location added correctly: ${experience.location}")
            }
            else {
                location.error = "Location can't be empty"
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
                cost.error = "Cost can't be a negative number"
                i("Cost field needs to be a positive double")
            }

            if (experience.achieved) {
                i("Achieved field is set to: True")
            }
            else {
                i("Achieved field is set to: False")
            }



            // Adding or updating experienceModel to experiences ArrayList
            if (experience.title.isNotEmpty() && experience.category.isNotEmpty() && experience.priority in 1..5 && experience.location.isNotEmpty() && experience.cost >= 0.00) {
                if (intent.hasExtra("experience_edit")) {
                    app.experiences.update(ExperienceModel(experience.id, experience.title, experience.category, experience.priority, experience.location, experience.cost, experience.dueDate, experience.achieved))
                } else {
                app.experiences.create(ExperienceModel(experience.id, experience.title, experience.category, experience.priority, experience.location, experience.cost, experience.dueDate, experience.achieved))
                }

                // Calling function to write to JSON file
                //writeToJSON(experience, gson, applicationContext)
                writeToJSON(app.experiences.findAll(), gson, applicationContext)

                val successfulAddButton = getString(R.string.button_successfulAdd)
                Toast.makeText(applicationContext, successfulAddButton, Toast.LENGTH_LONG).show()

                setResult(RESULT_OK)
                finish()
            }
            else {
                // Reference of how I am getting the string from strings.xml
                // https://stackoverflow.com/questions/2183962/how-to-read-value-from-string-xml-in-android
                Snackbar
                    .make(it, getString(R.string.snackbar_fieldsNotCompleted), Snackbar.LENGTH_LONG)
                    .show()
                i("Required fields not yet completed...")
            }
        }

    }
}