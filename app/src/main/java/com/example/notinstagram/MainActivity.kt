package com.example.notinstagram

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notinstagram.content.ImageCard
import com.example.notinstagram.content.ImageCardAdapter
import com.example.notinstagram.interactions.EditorContract
import com.example.notinstagram.interactions.PhotoObserver
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(), PhotoObserver {

    private var contentView: RecyclerView? = null
    private val data = ArrayList<ImageCard>();

    private val editorLauncher = this.registerForActivityResult( EditorContract() )
                                    { result -> addImageCard(result) }

    private fun addImageCard(result: ImageCard?) {
        result?.let {
            data.add(0, it)
            contentView?.adapter?.notifyItemInserted(0)
            contentView?.scrollToPosition(0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val main = application as MainApplication
        main.database.retrieveImageCards()?.let {
            data.addAll(it)
        }

        main.photoHandler.setListener(this, this)

        val adapter = ImageCardAdapter(this, data)
        contentView = findViewById(R.id.recycler_view)
        contentView?.layoutManager = LinearLayoutManager(this)
        contentView?.adapter = adapter;

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{
            main.photoHandler.takePicture()
        }
    }

    override fun onSuccess(imageUri: Uri?) {
        editorLauncher.launch(imageUri)
    }

    override fun onFailure() {
        TODO("Not yet implemented")
    }
}