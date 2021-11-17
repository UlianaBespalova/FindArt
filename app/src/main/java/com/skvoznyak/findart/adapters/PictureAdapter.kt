package com.skvoznyak.findart

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.skvoznyak.findart.model.Picture
import com.squareup.picasso.Picasso


class PictureAdapter(val context: Context, private val pictures: List<Picture>,
                     private val callback: (() -> Unit)?):
    Adapter<PictureAdapter.PictureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.picture_item, parent, false)
        return PictureViewHolder(view)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {

// TODO: Решить вопрос с лоадером
        if (position == 0)
            holder.bind(pictures[position], context, callback)
        else holder.bind(pictures[position], context, null)
    }

    override fun getItemCount(): Int = pictures.size


    class PictureViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val image:ImageView = itemView.findViewById(R.id.item_picture)
        private val title:TextView = itemView.findViewById(R.id.item_picture_title)
        private val painter:TextView = itemView.findViewById(R.id.item_picture_painter)

        fun bind(picture: Picture, context: Context, callback: (() -> Unit)?) {
            image.setImageURI(Uri.parse(picture.image))
            title.text = picture.title
            painter.text = picture.painter

            Picasso.with(context).load(picture.image)
                .into(image, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        if (callback != null) callback()
                    }
                    override fun onError() {
                        if (callback != null) callback()
                    }
                })
        }
    }
}


class HeaderAdapter(private val text: String):
    RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_header, parent, false)
        return HeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(text)
    }

    override fun getItemCount(): Int {
        return 1
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val textView = view.findViewById<TextView>(R.id.header_text)
        fun bind(text: String) {
            textView.text = text
        }
    }
}




