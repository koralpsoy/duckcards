package com.example.duckcards

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.floor
import kotlin.math.max

data class FolderItem(val id: Int, val name: String, val count: Int)

class MainActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var subtitle: TextView
    private lateinit var fab: FloatingActionButton
    private lateinit var scrim: View
    private lateinit var selectionBar: View
    private lateinit var selectionTitle: TextView
    private lateinit var btnEdit: ImageButton
    private lateinit var btnDelete: ImageButton

    private val app by lazy { DuckCards(this) }
    private val adapter by lazy {
        FolderAdapter(
            onClick = { folder -> openFolder(folder) },
            onLongClick = { folder -> enterSelection(folder) }
        )
    }

    private var selected: FolderItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv = findViewById(R.id.rvFolders)
        subtitle = findViewById(R.id.tvSubtitle)
        fab = findViewById(R.id.fabAddFolder)
        scrim = findViewById(R.id.scrim)
        selectionBar = findViewById(R.id.selectionBar)
        selectionTitle = findViewById(R.id.selectionTitle)
        btnEdit = findViewById(R.id.btnEdit)
        btnDelete = findViewById(R.id.btnDelete)

        rv.adapter = adapter
        rv.layoutManager = GridLayoutManager(this, 2) // wird gleich dynamisch gesetzt

        // responsive Spaltenanzahl anhand verfügbaren Breite berechnen
        rv.post { applyResponsiveGrid() }

        fab.setOnClickListener { showCreateDialog() }
        scrim.setOnClickListener { exitSelection(animated = true) }
        btnEdit.setOnClickListener { selected?.let { showRenameDialog(it) } }
        btnDelete.setOnClickListener { selected?.let { confirmDelete(it) } }

        refreshData()
    }

    private fun dp(dp: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

    private fun applyResponsiveGrid() {
        val widthPx = rv.width
        val minCardWidth = dp(220f) // gewünschte Mindestbreite einer Karte
        val spans = max(1, floor(widthPx.toFloat() / minCardWidth).toInt())
        (rv.layoutManager as? GridLayoutManager)?.spanCount = spans
        rv.requestLayout()
    }

    private fun refreshData() {
        val folders = app.GetFolders().map {
            FolderItem(it.folderId, it.folderName, app.getFolderCount(it.folderId))
        }
        adapter.submit(folders)

        subtitle.text = if (folders.isEmpty())
            "Füge einen Ordner hinzu"
        else
            "Klicke auf einen Ordner"
    }

    private fun showCreateDialog() {
        val input = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("Neuen Ordner hinzufügen")
            .setView(input)
            .setPositiveButton("Hinzufügen") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) {
                    app.insertFolder(name)
                    refreshData()
                }
            }
            .setNegativeButton("Abbrechen", null)
            .show()
    }

    private fun showRenameDialog(folder: FolderItem) {
        val input = EditText(this)
        input.setText(folder.name)
        AlertDialog.Builder(this)
            .setTitle("Ordner umbenennen")
            .setView(input)
            .setPositiveButton("Speichern") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) {
                    app.renameFolder(folder.id, name)
                    exitSelection(animated = true)
                    refreshData()
                }
            }
            .setNegativeButton("Abbrechen", null)
            .show()
    }

    private fun confirmDelete(folder: FolderItem) {
        AlertDialog.Builder(this)
            .setTitle("Ordner löschen?")
            .setMessage("„${folder.name}“ und alle Karten werden entfernt.")
            .setPositiveButton("Löschen") { _, _ ->
                app.deleteFolder(folder.id) // inkl. Fragen
                exitSelection(animated = true)
                refreshData()
            }
            .setNegativeButton("Abbrechen", null)
            .show()
    }

    private fun openFolder(folder: FolderItem) {
        val intent = Intent(this, QuestionListActivity::class.java).apply {
            putExtra("FOLDER_ID", folder.id)
            putExtra("FOLDER_NAME", folder.name)
        }
        startActivity(intent)
    }


    private fun enterSelection(folder: FolderItem) {
        selected = folder
        selectionTitle.text = folder.name

        scrim.isVisible = true
        selectionBar.isVisible = true
        findViewById<View>(R.id.selectionPreview).isVisible = true

        scrim.alpha = 0f
        scrim.animate().alpha(1f).setDuration(150).start()
        selectionBar.alpha = 0f
        selectionBar.animate().alpha(1f).setDuration(150).start()
    }

    private fun exitSelection(animated: Boolean) {
        selected = null
        if (!animated) {
            scrim.isVisible = false
            selectionBar.isVisible = false
            findViewById<View>(R.id.selectionPreview).isVisible = false
            return
        }
        scrim.animate().alpha(0f).setDuration(120).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                scrim.isVisible = false
                scrim.alpha = 1f
            }
        }).start()
        selectionBar.animate().alpha(0f).setDuration(120).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                selectionBar.isVisible = false
                selectionBar.alpha = 1f
            }
        }).start()
        val preview = findViewById<View>(R.id.selectionPreview)
        preview.animate().alpha(0f).setDuration(120).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                preview.isVisible = false
                preview.alpha = 1f
            }
        }).start()
    }
}

