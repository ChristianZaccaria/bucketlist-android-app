package ie.setu.bucketlistandroidapp.main

import android.app.Application
import com.google.gson.Gson
import ie.setu.bucketlistandroidapp.models.ExperienceModel
import ie.setu.bucketlistandroidapp.utils.readFromJSON
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val experiences = ArrayList<ExperienceModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Bucket List App started")
        // Reading from JSON and this way listing all saved experiences in our List Activity
        experiences.addAll(readFromJSON(Gson(),applicationContext))
    }
}