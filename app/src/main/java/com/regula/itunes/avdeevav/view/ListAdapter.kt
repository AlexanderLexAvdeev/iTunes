package com.regula.itunes.avdeevav.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.itunes_list_item.view.*

import com.regula.itunes.avdeevav.R
import com.regula.itunes.avdeevav.repository.loader.ImageLoader
import com.regula.itunes.avdeevav.repository.data.SearchResult


class ListAdapter(val iListAdapter: IListAdapter) : RecyclerView.Adapter<ListAdapter.ListItemViewHolder>() {

    private var list: List<SearchResult> = ArrayList()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {

        return ListItemViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.itunes_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ListItemViewHolder, position: Int) {

        viewHolder.bind(list[position])
    }

    override fun getItemCount() = list.size

    override fun getItemId(position: Int) = position.toLong()


    fun update(list: List<SearchResult>) {

        this.list = list

        notifyDataSetChanged()
    }

    fun getList() = list


    inner class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(listItem: SearchResult) {

            itemView.favorite.setOnClickListener {
                iListAdapter.onFavoritesClick(listItem)
            }
            ImageLoader.load(listItem.artworkUrl100 ?: "undefined", itemView.artwork)
            itemView.name.text = listItem.trackName
            itemView.author.text = listItem.artistName
            itemView.media.text = listItem.kind
            itemView.favorite.setImageResource(
                    if (listItem.favorite == true) {
                        R.drawable.ic_favorite
                    } else {
                        R.drawable.ic_not_favorite
                    }
            )
            itemView.price.text =
                    if (listItem.formattedPrice == null) {
                        if (listItem.trackPrice == null) {
                            if (listItem.price == null) {
                                ""
                            } else {
                                "%.2f %s".format(listItem.price, listItem.currency)
                            }
                        } else {
                            "%.2f %s".format(listItem.trackPrice, listItem.currency)
                        }
                    } else {
                        listItem.formattedPrice
                    }
        }
    }
}
