package com.example.appseries.ui.fragments

import PerfilAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appseries.R
import com.example.appseries.adapter.PostListener
import com.example.appseries.data.MessageFactory
import com.example.appseries.model.Serie
import com.example.appseries.model.User
import com.example.appseries.network.Callback
import com.example.appseries.network.StorageServices
import com.example.appseries.ui.activities.LoginActivity
import com.example.appseries.viewmodel.SeriesViewModel
import com.example.appseries.viewmodel.UsersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_add_serie_dialog.*
import kotlinx.android.synthetic.main.fragment_perfil.*
import kotlinx.android.synthetic.main.fragment_perfil.tvNombreUsuario
import kotlinx.android.synthetic.main.fragment_perfil.tvNumPhotosUploaded
import kotlinx.android.synthetic.main.fragment_perfil.tvNumSeguidores
import kotlinx.android.synthetic.main.fragment_perfil.tvNumSigue
import kotlinx.android.synthetic.main.loading_screen.*
import kotlinx.android.synthetic.main.rv_perfil.*
import java.lang.Exception
import java.util.*
import kotlin.concurrent.schedule

class PerfilFragment : Fragment(), PostListener {

    private val auth = FirebaseAuth.getInstance()

    private lateinit var perfilAdapter: PerfilAdapter
    private lateinit var userViewModel: UsersViewModel
    private lateinit var serieViewModel: SeriesViewModel
    private val storageServices = StorageServices()
    private var selectedPhoroUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingScreenPerfil.visibility = View.VISIBLE

        userViewModel = ViewModelProvider(this).get(UsersViewModel::class.java)
        serieViewModel = ViewModelProvider(this).get(SeriesViewModel::class.java)
        perfilAdapter = PerfilAdapter(this)

        rvPerfil.apply {
            layoutManager = GridLayoutManager(view.context, 2)
            adapter = perfilAdapter
        }

        observeUserViewModel()
        observeSerieViewModel()

        userViewModel.suscribeToChanges()
        serieViewModel.suscribeToChanges()

        btSignOut.setOnClickListener {
            context?.let { context ->
                signOutDialog(context)
            }
        }

        btChangePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
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
                    if (it.photo != "") {
                        Picasso.get().load(it.photo).into(ivPhotoUser)
                    } else {
                        ivPhotoUser.setImageResource(R.drawable.photo_perfil_clean)
                    }

                    Timer().schedule(800) {
                        try {
                            loadingScreenPerfil.visibility = View.INVISIBLE
                        } catch (e: Exception) {
                        }
                    }
                }
            })
    }

    override fun onPostClicked(serie: Serie, position: Int) {
        val bundle = bundleOf("serie" to serie)
        findNavController().navigate(R.id.serieDetailFragmentDialog, bundle)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhoroUri = data.data
            storageServices.uploadImageToFirebase(UUID.randomUUID().toString(), selectedPhoroUri)
        }
    }
}