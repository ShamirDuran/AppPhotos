package com.example.appseries.adapter

import com.example.appseries.model.Comment

interface CommentListener {
    fun onCommentClicked(comment: Comment, position: Int)
}