package com.example.appseries.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.green
import androidx.fragment.app.DialogFragment
import com.example.appseries.R
import com.example.appseries.data.UserSingleton
import com.example.appseries.model.User
import com.example.appseries.network.SeriesServices
import kotlinx.android.synthetic.main.fragment_friend_detail_dialog.*

class FriendDetailDialogFragment : DialogFragment() {
    private val db = SeriesServices()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_detail_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val friend = arguments?.getSerializable("friend") as User

        if (UserSingleton.getInstance().follow?.contains(friend.userId)!!) {
            btFollowFriend.text = "unfollow"
        } else {
            btFollowFriend.text = "follow"
        }

        tvNombreUsuario.text = friend.nombre
        tvNumPhotosUploaded.text = friend.photosUploaded.toString()
        tvNumSeguidores.text = friend.followers?.size.toString()
        tvNumSigue.text = friend.follow?.size.toString()

        var numFollowers: Int? = friend.followers?.size

        btFollowFriend.setOnClickListener {
            var isFollow = false

            if (btFollowFriend.text == "unfollow") {
                // Dejo de seguirlo
                isFollow = true
                btFollowFriend.text = "follow"

                // Se le resta uno a seguidores
                numFollowers?.let {
                    numFollowers -= 1
                }
                tvNumSeguidores.text = numFollowers.toString()

            } else {
                // Empezo a seguirlo
                btFollowFriend.text = "unfollow"

                numFollowers?.let {
                    numFollowers += 1;
                }
                tvNumSeguidores.text = numFollowers.toString()
            }
            db.updateFollow(friend, isFollow)
        }

        btCloseFriendDetail.setOnClickListener {
            dismiss()
        }
    }
}