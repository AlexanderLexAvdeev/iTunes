package com.regula.itunes.avdeevav.view.favorite

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_favorites.*

import com.regula.itunes.avdeevav.R
import com.regula.itunes.avdeevav.repository.favorite.FavoritesStorage
import com.regula.itunes.avdeevav.repository.favorite.FavoritesStorageCallback
import com.regula.itunes.avdeevav.repository.search.data.SearchResult
import com.regula.itunes.avdeevav.view.IListAdapter
import com.regula.itunes.avdeevav.view.ListAdapter
import kotlinx.android.synthetic.main.content_favorites.*

class FavoritesActivity : AppCompatActivity(), IListAdapter {

    private lateinit var listAdapter: ListAdapter
    private val favorites = FavoritesStorage()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_favorites)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initFavoritesList()
        showFavorites()
    }


    override fun onSupportNavigateUp(): Boolean {

        finish()

        return true
    }


    // IListAdapter
    override fun onFavoriteClick(searchResult: SearchResult) {

        if (searchResult.favorite == false) {
            favorites.add(listAdapter, searchResult)
        } else {
            favorites.remove(listAdapter, searchResult)
        }
    }


    private fun initFavoritesList() {

        listAdapter = ListAdapter(this@FavoritesActivity)

        list.layoutManager = LinearLayoutManager(this@FavoritesActivity, RecyclerView.VERTICAL, false)
        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = resources.getDimensionPixelSize(R.dimen.listOffsetVertical)
                } else {
                    parent.adapter?.let { adapter ->
                        if (parent.getChildAdapterPosition(view) == adapter.itemCount - 1) {
                            outRect.bottom = resources.getDimensionPixelSize(R.dimen.listOffsetVertical)
                        }
                    }
                }
            }
        })
        list.setHasFixedSize(true)
        list.adapter = listAdapter
    }

    private fun showFavorites() {

        favorites.get(object : FavoritesStorageCallback {
            override fun onResult(favorites: List<SearchResult>) {
                if (favorites.isEmpty()) {
                    showToast(resources.getString(R.string.messageNothingToShow))
                }
                listAdapter.update(favorites)
            }

        })
    }

    private fun showToast(message: String) {

        Toast.makeText(this@FavoritesActivity, message, Toast.LENGTH_SHORT).show()
    }
}
