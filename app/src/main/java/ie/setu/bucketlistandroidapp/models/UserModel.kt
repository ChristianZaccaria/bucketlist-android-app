package ie.setu.bucketlistandroidapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var username: String = ""
) : Parcelable