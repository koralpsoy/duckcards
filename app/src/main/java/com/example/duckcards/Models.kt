package com.example.duckcards

data class FolderModelClass(var folderId: Int, var folderName: String, var folderCount: Int)

data class QuestionModelClass(
    var questionId: Int,
    var folderId: Int,
    var questionName: String,
    var questionText: String,
    var questionAnswer: String,
    var isFavourite: Boolean,
    var points: Int
)
