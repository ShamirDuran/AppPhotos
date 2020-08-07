package com.example.appseries.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appseries.R
import com.example.appseries.adapter.SearchListener
import com.example.appseries.adapter.SearchUserAdapter
import com.example.appseries.model.User
import com.example.appseries.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search_friend.*

class SearchFriendFragment : Fragment(), SearchListener {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchUserAdapter: SearchUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        searchUserAdapter = SearchUserAdapter(this)

        rvSearchFiends.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            adapter = searchUserAdapter
        }

        btSearch.setOnClickListener {
            val ref = tiSearchFriendsRef.text.toString().capitalize()

            if (ref != ""){
                searchViewModel.getDataSearch(ref)
                observerViewModel()
            } else {
                Toast.makeText(context, "Write something", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onFriendClicked(friend: User, position: Int) {
        val bundle = bundleOf("friend" to  friend)
        findNavController().navigate(R.id.friendDetaildialogFragment, bundle)
    }

    private fun observerViewModel() {
        searchViewModel.getLiveListUsers().observe(viewLifecycleOwner, Observer<List<User>> { users ->
            users.let {
                searchUserAdapter.updateListUser(users)
            }
        })
    }
}