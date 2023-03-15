package ie.setu.bucketlistandroidapp.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class ExperienceMemStore : ExperienceStore {

    var experiences = ArrayList<ExperienceModel>()

    override fun findAll(): List<ExperienceModel> {
        return experiences
    }

    override fun create(experience: ExperienceModel) {
        experience.id = getId()
        experiences.add(experience)
        logAll()
    }

    override fun update(experience: ExperienceModel) {
        val foundExperience: ExperienceModel? = experiences.find { e -> e.id == experience.id }
        if (foundExperience != null) {
            foundExperience.title = experience.title
            foundExperience.category = experience.category
            foundExperience.priority = experience.priority
            foundExperience.cost = experience.cost
            foundExperience.image = experience.image
            foundExperience.dueDate = experience.dueDate
            foundExperience.achieved = experience.achieved
            logAll()
        }
    }

    override fun delete(experience: ExperienceModel) {
            experiences.remove(experience)
            logAll()
    }

    private fun logAll() {
        experiences.forEach{ i("$it") }
    }


}