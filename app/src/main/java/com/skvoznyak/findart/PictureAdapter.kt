package com.skvoznyak.findart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter


class PictureAdapter(private val pictures: List<Picture>): Adapter<PictureAdapter.PictureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.picture_item, parent, false)
        return PictureViewHolder(view)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(pictures[position])
    }

    override fun getItemCount(): Int = pictures.size


    class PictureViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val image:ImageView = itemView.findViewById(R.id.item_picture)
        private val title:TextView = itemView.findViewById(R.id.item_picture_title)
        private val painter:TextView = itemView.findViewById(R.id.item_picture_painter)

        fun bind(picture: Picture) {
            image.setImageResource(picture.image)
            title.text = picture.title
            painter.text = picture.painter
        }
    }
}


class HeaderAdapter: RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_header, parent, false)
        return HeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 1
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind() {
        }
    }
}




