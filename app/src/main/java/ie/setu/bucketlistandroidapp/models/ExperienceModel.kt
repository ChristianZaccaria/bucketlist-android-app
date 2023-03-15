package ie.setu.bucketlistandroidapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

// TODO: image will use URI soon
@Parcelize
data class ExperienceModel(
    var id: Long = 0,
    var title: String = "",
    var category: String = "",
    var priority: Int = 1,
    var location: String = "",
    var cost: Double = 0.00,
    var image: String = "empty",
    var dueDate: Date = Date(0),
    var achieved: Boolean = false) : Parcelable
