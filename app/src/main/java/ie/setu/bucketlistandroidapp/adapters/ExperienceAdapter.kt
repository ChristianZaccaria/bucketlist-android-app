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

interface ExperienceListener {
    fun onExperienceClick(experience: ExperienceModel)
}

class ExperienceAdapter constructor(private var experiences: List<ExperienceModel>, private val listener: ExperienceListener) :
    RecyclerView.Adapter<ExperienceAdapter.MainHolder>() {
    /* filteredExperiences is a copy of the experiences List, used for filtering results from a search.
    The filteredExperiences is an ArrayList rather than a List as we will be manipulating this ArrayList
    for the search. A List can not be modified after it has been created.*/
    private var filteredExperiences = ArrayList(experiences)

    /* onCreateViewHolder prepares the layout of the items by inflating the correct layout for the
    * individual data elements and returns an object of type ViewHolder per visual entry in the RecyclerView*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardExperienceBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }
    // onBindViewHolder assigns the data to the individual widgets.
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val experience = filteredExperiences[holder.adapterPosition]
        holder.bind(experience, listener)
    }

/* filter function inspired from: https://stackoverflow.com/questions/30398247/how-to-filter-a-recyclerview-with-a-searchview
* However, I changed some logic on the function for it to work as it should:
* First we clear the filteredExperiences, then check if the search bar is empty or not. If empty, we add
* all experiences into filteredExperiences and display that, else, we add only the items that match the search and
* add it to the filteredExperiences and display that. This way, the original experiences List is never affected and also serves
* as a starting point or "Reality Check" for the filteredExperiences i.e., when a new item is added or deleted.*/
@SuppressLint("NotifyDataSetChanged")
fun filter(text: String) {
    filteredExperiences.clear()
    if (text.isEmpty()) {
        filteredExperiences.addAll(experiences)
    } else {
        for (item in experiences) {
            if (item.title.lowercase().contains(text)) {
                filteredExperiences.add(item)
            }
        }
    }
    notifyDataSetChanged()
}

    override fun getItemCount(): Int = filteredExperiences.size

    class MainHolder(private val binding : CardExperienceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(experience: ExperienceModel, listener: ExperienceListener) {
            val date = experience.dueDate
            /* Formatting so that we only get the year, month, and day, instead of also hours, minutes, etc.
            We are formatting for displaying purposes (to the card)
            Reference: https://www.baeldung.com/kotlin/string-to-date */
            val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val formattedDate = formatter.format(date)


            binding.experienceTitle.text = experience.title

            if (date == Date(0)) {
                binding.experienceDueDate.text = buildString {
                    append("Due Date: ")
                    append("N/A")
                }
            } else {
                binding.experienceDueDate.text = buildString {
                    append("Due Date: ")
                    append(formattedDate)
                }
            }

            binding.experienceCategory.text = experience.category
            binding.root.setOnClickListener { listener.onExperienceClick(experience) }


            val isAchieved = experience.achieved
            // If experience achieved is TRUE: We display "Achieved" plus a Checkmark that comes from our drawable package.
            if (isAchieved) {
                binding.experienceAchieved.text = buildString { append("Achieved ") }
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
                binding.experienceAchieved.text = buildString { append("Not yet achieved ") }
                // If the item is updated from isAchieved='true' to 'false', then we do not show the checkmark drawable
                binding.experienceAchieved.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
            }
        }
    }
}
