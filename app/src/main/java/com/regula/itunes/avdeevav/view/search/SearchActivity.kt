package com.regula.itunes.avdeevav.view.search

import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.loader.app.LoaderManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_search_in_itunes.*
import kotlinx.android.synthetic.main.content_search_in_itunes.*

import com.regula.itunes.avdeevav.R
import com.regula.itunes.avdeevav.repository.*
import com.regula.itunes.avdeevav.repository.data.SearchResult
import com.regula.itunes.avdeevav.repository.favorite.FavoritesStorage
import com.regula.itunes.avdeevav.repository.favorite.FavoritesStorageCallback
import com.regula.itunes.avdeevav.view.IListAdapter
import com.regula.itunes.avdeevav.view.ListAdapter


class SearchActivity : AppCompatActivity(), ISearchActivity, IListAdapter, ISearchOptionsDialog {

    private var query: String = ""
    private var mediaTypeIndex: Int = 0
    private var doRequest: Boolean = false

    private lateinit var listAdapter: ListAdapter
    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this@SearchActivity).get(SearchViewModel::class.java)
    }
    private val favorites = FavoritesStorage()

    private lateinit var searchItem: MenuItem
    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search_in_itunes)
        setSupportActionBar(toolbar)
        initSwipeToRefresh()
        initSearchResultList()
        observeSearchViewModel()

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
            R.id.actionFavorites -> showFavorites()
            R.id.actionSearchOptions -> showSearchOptionsDialog()
            else -> false
        }
    }


    // ISearchActivity
    override fun showError(error: String) {

        setViewUpdating(false)
        showToast(error)
    }

    // IListAdapter
    override fun onFavoritesClick(searchResult: SearchResult) {

        if (searchResult.favorite == false) {
            favorites.add(listAdapter, searchResult)
        } else {
            favorites.remove(listAdapter, searchResult)
        }
    }

    // ISearchOptionsDialog
    override fun onMediaTypeSelected(mediaTypeIndex: Int) {

        this.mediaTypeIndex = mediaTypeIndex
        searchInITunes()
    }


    private fun initSwipeToRefresh() {

        swipeToRefresh.setOnRefreshListener {
            searchInITunes()
        }
    }

    private fun initSearchResultList() {

        listAdapter = ListAdapter(this@SearchActivity)

        list.layoutManager = LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false)
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

    private fun observeSearchViewModel() {

        searchViewModel.getResultListObservable(
                LoaderManager.getInstance(this@SearchActivity),
                this@SearchActivity
        ).observe(this, Observer {
            it?.let {
                setViewUpdating(false)
                if (it.isEmpty()) {
                    showToast(resources.getString(R.string.messageNothingFound))
                }
                listAdapter.update(it)
            }
        })
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

        supportActionBar?.title = resources.getString(R.string.appName)
        if (searchItem.isActionViewExpanded) {
            query = searchView.query.toString()
        } else {
            searchItem.expandActionView()
        }
        searchView.clearFocus()
        setViewUpdating(true)
        searchViewModel.requestResult(query, SearchMediaTypes.values()[mediaTypeIndex].value)
    }

    private fun showFavorites(): Boolean {

        supportActionBar?.title = resources.getString(R.string.menuActionFavorites)
        favorites.get(object : FavoritesStorageCallback {
            override fun onResult(favorites: List<SearchResult>) {
                listAdapter.update(favorites)
            }

        })

        return true
    }

    private fun setViewUpdating(updating: Boolean) {

        swipeToRefresh.isRefreshing = updating
    }

    private fun showSearchOptionsDialog(): Boolean {

        SearchOptionsDialog.getInstance(supportFragmentManager, mediaTypeIndex)
                .show(supportFragmentManager, SearchOptionsDialog.getTag())

        return true
    }

    private fun showToast(message: String) {

        Toast.makeText(this@SearchActivity, message, Toast.LENGTH_SHORT).show()
    }
}
