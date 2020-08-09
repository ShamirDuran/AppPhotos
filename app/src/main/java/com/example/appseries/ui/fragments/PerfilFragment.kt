package com.example.appseries.ui.fragments

import PerfilAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appseries.R
import com.example.appseries.adapter.PostListener
import com.example.appseries.data.MessageFactory
import com.example.appseries.model.Serie
import com.example.appseries.model.User
import com.example.appseries.network.Callback
import com.example.appseries.ui.activities.LoginActivity
import com.example.appseries.viewmodel.SeriesViewModel
import com.example.appseries.viewmodel.UsersViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_perfil.*
import kotlinx.android.synthetic.main.fragment_perfil.tvNombreUsuario
import kotlinx.android.synthetic.main.fragment_perfil.tvNumPhotosUploaded
import kotlinx.android.synthetic.main.fragment_perfil.tvNumSeguidores
import kotlinx.android.synthetic.main.fragment_perfil.tvNumSigue
import kotlinx.android.synthetic.main.loading_screen.*
import kotlinx.android.synthetic.main.rv_perfil.*
import java.lang.Exception

class PerfilFragment : Fragment(), PostListener {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var perfilAdapter: PerfilAdapter
    private lateinit var userViewModel: UsersViewModel
    private lateinit var serieViewModel: SeriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBarPerfil.visibility = View.INVISIBLE

        userViewModel = ViewModelProvider(this).get(UsersViewModel::class.java)
        serieViewModel = ViewModelProvider(this).get(SeriesViewModel::class.java)
        perfilAdapter = PerfilAdapter(this)

        rvPerfil.apply {
            layoutManager = GridLayoutManager(view.context, 2)
            adapter = perfilAdapter
        }

        observeUserViewModel()
        observeSerieViewModel()
        serieViewModel.suscribeToChanges()

        //userViewModel.suscribeToChanges()

        btSignOut.setOnClickListener {
            context?.let { context ->
                signOutDialog(context)
            }
        }
    }

    private fun signOutDialog(context: Context) {
        val dialogFactory = MessageFactory()
        val adviceDialog =
            dialogFactory.getDialog(context, "typeAction", object : Callback<Boolean> {
                override fun onSuccess(result: Boolean?) {
                    if (result == true) {
                        auth.signOut()
                        startActivity(Intent(context, LoginActivity::class.java))
                        activity?.finish()
                    }
                }

                override fun onFailed(exception: Exception) {}
            })
        adviceDialog.show()
    }

    private fun observeSerieViewModel() {
        serieViewModel.getLiveDataListSeries()
            .observe(viewLifecycleOwner, Observer<List<Serie>> { series ->
                series.let {
                    perfilAdapter.updatePostList(series)
                }
            })
    }

    private fun observeUserViewModel() {
        userViewModel.getLiveDataUser()
            .observe(viewLifecycleOwner, Observer<User> { user ->
                user.let {
                    tvNombreUsuario.text = user.nombre
                    tvNumSeguidores.text = user.followers?.size.toString()
                    tvNumSigue.text = user.follow?.size.toString()
                    tvNumPhotosUploaded.text = user.photosUploaded.toString()
                    gradientPerfil.visibility = View.INVISIBLE
                }
            })
    }

    override fun onPostClicked(serie: Serie, position: Int) {

    }
}