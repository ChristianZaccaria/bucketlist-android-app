package ie.setu.bucketlistandroidapp.models

import android.net.Uri
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
    var image: Uri = Uri.EMPTY,
    var dueDate: Date = Date(0),
    var achieved: Boolean = false) : Parcelable
