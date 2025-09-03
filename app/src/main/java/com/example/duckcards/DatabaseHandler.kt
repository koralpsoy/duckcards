package com.example.duckcards

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DatabaseHandler(private val ctx: Context)
    : SQLiteOpenHelper(ctx, DATABASE_NAME, null, DATABASE_VERSION) {

    private val showToast = false

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "DuckCardsDatabase"

        private const val TABLE_FOLDERS = "FoldersTable"
        private const val KEY_FOLDER_ID = "fid"
        private const val KEY_FOLDER_NAME = "fname"

        private const val TABLE_QUESTIONS = "QuestionsTable"
        private const val KEY_QUESTION_ID = "qid"
        private const val KEY_QUESTION_NAME = "qname"
        private const val KEY_QUESTION_TEXT = "qtext"
        private const val KEY_QUESTION_ANSWER = "qanswer"
        private const val KEY_QUESTION_FAV = "qfav"
        private const val KEY_QUESTION_POINTS = "qpoints"

        private const val KEY_COUNT = "ANZAHL"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createFolders = """
            CREATE TABLE IF NOT EXISTS $TABLE_FOLDERS (
                $KEY_FOLDER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_FOLDER_NAME TEXT NOT NULL UNIQUE
            )
        """.trimIndent()

        val createQuestions = """
            CREATE TABLE IF NOT EXISTS $TABLE_QUESTIONS (
                $KEY_QUESTION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_FOLDER_ID INTEGER,
                $KEY_QUESTION_NAME TEXT NOT NULL,
                $KEY_QUESTION_TEXT TEXT NOT NULL,
                $KEY_QUESTION_ANSWER TEXT NOT NULL,
                $KEY_QUESTION_FAV INTEGER DEFAULT 0,
                $KEY_QUESTION_POINTS INTEGER DEFAULT 0,
                FOREIGN KEY($KEY_FOLDER_ID) REFERENCES $TABLE_FOLDERS($KEY_FOLDER_ID)
            )
        """.trimIndent()

        db.execSQL(createFolders)
        db.execSQL(createQuestions)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Schema bleibt stabil; falls nötig hier Migrations einbauen
    }

    private fun toast(msg: String) { if (showToast) Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show() }

    fun insertFolder(folderName: String): Boolean {
        return try {
            val values = ContentValues().apply {
                put(KEY_FOLDER_NAME, folderName)
            }
            writableDatabase.insertOrThrow(TABLE_FOLDERS, null, values)
            true
        } catch (e: Exception) {
            toast("insertFolder: $e"); false
        }
    }

    fun insertQuestion(folderId: Int, questionName: String, questionText: String, questionAnswer: String): Boolean {
        return try {
            val values = ContentValues().apply {
                put(KEY_FOLDER_ID, folderId)
                put(KEY_QUESTION_NAME, questionName)
                put(KEY_QUESTION_TEXT, questionText)
                put(KEY_QUESTION_ANSWER, questionAnswer)
                put(KEY_QUESTION_FAV, 0)
                put(KEY_QUESTION_POINTS, 0)
            }
            writableDatabase.insertOrThrow(TABLE_QUESTIONS, null, values)
            true
        } catch (e: Exception) {
            toast("insertQuestion: $e"); false
        }
    }

    fun renameFolder(folderId: Int, folderName: String): Boolean {
        return try {
            val values = ContentValues().apply { put(KEY_FOLDER_NAME, folderName) }
            val rows = writableDatabase.update(
                TABLE_FOLDERS, values, "$KEY_FOLDER_ID = ?", arrayOf(folderId.toString())
            )
            rows > 0
        } catch (e: Exception) { toast("renameFolder: $e"); false }
    }

    fun favouriteQuestion(questionId: Int, fav: Boolean): Boolean {
        return try {
            val values = ContentValues().apply { put(KEY_QUESTION_FAV, if (fav) 1 else 0) }
            val rows = writableDatabase.update(
                TABLE_QUESTIONS, values, "$KEY_QUESTION_ID = ?", arrayOf(questionId.toString())
            )
            rows > 0
        } catch (e: Exception) { toast("favouriteQuestion: $e"); false }
    }

    fun pointsQuestion(questionId: Int, delta: Int): Boolean {
        return try {
            val current = getPoints(questionId)
            val values = ContentValues().apply { put(KEY_QUESTION_POINTS, current + delta) }
            val rows = writableDatabase.update(
                TABLE_QUESTIONS, values, "$KEY_QUESTION_ID = ?", arrayOf(questionId.toString())
            )
            rows > 0
        } catch (e: Exception) { toast("pointsQuestion: $e"); false }
    }

    private fun getPoints(questionId: Int): Int {
        var p = 0
        val sql = "SELECT $KEY_QUESTION_POINTS FROM $TABLE_QUESTIONS WHERE $KEY_QUESTION_ID = ?"
        val c: Cursor? = readableDatabase.rawQuery(sql, arrayOf(questionId.toString()))
        c?.use { if (it.moveToFirst()) p = it.getInt(0) }
        return p
    }

    fun deleteQuestion(questionId: Int): Boolean {
        return try {
            val rows = writableDatabase.delete(
                TABLE_QUESTIONS, "$KEY_QUESTION_ID = ?", arrayOf(questionId.toString())
            )
            rows > 0
        } catch (e: Exception) { toast("deleteQuestion: $e"); false }
    }

    fun deleteQuestionsFromFolderId(folderId: Int): Boolean {
        return try {
            writableDatabase.delete(TABLE_QUESTIONS, "$KEY_FOLDER_ID = ?", arrayOf(folderId.toString()))
            true
        } catch (e: Exception) { toast("deleteQuestionsFromFolderId: $e"); false }
    }

    fun deleteOnlyFolder(folderId: Int): Boolean {
        return try {
            val rows = writableDatabase.delete(
                TABLE_FOLDERS, "$KEY_FOLDER_ID = ?", arrayOf(folderId.toString())
            )
            rows > 0
        } catch (e: Exception) { toast("deleteOnlyFolder: $e"); false }
    }

    fun getFolderCount(folderId: Int): Int {
        var count = 0
        val sql = "SELECT COUNT($KEY_QUESTION_ID) FROM $TABLE_QUESTIONS WHERE $KEY_FOLDER_ID = ?"
        val c = readableDatabase.rawQuery(sql, arrayOf(folderId.toString()))
        c.use { if (it.moveToFirst()) count = it.getInt(0) }
        return count
    }

    fun GetFolders(): List<FolderModelClass> {
        val list = ArrayList<FolderModelClass>()
        val c = readableDatabase.rawQuery("SELECT $KEY_FOLDER_ID,$KEY_FOLDER_NAME FROM $TABLE_FOLDERS ORDER BY $KEY_FOLDER_NAME ASC", null)
        c.use {
            if (it != null && it.moveToFirst()) {
                do {
                    val id = it.getInt(0)
                    val name = it.getString(1)
                    val cnt = getFolderCount(id)
                    list.add(FolderModelClass(id, name, cnt))
                } while (it.moveToNext())
            }
        }
        return list
    }

    fun GetQuestionsFromFolderId(folderId: Int): List<QuestionModelClass> {
        val list = ArrayList<QuestionModelClass>()
        val sql = """
            SELECT $KEY_QUESTION_ID,$KEY_FOLDER_ID,$KEY_QUESTION_NAME,$KEY_QUESTION_TEXT,$KEY_QUESTION_ANSWER,
                   $KEY_QUESTION_FAV,$KEY_QUESTION_POINTS
            FROM $TABLE_QUESTIONS
            WHERE $KEY_FOLDER_ID = ?
            ORDER BY $KEY_QUESTION_ID ASC
        """.trimIndent()
        val c = readableDatabase.rawQuery(sql, arrayOf(folderId.toString()))
        c.use {
            if (it != null && it.moveToFirst()) {
                do {
                    val qid = it.getInt(0)
                    val fid = it.getInt(1)
                    val qname = it.getString(2)
                    val qtext = it.getString(3)
                    val qanswer = it.getString(4)
                    val fav = it.getInt(5) != 0
                    val points = it.getInt(6)
                    list.add(
                        QuestionModelClass(
                            questionId = qid,
                            folderId = fid,
                            questionName = qname,
                            questionText = qtext,
                            questionAnswer = qanswer,
                            isFavourite = fav,
                            points = points
                        )
                    )
                } while (it.moveToNext())
            }
        }
        return list
    }
}
