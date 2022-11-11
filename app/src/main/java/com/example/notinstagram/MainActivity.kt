package com.example.notinstagram

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notinstagram.content.ImageCard
import com.example.notinstagram.content.ImageCardAdapter
import com.example.notinstagram.interactions.EditorContract
import com.example.notinstagram.interactions.PhotoHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.FileNotFoundException


class MainActivity : AppCompatActivity() {

    private var contentView: RecyclerView? = null
    private val data = ArrayList<ImageCard>();
    private val photoHandler = PhotoHandler(this)

    private val editorLauncher = this.registerForActivityResult( EditorContract() )
                                    { result -> addImageCard(result) }

    private fun addImageCard(result: ImageCard?) {
        result?.let {
            data.add(0, it)
            contentView?.adapter?.notifyItemInserted(0)
            contentView?.scrollToPosition(0)

            val main = application as MainApplication
            main.database.insertImageCard(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val main = application as MainApplication
        loadImageCards(data, main)

        val adapter = ImageCardAdapter(this, data)
        contentView = findViewById(R.id.recycler_view)
        contentView?.layoutManager = LinearLayoutManager(this)
        contentView?.adapter = adapter;

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{
            photoHandler.takePhoto{ imagePath -> onSuccess(imagePath) }
        }

        findViewById<FloatingActionButton>(R.id.fab_delete).setOnClickListener{
            main.database.clearDatabase()
            data.clear()
            contentView?.adapter?.notifyDataSetChanged()
        }

    }

    private fun loadImageCards(destination: ArrayList<ImageCard>, main: MainApplication) {
        val cards = main.database.retrieveImageCards()
        cards.forEach{ item -> checkImageExistence(item) }
        destination.addAll(cards)
    }

    private fun checkImageExistence(item: ImageCard) {
        item.imageReference?.let {
            try {
                contentResolver.openInputStream(it)?.close()
            } catch (e: FileNotFoundException) {
                item.imageReference = null
            }
        }
    }

    private fun onSuccess(imageUri: Uri?) {
        editorLauncher.launch(imageUri)
    }
}