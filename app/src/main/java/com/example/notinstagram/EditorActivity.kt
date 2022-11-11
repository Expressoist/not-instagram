package com.example.notinstagram

import android.content.Intent
import android.graphics.drawable.Drawable
import android.location.Address
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notinstagram.interactions.EditorContract
import com.example.notinstagram.interactions.LocationHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditorActivity : AppCompatActivity() {

    private var locationButton: ImageButton? = null
    private var imageView: ImageView? = null
    private var locationTakenView: EditText? = null
    private var descriptionView: EditText? = null
    private var imagePath: Uri? = null
    private var locationHandler = LocationHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        imageView = findViewById(R.id.image_view)
        locationTakenView = findViewById(R.id.location_taken)
        descriptionView = findViewById(R.id.description)
        locationButton = findViewById(R.id.location_button)

        imagePath = intent?.extras?.get(EditorContract.IMAGE_URI) as Uri?
        imageView?.setImageURI(imagePath)

        findViewById<FloatingActionButton>(R.id.fab_save).setOnClickListener{ onSave() }
        findViewById<FloatingActionButton>(R.id.fab_cancel).setOnClickListener{ onCancel() }
        findViewById<Button>(R.id.description_werther).setOnClickListener{
            descriptionView?.setText(R.string.werther_text)
        }
        findViewById<Button>(R.id.description_kafka).setOnClickListener{
            descriptionView?.setText(R.string.kafka_text)
        }
        locationButton?.setOnClickListener{ insertLocation() }
    }

    private fun onSave() {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(EditorContract.IMAGE_URI, imagePath)
        intent.putExtra(EditorContract.LOCATION_TAKEN, locationTakenView?.text.toString())
        intent.putExtra(EditorContract.DESCRIPTION, descriptionView?.text.toString())
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun onCancel(){
        setResult(RESULT_CANCELED, Intent())
        finish()
    }

    private fun insertLocation(){
        disableLocationFields()
        locationTakenView?.setText(R.string.searching)
        locationHandler.findLocation(
            { address -> onLocationFound(address) },
            { failure -> onLocationFailure(failure) })
    }

    private fun onLocationFound(address: Address) {
        val location = "${address.locality}, ${address.countryName}"
        locationTakenView?.setText(location)
        enableLocationFields()
    }

    private fun onLocationFailure(failure: LocationHandler.Failure) {
        val errorMessage = when (failure) {
            LocationHandler.Failure.MISSING_PERMISSIONS -> R.string.no_permissions
            LocationHandler.Failure.NO_PROVIDER_AVAILABLE -> R.string.no_provider
            else -> R.string.generic_location_error
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        locationTakenView?.setText("")
        enableLocationFields()
    }

    private fun enableLocationFields() {
        locationButton?.drawable?.setTint(resources.getColor(R.color.white))
        locationButton?.background?.setTint(resources.getColor(R.color.purple_700))
        locationButton?.isEnabled = true
        locationButton?.isClickable = true
        locationTakenView?.isEnabled = true
    }

    private fun disableLocationFields() {
        locationButton?.drawable?.setTint(resources.getColor(R.color.gray))
        locationButton?.background?.setTint(resources.getColor(R.color.light_gray))
        locationButton?.isEnabled = false
        locationButton?.isClickable = false
        locationTakenView?.isEnabled = false
    }
}