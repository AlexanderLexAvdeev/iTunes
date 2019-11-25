package com.regula.itunes.avdeevav.view.favorite

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.content_favorites.*

import com.regula.itunes.avdeevav.media.PlaySound
import com.regula.itunes.avdeevav.R
import com.regula.itunes.avdeevav.repository.favorite.FavoritesStorage
import com.regula.itunes.avdeevav.repository.favorite.FavoritesStorageCallback
import com.regula.itunes.avdeevav.repository.search.data.SearchResult
import com.regula.itunes.avdeevav.view.IListAdapter
import com.regula.itunes.avdeevav.view.ListAdapter
import com.regula.itunes.avdeevav.view.search.SearchActivity

class FavoritesActivity : AppCompatActivity(), IListAdapter {

    private lateinit var listAdapter: ListAdapter
    private val favorites = FavoritesStorage()

    private var favoritesChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_favorites)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initFavoritesList()
        showFavorites()
    }


    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()

        return true
    }

    override fun onBackPressed() {

        setResult(
                Activity.RESULT_OK,
                Intent().putExtra(SearchActivity.UPDATE_LIST, favoritesChanged)
        )
        finish()
    }


    // IListAdapter
    override fun onFavoriteClick(searchResult: SearchResult) {

        Snackbar
                .make(activity, R.string.text, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action) {
                    PlaySound.remove()
                    favorites.remove(listAdapter, searchResult, true)
                    favoritesChanged = true
                }.show()
    }


    private fun initFavoritesList() {

        listAdapter = ListAdapter(this@FavoritesActivity, true)

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
