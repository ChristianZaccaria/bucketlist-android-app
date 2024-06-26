package ie.setu.bucketlistandroidapp.utils

import android.content.Context
import com.google.gson.Gson
import ie.setu.bucketlistandroidapp.models.ExperienceModel
import timber.log.Timber.i
import java.io.File
import java.io.FileWriter

/* Storing and saving object in JSON (Serialization), and deserialization.
               * References: https://www.youtube.com/watch?v=f-kcvxYZrB4
               * https://www.youtube.com/watch?v=8zPkbV4INGA
               * https://github.com/google/gson
               * https://developer.android.com/reference/kotlin/java/io/FileWriter*/

/*The read and write to/from JSON is designed the following way: when the writeToJSON function
is called, we first check if the file to which we want to write to exists. If it does not
exist, we create the file and start adding experiences to it by converting the objects to strings
and storing them into the JSON file (serialization).*/
    fun writeToJSON(experiences: List<ExperienceModel>, gson: Gson, applicationContext: Context) {
        val filePath = "${applicationContext.filesDir.path}/experiences.json"
        File(filePath).run {
            if (!exists()) createNewFile()
        }
        val json = gson.toJson(experiences)
        try {
            // FileWriter writes to the specified path and the ".use" closes it after being used
            FileWriter(filePath).use { fileWriter -> fileWriter.write(json) }
            i("Successfully saved to JSON file")
            i("Internal storage path: %s", applicationContext.filesDir.path)
        } catch (e: Exception) {
            i("Failed to save to JSON file: ${e.message}")
        }
    }



/*We read/load from JSON and convert its
string contents into objects (deserialization)*/
    fun readFromJSON(gson: Gson, applicationContext: Context): List<ExperienceModel> {
        val filePath = "${applicationContext.filesDir.path}/experiences.json"
        return try {
            // Read file
            val json = File(filePath).readText()
            val experiences = gson.fromJson(json, Array<ExperienceModel>::class.java).toList()
            i("Successfully loaded experiences from JSON file: %s", experiences)
            experiences
        } catch (e: Exception) {
            i("Failed to load experiences from JSON file: ${e.message}")
            emptyList()
        }
    }

