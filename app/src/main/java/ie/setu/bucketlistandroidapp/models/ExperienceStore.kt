package ie.setu.bucketlistandroidapp.models

interface ExperienceStore {
    fun findAll(): List<ExperienceModel>
    fun create(experience: ExperienceModel)
    fun update(experience: ExperienceModel)
}