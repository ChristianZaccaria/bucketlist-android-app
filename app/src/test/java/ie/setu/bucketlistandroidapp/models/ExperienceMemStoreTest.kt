package ie.setu.bucketlistandroidapp.models

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList


class ExperienceMemStoreTest {
    private var app: ExperienceMemStore? = null
    private var learnPiano: ExperienceModel? = null
    private var summerHoliday: ExperienceModel? = null
    private var rockClimb: ExperienceModel? = null
    private var graduate: ExperienceModel? = null
    private var concert: ExperienceModel? = null

    @Before
    fun setUp() {
        app = ExperienceMemStore()
        learnPiano = ExperienceModel(0, "Learn Piano", "Hobby", 5, "Sweden", 300.00, Date(0), false)
        summerHoliday = ExperienceModel(1, "Summer Holiday to Spain", "Travel", 1, "Spain", 500.00, Date(0), false)
        rockClimb = ExperienceModel(2, "Going Rock Climbing", "Hobby", 4, "Mount Everest", 900.00, Date(0), false)
        graduate = ExperienceModel(3, "Graduate from WIT", "Career", 4, "Waterford", 9000.00, Date(0), true)
        concert = ExperienceModel(4, "Ed Sheeran concert", "Concert", 3, "Dublin", 200.00, Date(0), false)

        // Each test starts off with 5 experiences
        app!!.create(learnPiano!!)
        app!!.create(summerHoliday!!)
        app!!.create(rockClimb!!)
        app!!.create(graduate!!)
        app!!.create(concert!!)
    }

    @After
    fun tearDown() {
        learnPiano = null
        summerHoliday = null
        rockClimb = null
        graduate = null
        concert = null
        app!!.experiences = ArrayList()
    }

    // Tests for creating an experience
    @Test
    fun addingAnExperienceToArrayListAddsToArrayList() {
        val newExperience = ExperienceModel(5, "Play Guitar", "Hobby", 3, "Home", 25.00, Date(0), true)
        Assert.assertEquals(5, app!!.findAll().size)
        app!!.create(newExperience)
        Assert.assertEquals(6, app!!.findAll().size)
        Assert.assertTrue(app!!.findAll().contains(newExperience))
    }

    /* Tests for listing experiences: checks if the experiences that we have stored are actually
     there by searching for their title name*/
    @Test
    fun `findAll() returns all experiences in ArrayList`() {
        Assert.assertEquals(5, app!!.findAll().size)
        val expected = listOf(learnPiano, summerHoliday, rockClimb, graduate, concert)
        Assert.assertEquals(expected, app!!.findAll())
        val experiences = app!!.findAll()
        Assert.assertTrue(experiences.any { it.title == "Learn Piano" })
        Assert.assertTrue(experiences.any { it.title == "Summer Holiday to Spain" })
        Assert.assertTrue(experiences.any { it.title == "Going Rock Climbing" })
        Assert.assertTrue(experiences.any { it.title == "Graduate from WIT" })
        Assert.assertTrue(experiences.any { it.title == "Ed Sheeran concert" })
    }

    // Tests for deleting experiences
    /* Creating two experiences in an Arraylist that already has 5 experiences. Then deleting the two that were
    created, one by one.*/
    @Test
    fun `deleting experiences`() {
        Assert.assertEquals(5, app!!.findAll().size)
        val experience = ExperienceModel(5, "Learn Piano", "Hobby", 5, "Sweden", 300.00, Date(0), false)
        val experience2 = ExperienceModel(6, "Play Guitar", "Hobby", 3, "Home", 25.00, Date(0), true)
        app!!.create(experience)
        app!!.create(experience2)
        app!!.delete(experience)
        val experiences = app!!.findAll()
        Assert.assertEquals(6, experiences.size)
        app!!.delete(experience2)
        Assert.assertEquals(5, experiences.size)
    }

    /* Updating the experience created, shows that the title got updated by looking for the title names
    after the update has been done*/
    @Test
    fun `test update`() {
        Assert.assertEquals(5, app!!.findAll().size)
        val experience = ExperienceModel(5, "Play Soccer", "Hobby", 5, "Sweden", 300.00, Date(0), false)
        app!!.create(experience)
        experience.title = "Play Basketball"
        app!!.update(experience)
        val experiences = app!!.findAll()
        Assert.assertEquals(6, experiences.size)
        Assert.assertTrue(experiences.any { it.title == "Play Basketball" })
        Assert.assertFalse(experiences.any { it.title == "Play Soccer" })
    }
}