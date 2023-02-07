package ie.setu.bucketlistandroidapp.models

import java.util.*

// TODO: image will use URI soon
data class ExperienceModel(
    var title: String = "",
    var category: String = "",
    var priority: Int = 0,
    var location: String = "",
    var image: String = "",
    var cost: Double = 0.00,
    var dueDate: Date = Date(0),
    var achieved: Boolean = false)
