package com.example.appseries.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.appseries.R
import com.example.appseries.adapter.SearchUserAdapter
import com.example.appseries.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search_friend.*

class SearchFriendFragment : Fragment() {

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
        searchUserAdapter = SearchUserAdapter()

        btSearch.setOnClickListener {
            val ref = tiSearchFriendsRef.text.toString()
            searchViewModel.getDataSearch(ref)
            searchUserAdapter.updateListUser(searchViewModel.getListUsers())
        }
    }
}