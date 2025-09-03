package com.example.duckcards

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var app: DuckCards
    private var folderId: Int = 0

    private lateinit var header: TextView
    private lateinit var title: TextView
    private lateinit var qText: TextView
    private lateinit var aText: TextView
    private lateinit var showBtn: Button
    private lateinit var ducks: LinearLayout
    private lateinit var green: ImageButton
    private lateinit var yellow: ImageButton
    private lateinit var red: ImageButton

    private val questions = ArrayList<QuestionModelClass>()
    private var index = 0

    private var correct = 0
    private var unsure = 0
    private var wrong = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        app = DuckCards(this)
        folderId = intent.getIntExtra("folderId", 0)

        header = findViewById(R.id.quizHeader)
        title = findViewById(R.id.questionTitle)
        qText = findViewById(R.id.questionText)
        aText = findViewById(R.id.answerText)
        showBtn = findViewById(R.id.showAnswerBtn)
        ducks = findViewById(R.id.ducksRow)
        green = findViewById(R.id.greenDuck)
        yellow = findViewById(R.id.yellowDuck)
        red = findViewById(R.id.redDuck)

        loadData()
        bindCurrent()

        showBtn.setOnClickListener {
            aText.visibility = View.VISIBLE
            ducks.visibility = View.VISIBLE
            showBtn.visibility = View.GONE
        }

        green.setOnClickListener { rate(+1) }
        yellow.setOnClickListener { rate(0) }
        red.setOnClickListener { rate(-1) }
    }

    private fun loadData() {
        questions.clear()
        questions.addAll(app.GetQuestionsFromFolderId(folderId))

        // Erste Session = alle Punkte 0 -> natürliche Reihenfolge (questionId aufsteigend)
        // Spätere Sessions: sortiert nach Punkten (aufsteigend = schwächere zuerst)
        val allZero = questions.all { it.points == 0 }
        if (allZero) {
            questions.sortBy { it.questionId }
        } else {
            questions.sortBy { it.points }
        }

        index = 0
        header.text = "Abfrage – ${questions.size} Karten"
    }

    private fun bindCurrent() {
        if (index >= questions.size) {
            finishSession()
            return
        }
        val q = questions[index]
        title.text = q.questionName
        qText.text = q.questionText
        aText.text = q.questionAnswer

        // Reset Sichtbarkeit
        aText.visibility = View.GONE
        ducks.visibility = View.GONE
        showBtn.visibility = View.VISIBLE
    }

    private fun rate(delta: Int) {
        val q = questions[index]

        // Statistik
        when (delta) {
            +1 -> correct++
            0  -> unsure++
            -1 -> wrong++
        }

        // Punkte persistent speichern (nur +1 / 0 / -1)
        if (delta != 0) app.pointsQuestion(q.questionId, delta)

        // Nächste Karte (während Session NICHT neu sortieren)
        index++
        bindCurrent()
    }

    private fun finishSession() {
        val i = Intent(this, ResultActivity::class.java)
        i.putExtra("green", correct)
        i.putExtra("yellow", unsure)
        i.putExtra("red", wrong)
        startActivity(i)
        finish()
    }
}
