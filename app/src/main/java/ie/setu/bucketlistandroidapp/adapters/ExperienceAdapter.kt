package ie.setu.bucketlistandroidapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.bucketlistandroidapp.databinding.CardBucketlistBinding
import ie.setu.bucketlistandroidapp.models.ExperienceModel

/*Some notes: ViewHolder holds references to the relevant views. With these references, the code
* can avoid using findViewById() method to update the widgets with new data.*/

class ExperienceAdapter constructor(private var experiences: List<ExperienceModel>) :
    RecyclerView.Adapter<ExperienceAdapter.MainHolder>() {

    /* onCreateViewHolder prepares the layout of the items by inflating the correct layout for the
    * individual data elements and returns an object of type ViewHolder per visual entry in the RecyclerView*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardBucketlistBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }
    // onBindViewHolder assigns the data to the individual widgets.
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val experience = experiences[holder.adapterPosition]
        holder.bind(experience)
    }

    override fun getItemCount(): Int = experiences.size

    class MainHolder(private val binding : CardBucketlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(experience: ExperienceModel) {
            binding.experienceTitle.text = experience.title
            binding.experienceCategory.text = experience.category
        }
    }
}
