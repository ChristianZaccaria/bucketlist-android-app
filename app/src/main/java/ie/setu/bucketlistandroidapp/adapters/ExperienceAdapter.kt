package ie.setu.bucketlistandroidapp.adapters

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.bucketlistandroidapp.R
import androidx.core.content.ContextCompat
import ie.setu.bucketlistandroidapp.databinding.CardExperienceBinding
import ie.setu.bucketlistandroidapp.models.ExperienceModel
import java.util.*

/*Some notes: ViewHolder holds references to the relevant views. With these references, the code
* can avoid using findViewById() method to update the widgets with new data.*/

class ExperienceAdapter constructor(private var experiences: List<ExperienceModel>) :
    RecyclerView.Adapter<ExperienceAdapter.MainHolder>() {

    /* onCreateViewHolder prepares the layout of the items by inflating the correct layout for the
    * individual data elements and returns an object of type ViewHolder per visual entry in the RecyclerView*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardExperienceBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }
    // onBindViewHolder assigns the data to the individual widgets.
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val experience = experiences[holder.adapterPosition]
        holder.bind(experience)
    }

    override fun getItemCount(): Int = experiences.size

    class MainHolder(private val binding : CardExperienceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n") /* Much easier to suppress the text 'warnings' than using string formatting (already tried).
        This can be re-evaluated later if have time.*/
        fun bind(experience: ExperienceModel) {
            val date = experience.dueDate
            /* Formatting so that we only get the year, month, and day, instead of also hours, minutes, etc.
            We are formatting for displaying purposes (to the card)
            Reference: https://www.baeldung.com/kotlin/string-to-date */
            val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val formattedDate = formatter.format(date)


            binding.experienceTitle.text = experience.title
            binding.experienceDueDate.text = "Due Date: $formattedDate"
            binding.experienceCategory.text = experience.category


            val isAchieved = experience.achieved
            // If experience achieved is TRUE: We display "Achieved" plus a Checkmark that comes from our drawable package.
            if (isAchieved) {
                binding.experienceAchieved.text = "Achieved "
                // Here we are getting the checkmark drawable
                val checkMarkDrawable = ContextCompat.getDrawable(binding.root.context, R.drawable.baseline_check_mark)
                /* Making the drawable be on the right side of the text
                References: https://developer.android.com/reference/android/widget/TextView#setCompoundDrawablesWithIntrinsicBounds(android.graphics.drawable.Drawable,%20android.graphics.drawable.Drawable,%20android.graphics.drawable.Drawable,%20android.graphics.drawable.Drawable) */
                binding.experienceAchieved.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    checkMarkDrawable,
                    null
                )
            } else {
                binding.experienceAchieved.text = "Not yet achieved "
            }
        }
    }
}
