package ie.setu.bucketlistandroidapp.models

// TODO: image will use URI soon
data class ExperienceModel(
    var title: String = "",
    var category: String = "",
    var priority: Int = 0,
    var location: String = "",
    var image: String = "",
    var cost: Double = 0.00,
//    var dueDate: Date = Date(0), TODO: more investigation needed on calendar picker
    var achieved: Boolean = false)
