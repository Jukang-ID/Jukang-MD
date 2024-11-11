package com.example.jukang.helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jukang.R
import com.example.jukang.data.response.PostsItem
import com.example.jukang.databinding.CardberitaBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class AdapterNews(private val listNews : List<PostsItem>) : RecyclerView.Adapter<AdapterNews.ViewHolders>() {
    class ViewHolders(private val binding: CardberitaBinding ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news : PostsItem) {
            binding.Title.text = news.title
            Glide.with(itemView.context)
                .load(news.thumbnail)
                .into(binding.thumbnail)
            binding.des.text = news.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterNews.ViewHolders {
        val binding = CardberitaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }

    override fun onBindViewHolder(holder: AdapterNews.ViewHolders, position: Int) {
        holder.bind(listNews[position])

        holder.itemView.setOnClickListener{
            showBottomSheet(holder.itemView.context, listNews[position].title ?: "", listNews[position].description ?: "", listNews[position].thumbnail ?: "", listNews[position].link ?: "")
        }
    }

    override fun getItemCount(): Int = listNews.size

    private fun showBottomSheet(context: Context, title: String, description: String, thumbnail: String, link: String) {
        val bottomSheetDialog = BottomSheetDialog(context)

        val customView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_modal, null)

        val titleTextView = customView.findViewById<TextView>(R.id.titlemodal)
        val descriptionTextView = customView.findViewById<TextView>(R.id.deskripsimodal)
        val thumbnailImageView = customView.findViewById<ImageView>(R.id.imageView7)
        val linkButton = customView.findViewById<Button>(R.id.button2)
        val shareButton = customView.findViewById<ImageView>(R.id.share)

        titleTextView.text = title
        descriptionTextView.text = description
        Glide.with(context).load(thumbnail).into(thumbnailImageView)

        linkButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            context.startActivity(intent)
            bottomSheetDialog.dismiss()
        }

        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND, Uri.parse(link))
            intent.putExtra(Intent.EXTRA_TEXT, "$link \n $title")
            intent.type = "text/plain"
            context.startActivity(Intent.createChooser(intent, "Share to"))
        }

        bottomSheetDialog.setContentView(customView)
        bottomSheetDialog.show()
    }
}