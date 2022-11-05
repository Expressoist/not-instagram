package com.example.notinstagram

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.health.PackageHealthStats
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import com.example.notinstagram.content.ImageCard
import com.example.notinstagram.interactions.EditorContract
import com.example.notinstagram.interactions.LocationHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditorActivity : AppCompatActivity() {

    private var imageView: ImageView? = null
    private var locationTakenView: EditText? = null
    private var descriptionView: EditText? = null

    private var currentUri: Uri? = null

    private var locationHandler = LocationHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        //val imageUri = intent?.extras?.getString(EditorContract.IMAGE_URI)
        val imageUri = intent.action

        if (imageUri == null) {
            this.setResult(RESULT_CANCELED)
            return
        }

        imageView = findViewById(R.id.image_view)
        locationTakenView = findViewById(R.id.location_taken)
        descriptionView = findViewById(R.id.description)

        imageView?.setImageURI( Uri.parse(imageUri) )
        currentUri = Uri.parse(imageUri)

        findViewById<FloatingActionButton>(R.id.fab_save).setOnClickListener{ onSave() }
        findViewById<FloatingActionButton>(R.id.fab_cancel).setOnClickListener{ onCancel() }
        findViewById<ImageButton>(R.id.location_button).setOnClickListener{ insertLocation() }
    }

    private fun onSave() {
        val intent = Intent()
        intent.putExtra(EditorContract.IMAGE_URI, currentUri)
        intent.putExtra("locationTaken", locationTakenView?.text.toString())
        intent.putExtra("description", descriptionView?.text.toString())
        setResult(RESULT_OK, intent)
        finish()
    }

    fun onCancel(){

    }

    fun onRedo(){

    }

    private fun insertLocation(){
        locationHandler.findLocation { address -> onLocationFound(address) }
    }

    private fun onLocationFound(address: Address) {
        locationTakenView?.setText(address.subAdminArea);
    }
}