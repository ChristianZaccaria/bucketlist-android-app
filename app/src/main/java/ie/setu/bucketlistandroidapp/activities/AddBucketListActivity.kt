package ie.setu.bucketlistandroidapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import ie.setu.bucketlistandroidapp.R
import ie.setu.bucketlistandroidapp.databinding.ActivityAddbucketlistBinding
import ie.setu.bucketlistandroidapp.helpers.showImagePicker
import ie.setu.bucketlistandroidapp.main.MainApp
import ie.setu.bucketlistandroidapp.models.ExperienceModel
import ie.setu.bucketlistandroidapp.models.Location
import ie.setu.bucketlistandroidapp.models.User
import timber.log.Timber.i
import java.util.*

class AddBucketListActivity : AppCompatActivity() {

    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityAddbucketlistBinding
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    // class member
    private var signedInUser: User? = null
    private var experience = ExperienceModel()
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    lateinit var app : MainApp



    @SuppressLint("ThrowableNotAtBeginning")
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
//        val gson = Gson()

        app = application as MainApp
        i("Add Activity started...")

        storageReference = FirebaseStorage.getInstance().reference
        firestoreDb = FirebaseFirestore.getInstance()
        firestoreDb.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.email as String)
            .get()
            .addOnSuccessListener { userSnapshot ->
                signedInUser = userSnapshot.toObject(User::class.java)
                i("Signed in user: ${signedInUser?.username}")

                // Here we are only getting the experiences where the experience has been created by the logged-in user
                val experiencesReference = firestoreDb
                    .collection("experiences")
                    .whereEqualTo("user.username", signedInUser?.username)
                experiencesReference.addSnapshotListener { snapshot, exception ->
                    if (exception != null || snapshot == null) {
                        i("Exception when querying posts: %s", exception)
                        return@addSnapshotListener
                    }
                    val listOfExperiences = snapshot.toObjects(ExperienceModel::class.java)
                    app.experiences.updateExperiencesToShow(listOfExperiences)
                    for (experience in listOfExperiences) {
                        i("Experience: $experience")
                    }
                }
            }
            .addOnFailureListener{ exception ->
                i("Failure fetching signed in user: $exception")
            }



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


        registerImagePickerCallback()
        // Event handler for when the user taps on the image
        binding.experienceImage.setOnClickListener {
            i("Select image")
            showImagePicker(imageIntentLauncher)
        }


        registerMapCallback()
        binding.experienceLocationButton.setOnClickListener {
            i("Set Location Pressed")
            val location = Location(52.245696, -7.139102, 15f)
            if (experience.zoom != 0f) {
                location.lat =  experience.lat
                location.lng = experience.lng
                location.zoom = experience.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
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
            binding.experienceLocationButton.text = getString(R.string.update_location_button_text)
            binding.experienceCost.setText(experience.cost.toString())
            Picasso.get()
                    .load(experience.image)
                    .error(R.mipmap.ic_launcher) // Default image if empty or could not be loaded
                    .into(binding.experienceImage)
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
//                app.experiences.delete(ExperienceModel(experience.id, experience.title, experience.category, experience.priority, experience.lat, experience.lng, experience.zoom, experience.cost, experience.image, experience.dueDate, experience.achieved))
                // Calling function to write to JSON file
                //writeToJSON(experience, gson, applicationContext)
//                writeToJSON(app.experiences.findAll(), gson, applicationContext)

                // Reference: for deleting in firebase: https://www.youtube.com/watch?v=0ibZKXTARyU
                // Get a ref to the experience you want to delete
                val experienceRef = firestoreDb.collection("experiences")
                    .whereEqualTo("id", experience.id)
                    .get()

                // Delete the experience from Firestore
                experienceRef.addOnSuccessListener {
                        for (document in it) {
                            // The experience was successfully deleted in firebase
                            firestoreDb.collection("experiences").document(document.id).delete()
                        }
                    }
                    .addOnFailureListener { exception ->
                        // There was an error deleting the experience
                        i("Error deleting experience: $exception")
                        Toast.makeText(this, "Failed to delete experience", Toast.LENGTH_SHORT).show()
                    }


                // Update the list of experiences in the app
                app.experiences.delete(experience)

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
        binding.btnAdd.setOnClickListener { it ->
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

            if (signedInUser == null) {
                Toast.makeText(this, "No signed in user", Toast.LENGTH_SHORT).show()
            }

            if (experience.title.isNotEmpty() && experience.category.isNotEmpty() && experience.priority in 1..5 && experience.cost >= 0.00) {
                // Updating in firebase
                // Reference: https://www.youtube.com/watch?v=aePJ-Zc4ZX8
                if (intent.hasExtra("experience_edit")) {
                    val mapUpdate = mapOf(
                        "title" to experience.title,
                        "category" to experience.category,
                        "priority" to experience.priority,
                        "lat" to experience.lat,
                        "lng" to experience.lng,
                        "zoom" to experience.zoom,
                        "cost" to experience.cost,
                        "image" to experience.image,
                        "dueDate" to experience.dueDate,
                        "achieved" to experience.achieved
                    )
                    // Get a ref to the experience you want to update
                    val experienceRef = firestoreDb.collection("experiences")
                        .whereEqualTo("id", experience.id)
                        .get()

                    // Update the experience from Firestore
                    experienceRef.addOnSuccessListener {
                        for (document in it) {
                            // The experience was successfully updated in firebase
                            firestoreDb.collection("experiences").document(document.id).update(mapUpdate)
                        }
                    }
                        .addOnFailureListener { exception ->
                            // There was an error updating the experience
                            i("Error updating experience: $exception")
                            Toast.makeText(this, "Failed to update experience", Toast.LENGTH_SHORT).show()
                        }
                    // Update the list of experiences in the app
                    app.experiences.update(ExperienceModel(experience.id, experience.title, experience.category, experience.priority, experience.lat, experience.lng, experience.zoom, experience.cost, experience.image, experience.dueDate, experience.achieved, signedInUser))
                } else { // else we create (add)
                    val photoReference =
                        storageReference.child("images/${System.currentTimeMillis()}-photo.jpg")
                    // Upload image to Firebase Storage
                    photoReference.putFile(Uri.parse(experience.image))
                        .continueWithTask { photoUploadTask ->
                            i("Uploaded bytes: ${photoUploadTask.result?.bytesTransferred}")
                            // Retrieve image url of the uploaded image
                            photoReference.downloadUrl
                        }.continueWithTask { downloadUrlTask ->
                            // Updating image path to be pointing at the image stored in Firebase
                            experience.image = downloadUrlTask.result.toString()
                            // Create an experience object with the image URL and add that to the experiences collection
                            val exp = ExperienceModel(
                                experience.id,
                                experience.title,
                                experience.category,
                                experience.priority,
                                experience.lat,
                                experience.lng,
                                experience.zoom,
                                experience.cost,
                                experience.image,
                                experience.dueDate,
                                experience.achieved,
                                signedInUser
                            )
                            firestoreDb.collection("experiences").add(exp)
                        }.addOnCompleteListener { postCreationTask ->
                            if (!postCreationTask.isSuccessful) {
                                i("Exception during Firebase operations: ${postCreationTask.exception}")
                                Toast.makeText(
                                    this,
                                    "Failed to save experience",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    app.experiences.create(experience)
                }
                val successfulAddButton = getString(R.string.button_successfulAdd)
                Toast.makeText(
                    applicationContext,
                    successfulAddButton,
                    Toast.LENGTH_LONG
                )
                    .show()
                setResult(RESULT_OK)
                finish()
            } else {
                // Reference of how I am getting the string from strings.xml
                // https://stackoverflow.com/questions/2183962/how-to-read-value-from-string-xml-in-android
                Snackbar
                    .make(it, getString(R.string.snackbar_fieldsNotCompleted), Snackbar.LENGTH_LONG)
                    .show()
                i("Required fields not yet completed...")
            }


            // Adding or updating experienceModel to experiences ArrayList
//            if (experience.title.isNotEmpty() && experience.category.isNotEmpty() && experience.priority in 1..5 && experience.cost >= 0.00) {
//                if (intent.hasExtra("experience_edit")) {
//                    app.experiences.update(ExperienceModel(experience.id, experience.title, experience.category, experience.priority, experience.lat, experience.lng, experience.zoom, experience.cost, experience.image, experience.dueDate, experience.achieved, signedInUser))
//                } else {
//                app.experiences.create(ExperienceModel(experience.id, experience.title, experience.category, experience.priority, experience.lat, experience.lng, experience.zoom, experience.cost, experience.image, experience.dueDate, experience.achieved, signedInUser))
//                }
//
//                // Calling function to write to JSON file
//                //writeToJSON(experience, gson, applicationContext)
//                writeToJSON(app.experiences.findAll(), gson, applicationContext)
//
//                val successfulAddButton = getString(R.string.button_successfulAdd)
//                Toast.makeText(applicationContext, successfulAddButton, Toast.LENGTH_LONG).show()
//
//                setResult(RESULT_OK)
//                finish()
//            }
//            else {
//                // Reference of how I am getting the string from strings.xml
//                // https://stackoverflow.com/questions/2183962/how-to-read-value-from-string-xml-in-android
//                Snackbar
//                    .make(it, getString(R.string.snackbar_fieldsNotCompleted), Snackbar.LENGTH_LONG)
//                    .show()
//                i("Required fields not yet completed...")
//            }
        }

    }
    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            experience.image = result.data!!.data!!.toString()
                            Picasso.get()
                                    .load(experience.image)
                                    .into(binding.experienceImage)
                            /* Code below needed as the image was not showing after restarting application.
                            * Reference: https://developer.android.com/training/data-storage/shared/documents-files#persist-permissions */
                            val imageUri = Uri.parse(experience.image)
                            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            contentResolver.takePersistableUriPermission(imageUri, takeFlags)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            @Suppress("DEPRECATION")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            experience.lat = location.lat
                            experience.lng = location.lng
                            experience.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}