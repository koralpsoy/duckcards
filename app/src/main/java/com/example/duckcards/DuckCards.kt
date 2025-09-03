package com.example.duckcards

import android.content.Context

class DuckCards(context: Context) {
    private val dbh = DatabaseHandler(context)

    fun GetFolders(): List<FolderModelClass> = dbh.GetFolders()
    fun GetQuestionsFromFolderId(folderId: Int): List<QuestionModelClass> = dbh.GetQuestionsFromFolderId(folderId)

    fun deleteFolder(folderId: Int): Boolean {
        dbh.deleteQuestionsFromFolderId(folderId)
        return dbh.deleteOnlyFolder(folderId)
    }

    fun deleteQuestion(questionId: Int): Boolean = dbh.deleteQuestion(questionId)
    fun insertFolder(folderName: String): Boolean = dbh.insertFolder(folderName)
    fun insertQuestion(folderId: Int, questionName: String, questionText:String, questionAnswer:String): Boolean =
        dbh.insertQuestion(folderId, questionName, questionText, questionAnswer)

    fun renameFolder(folderId: Int, folderName: String): Boolean = dbh.renameFolder(folderId, folderName)
    fun favouriteQuestion(questionId: Int, questionFav:Boolean): Boolean = dbh.favouriteQuestion(questionId, questionFav)
    fun pointsQuestion(questionId: Int, points: Int): Boolean = dbh.pointsQuestion(questionId, points)
    fun getFolderCount(folderId: Int): Int = dbh.getFolderCount(folderId)
}
