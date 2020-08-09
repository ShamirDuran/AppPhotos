import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.appseries.R
import com.example.appseries.adapter.PostListener
import com.example.appseries.model.Serie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_perfil.view.*


class PerfilAdapter(val postListener: PostListener) :
    RecyclerView.Adapter<PerfilAdapter.ViewHolder>() {

    private val listPost = ArrayList<Serie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val perfilView = inflater.inflate(R.layout.item_perfil, parent, false)
        return ViewHolder(perfilView)
    }

    override fun getItemCount(): Int {
        return listPost.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post: Serie = listPost[position]

        if (post.imageUrl != "") {
            Picasso.get().load(post.imageUrl).into(holder.photo)
        }

        holder.itemView.setOnClickListener {
            postListener.onPostClicked(post, position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photo: ImageView = itemView.findViewById(R.id.ivPhotoPost)
    }

    fun updatePostList(data: List<Serie>) {
        this.listPost.clear()
        this.listPost.addAll(data)
        notifyDataSetChanged()
    }
}