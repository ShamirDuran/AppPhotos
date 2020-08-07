package com.example.appseries.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.appseries.R
import com.example.appseries.model.User
import com.example.appseries.ui.activities.LoginActivity
import com.example.appseries.viewmodel.UsersViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_friend_detail_dialog.*
import kotlinx.android.synthetic.main.fragment_perfil.*
import kotlinx.android.synthetic.main.fragment_perfil.tvNombreUsuario
import kotlinx.android.synthetic.main.fragment_perfil.tvNumPhotosUploaded
import kotlinx.android.synthetic.main.fragment_perfil.tvNumSeguidores

class PerfilFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var  userViewModel: UsersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this).get(UsersViewModel::class.java)

        val user = userViewModel.getLiveDataUser()

        btSingOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }

        observeUserViewModel()
        userViewModel.suscribeToChanges()
    }

    private fun observeUserViewModel() {
        userViewModel.getLiveDataUser()
            .observe(viewLifecycleOwner, Observer<User> { user ->
                user.let {
                    tvNombreUsuario.text = user.nombre
                    tvNumSeguidores.text = user.followers?.size.toString()
                    tvNumPhotosUploaded.text = user.photosUploaded.toString()
                }
            })
    }
}