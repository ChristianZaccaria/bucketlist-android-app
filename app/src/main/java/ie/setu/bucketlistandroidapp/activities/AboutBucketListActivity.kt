package ie.setu.bucketlistandroidapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ie.setu.bucketlistandroidapp.databinding.ActivityAboutBucketListBinding

class AboutBucketListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBucketListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // takes an XML file as input and builds the View objects from it
        binding = ActivityAboutBucketListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // This will enable a back button in our activity's actionbar
        /* References: https://www.youtube.com/watch?v=NXLHVF06AZ4
        https://stackoverflow.com/questions/72634225/onbackpressed-deprecated-what-is-the-alternative
        https://www.geeksforgeeks.org/how-to-create-an-alert-dialog-box-in-android/ */
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}