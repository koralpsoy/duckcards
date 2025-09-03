package com.example.duckcards

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton

class QuestionListActivity : AppCompatActivity() {

    private lateinit var app: DuckCards
    private var folderId: Int = 0
    private lateinit var adapter: QuestionAdapter
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_list)

        app = DuckCards(this)
        folderId = intent.getIntExtra("folderId", 0)
        val folderName = intent.getStringExtra("folderName") ?: "Fragen"

        title = findViewById(R.id.folderTitle)
        title.text = "Ordner: $folderName"

        val recycler = findViewById<RecyclerView>(R.id.questionsRecycler)
        recycler.layoutManager = GridLayoutManager(this, 2)
        adapter = QuestionAdapter(
            onLongClick = { q ->
                AlertDialog.Builder(this)
                    .setTitle("Frage löschen?")
                    .setMessage(q.questionName)
                    .setPositiveButton("Löschen") { _, _ ->
                        app.deleteQuestion(q.questionId)
                        reload()
                    }
                    .setNegativeButton("Abbrechen", null)
                    .show()
            }
        )
        recycler.adapter = adapter

        findViewById<FloatingActionButton>(R.id.addQuestionFab).setOnClickListener {
            showAddQuestionDialog()
        }

        findViewById<Button>(R.id.start_quiz_button).setOnClickListener {
            val i = Intent(this, QuizActivity::class.java)
            i.putExtra("folderId", folderId)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        reload()
    }

    private fun reload() {
        adapter.submit(app.GetQuestionsFromFolderId(folderId))
    }

    private fun showAddQuestionDialog() {
        val view = LayoutInflater.from(this).inflate(android.R.layout.simple_list_item_2, null, false)
        val questionInput = EditText(this).apply {
            hint = "Fragetitel"
            inputType = InputType.TYPE_CLASS_TEXT
        }
        val answerInput = EditText(this).apply {
            hint = "Antwort"
            inputType = InputType.TYPE_CLASS_TEXT
        }

        val container = androidx.appcompat.widget.LinearLayoutCompat(this).apply {
            orientation = androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
            setPadding(32, 16, 32, 0)
            addView(questionInput)
            addView(answerInput)
        }

        AlertDialog.Builder(this)
            .setTitle("Frage hinzufügen")
            .setView(container)
            .setPositiveButton("Speichern") { _, _ ->
                val q = questionInput.text.toString().trim()
                val a = answerInput.text.toString().trim()
                if (q.isNotEmpty() && a.isNotEmpty()) {
                    app.insertQuestion(folderId, q, q, a) // questionName=q, questionText=q (falls du separaten Text willst, anpassen)
                    reload()
                }
            }
            .setNegativeButton("Abbrechen", null)
            .show()
    }
}
