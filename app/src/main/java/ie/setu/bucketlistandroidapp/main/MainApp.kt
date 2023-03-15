package ie.setu.bucketlistandroidapp.main

import android.app.Application
import com.google.gson.Gson
import ie.setu.bucketlistandroidapp.models.ExperienceMemStore
import ie.setu.bucketlistandroidapp.models.ExperienceModel
import ie.setu.bucketlistandroidapp.utils.readFromJSON
import timber.log.Timber
import timber.log.Timber.i
import java.util.*

class MainApp : Application() {

    val experiences = ExperienceMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Bucket List App started")
        // Reading from JSON and this way listing all saved experiences in our List Activity
        val experiencesList = readFromJSON(Gson(), applicationContext)
        if (experiencesList.isEmpty()) {
            experiences.create(ExperienceModel(0, "Add an experience \uD83C\uDFD6✈\uD83D\uDCBC❤", "Work", 5, "Anywhere", 0.00, "empty", Date(0), false))
        } else {
            for (experience in experiencesList) {
                experiences.create(experience)
            }
        }
    }
}