package com.example.appseries.adapter

import com.example.appseries.model.User

interface SearchListener {
    fun onFriendClicked(friend: User, position:Int)
}