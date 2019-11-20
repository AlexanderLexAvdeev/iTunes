package com.regula.itunes.avdeevav

import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_search_in_itunes.*
import kotlinx.android.synthetic.main.content_search_in_itunes.*


class SearchActivity : AppCompatActivity(), ISearchOptionsDialog {

    private var query: String = ""
    private var mediaTypeIndex: Int = 0
    private var doRequest: Boolean = false

    //TODO: implement ITunesItemListAdapter
    //private lateinit var iTunesItemListAdapter: ITunesItemListAdapter

    private lateinit var searchItem: MenuItem
    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search_in_itunes)
        setSupportActionBar(toolbar)
        initSwipeToRefresh()
        initITunesItemList()

        if (savedInstanceState == null) {
            doRequest = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_itunes_activity, menu)

        initSearchView(menu)
        executeLastSearchRequest()

        return super.onCreateOptionsMenu(menu)
    }

    override fun onPause() {

        super.onPause()

        if (searchItem.isActionViewExpanded) {
            LastSearchRequest.setQuery(this@SearchActivity, searchView.query.toString())
        } else {
            LastSearchRequest.setQuery(this@SearchActivity, query)
        }
        LastSearchRequest.setMediaTypeIndex(this@SearchActivity, mediaTypeIndex)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.actionSearchCriteria -> showSearchOptionsDialog()
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onMediaTypeSelected(mediaTypeIndex: Int) {

        this.mediaTypeIndex = mediaTypeIndex
        searchInITunes()
    }


    private fun initSwipeToRefresh() {

        swipeToRefresh.setOnRefreshListener {
            searchInITunes()
        }
    }

    private fun initITunesItemList() {

        //iTunesItemListAdapter = ITunesItemListAdapter()

        iTunesItemList.layoutManager = LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false)
        iTunesItemList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = resources.getDimensionPixelSize(R.dimen.iTunesItemListOffsetVertical)
                } else {
                    parent.adapter?.let { adapter ->
                        if (parent.getChildAdapterPosition(view) == adapter.itemCount - 1) {
                            outRect.bottom = resources.getDimensionPixelSize(R.dimen.iTunesItemListOffsetVertical)
                        }
                    }
                }
            }
        })
        iTunesItemList.setHasFixedSize(true)
        //iTunesItemList.adapter = iTunesItemListAdapter
    }

    private fun initSearchView(menu: Menu) {

        searchItem = menu.findItem(R.id.actionSearch)
        searchView = searchItem.actionView as SearchView

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {

                searchView.onActionViewExpanded()
                searchView.setQuery(query, false)

                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {

                query = searchView.query.toString()

                return true
            }
        })
        searchView.queryHint = resources.getString(R.string.menuActionSearchInITunes)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {

                this@SearchActivity.query = searchView.query.toString()
                searchInITunes()

                return true
            }

        })
    }

    private fun executeLastSearchRequest() {

        initSearchOptions()

        if (doRequest) {
            searchInITunes()
        }
    }

    private fun initSearchOptions() {

        query = LastSearchRequest.getQuery(this@SearchActivity)
        mediaTypeIndex = LastSearchRequest.getMediaTypeIndex(this@SearchActivity)
    }

    private fun searchInITunes() {

        if (searchItem.isActionViewExpanded) {
            query = searchView.query.toString()
        } else {
            searchItem.expandActionView()
        }
        searchView.clearFocus()
        setViewUpdating(true)
        Toast.makeText(this@SearchActivity, "Search in ".plus(SearchMediaTypes.values()[mediaTypeIndex].mediaType), Toast.LENGTH_SHORT).show()
        //TODO: searchInITunes
    }

    private fun setViewUpdating(updating: Boolean) {

        swipeToRefresh.isRefreshing = updating
    }

    private fun showSearchOptionsDialog(): Boolean {

        SearchOptionsDialog
                .getInstance(supportFragmentManager, mediaTypeIndex)
                .show(supportFragmentManager, SearchOptionsDialog.getTag())

        return true
    }
}