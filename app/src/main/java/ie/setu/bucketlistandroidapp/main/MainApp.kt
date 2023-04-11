package ie.setu.bucketlistandroidapp.main

import android.app.Application
import ie.setu.bucketlistandroidapp.models.ExperienceMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val experiences = ExperienceMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Bucket List App started")
        // Reading from JSON and this way listing all saved experiences in our List Activity
//        val experiencesList = readFromJSON(Gson(), applicationContext)
//        if (experiencesList.isEmpty()) {
//            experiences.create(ExperienceModel(0, "Add an experience \uD83C\uDFD6✈\uD83D\uDCBC❤", "Work", 5, 52.245696, -7.139102, 15f, 0.00, "empty", Date(0), false))
//        } else {
//            for (experience in experiencesList) {
//                experiences.create(experience)
//            }
//        }
    }
}