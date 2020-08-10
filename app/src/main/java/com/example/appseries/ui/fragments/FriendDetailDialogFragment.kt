package com.example.appseries.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appseries.R
import com.example.appseries.adapter.FriendAdapter
import com.example.appseries.adapter.PostListener
import com.example.appseries.data.UserSingleton
import com.example.appseries.model.Serie
import com.example.appseries.model.User
import com.example.appseries.network.SeriesServices
import com.example.appseries.viewmodel.FriendViewModel
import kotlinx.android.synthetic.main.fragment_friend_detail_dialog.*

class FriendDetailDialogFragment : DialogFragment(), PostListener {
    private val db = SeriesServices()
    private var isFollow: Boolean = false //false = no follow - true = follow
    private var numFollowers: Int = 0
    private lateinit var friendViewModel: FriendViewModel
    private lateinit var friendAdapter: FriendAdapter

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
        friendViewModel = ViewModelProvider(this).get(FriendViewModel::class.java)
        friendViewModel.getDataPosts(friend.userId)
        friendAdapter = FriendAdapter(this)
        rvFriendPost.layoutManager = GridLayoutManager(context, 2)
        rvFriendPost.adapter = friendAdapter

        configureUI(friend)
        observeViewModel()

        friend.followers?.let {
            numFollowers = it.size
        }

        btFollowFriend.setOnClickListener {
            btFollowClicked(friend)
        }
    }

    private fun observeViewModel() {
        friendViewModel.getLiveDataPost()
            .observe(viewLifecycleOwner, Observer<List<Serie>> { series ->
                series.let {
                    friendAdapter.updateListPost(it)
                }
            })
    }

    private fun btFollowClicked(friend: User) {
        if (isFollow) {
            // Dejo de seguirlo
            isFollow = false
            btFollowFriend.setImageResource(R.drawable.ic_follow)

            // Se le resta uno a seguidores
            numFollowers.let {
                this.numFollowers -= 1
            }
            tvNumSeguidores.text = numFollowers.toString()

        } else {
            // Empezo a seguirlo
            isFollow = true
            btFollowFriend.setImageResource(R.drawable.ic_close)

            numFollowers.let {
                numFollowers += 1
            }
            tvNumSeguidores.text = numFollowers.toString()
        }
        db.updateFollow(friend, isFollow)
    }

    private fun configureUI(friend: User) {
        if (UserSingleton.getInstance().follow?.contains(friend.userId)!!) {
            // is following
            isFollow = true
            btFollowFriend.setImageResource(R.drawable.ic_close)
        } else {
            isFollow = false
            // not following
            btFollowFriend.setImageResource(R.drawable.ic_follow)
        }

        tvNombreUsuario.text = friend.nombre
        tvNumPhotosUploaded.text = friend.photosUploaded.toString()
        tvNumSeguidores.text = friend.followers?.size.toString()
        tvNumSigue.text = friend.follow?.size.toString()
    }

    override fun onPostClicked(serie: Serie, position: Int) {
        TODO("Not yet implemented")
    }
}