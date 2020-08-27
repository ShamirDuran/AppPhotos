package com.example.appseries.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appseries.R
import com.example.appseries.adapter.CommentListener
import com.example.appseries.adapter.CommentsAdapter
import com.example.appseries.data.MessageFactory
import com.example.appseries.data.UserSingleton
import com.example.appseries.model.Comment
import com.example.appseries.model.Serie
import com.example.appseries.model.User
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices
import com.example.appseries.network.StorageServices
import com.example.appseries.viewmodel.CommentsViewModel
import com.example.appseries.viewmodel.SerieDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_series_detail_dialog.*

class SeriesDetailDialogFragment : DialogFragment(), CommentListener {

    private val db = SeriesServices()
    private val storageServ = StorageServices()
    private var isFav: Boolean = false
    private lateinit var serieDetaillViewModel: SerieDetailViewModel
    private lateinit var commentViewModel: CommentsViewModel
    private lateinit var commentAdapter: CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_series_detail_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commentViewModel = ViewModelProvider(this).get(CommentsViewModel::class.java)
        commentAdapter = CommentsAdapter(this)

        rvComments.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = commentAdapter
        }

        val serie = arguments?.getSerializable("serie") as Serie
        getUserInfo(serie.userId)
        changeGUI(serie)

        commentViewModel.setPostId(serie.idSerie)
        commentViewModel.getComments()

        // Se comprueba si el post esta agregado como favorito
        isFav = UserSingleton.getInstance().seriesFav.contains(serie.idSerie)

        changeIconButtonFav()

        serieDetaillViewModel = ViewModelProvider(this).get(SerieDetailViewModel::class.java)
        serieDetaillViewModel.setDataSerie(serie)

        if (serie.imageUrl != "") {
            Picasso.get().load(serie.imageUrl).into(ivImagenSerieDialog)
        }

        btDeleteSerie.setOnClickListener {
            val dialogFactory = MessageFactory()
            context?.let {
                val adviceDialog =
                    dialogFactory.getDialog(it, "typeAdvice", object : Callback<Boolean> {
                        override fun onSuccess(result: Boolean?) {
                            if (result == true) {
                                storageServ.deleteImageSerie(serie)
                                dismiss()
                            }
                        }

                        override fun onFailed(exception: Exception) {}
                    })
                adviceDialog.show()
            }
        }

        btCloseSerieDetail.setOnClickListener { dismiss() }

        btEditSerie.setOnClickListener { onEditSerieClicked(serie) }

        btAddFav.setOnClickListener {
            onAddFavClicked(serie)
            isFav = !isFav
            changeIconButtonFav()
        }

        itComment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (itComment.text == null) {
                    btSendComment.visibility = View.VISIBLE
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p3 == 0) {
                    // si esta vacio oculte el boton
                    btSendComment.visibility = View.INVISIBLE
                } else {
                    // si contiene algo muestre boton de send
                    btSendComment.visibility = View.VISIBLE
                }
            }
        })

        btSendComment.setOnClickListener { sendComment(itComment.text.toString(), serie.idSerie) }

        observeSerieDetailViewModel()
        observeCommentViewModel()

        serieDetaillViewModel.suscribeToChanges()
        commentViewModel.suscribeToChanges()

    }

    private fun getUserInfo(id_user: String) {
        db.getDataUser(object : Callback<User> {
            override fun onSuccess(result: User?) {
                if (result != null) {
                    if (result.photo != "") Picasso.get().load(result.photo).into(ivPhotoOwner)
                    tvUserOwner.text = result.nombre
                }
//                loadingGradient.visibility = View.INVISIBLE
                cardComment.visibility = View.VISIBLE
            }

            override fun onFailed(exception: Exception) {
                Log.e("Error seriesDetail", "Error al obtener info del autor", exception.cause)
            }
        }, id_user)
    }

    override fun onCommentClicked(comment: Comment, position: Int) {
        Log.d("Mensaje", "Comentario seleccionado")
    }

    private fun sendComment(content: String, idSerie: String) {
        val comment = Comment("", idSerie, content)
        db.addComment(comment, object : Callback<Boolean> {
            override fun onSuccess(result: Boolean?) {
                Toast.makeText(context, "Comment save!", Toast.LENGTH_SHORT).show()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                itComment.text?.clear()
                itComment.hint = "Comment..."
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
            }

            override fun onFailed(exception: Exception) {
                Toast.makeText(context, "Error, commant not save", Toast.LENGTH_SHORT).show()
                Log.e("Error seriesDetail", "Error al guardar comentario", exception.cause)
            }

        })
    }

    private fun changeGUI(serie: Serie) {
        if (UserSingleton.getInstance().userId == serie.userId) {
            // es el autor del post
            btEditSerie.visibility = View.VISIBLE
            btEditSerie.isEnabled = true
            btDeleteSerie.visibility = View.VISIBLE
            btDeleteSerie.isEnabled = true
        } else {
            // no es autor del post
            btEditSerie.visibility = View.INVISIBLE
            btEditSerie.isEnabled = false
            btDeleteSerie.visibility = View.INVISIBLE
            btDeleteSerie.isEnabled = false
        }
    }

    private fun changeIconButtonFav() {
        // Esta como fav, se cambia el icono a facorito
        if (isFav) btAddFav.setImageResource(R.drawable.ic_fav_pink)
        // No esta como fav, se deja el icono vacio
        else btAddFav.setImageResource(R.drawable.ic_fav_border)
    }

    private fun onAddFavClicked(serie: Serie) {
        db.updateFavorite(serie.idSerie, isFav)
    }

    private fun onEditSerieClicked(serie: Serie) {
        val bundle = bundleOf("serie" to serie)
        findNavController().navigate(R.id.editSerieDialogFragment, bundle)
    }

    private fun observeSerieDetailViewModel() {
        serieDetaillViewModel.getLivedataSerie()
            .observe(viewLifecycleOwner, Observer<Serie> { serie ->
                serie.let {
                    tvNombreSerieDialog.text = serie.nombre
                    tvDescSerieDialog.text = serie.desc
                }
            })
    }

    private fun observeCommentViewModel() {
        commentViewModel.getLiveDataComments()
            .observe(viewLifecycleOwner, Observer<List<Comment>> {
                it.let {
                    commentAdapter.updateListComments(it)
                }
            })
    }
}