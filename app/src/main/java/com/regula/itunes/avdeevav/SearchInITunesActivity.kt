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


class SearchInITunesActivity : AppCompatActivity(), ISearchOptionsDialog {

    //TODO: implement ITunesItemListAdapter
    //private lateinit var iTunesItemListAdapter: ITunesItemListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search_in_itunes)
        setSupportActionBar(toolbar)
        initSwipeToRefresh()
        initITunesItemList()

        //TODO: search in iTunes
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_itunes_activity, menu)

        initSearchView(menu)

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.actionSearchCriteria -> showSearchCriteriaDialog()
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onSearchOptionSelected(searchOption: SearchOptions) {

        Toast.makeText(this@SearchInITunesActivity, searchOption.value, Toast.LENGTH_SHORT).show()
        //TODO: search in iTunes
    }


    private fun initSearchView(menu: Menu) {

        val searchItem: MenuItem = menu.findItem(R.id.actionSearch)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.queryHint = resources.getString(R.string.menuActionSearchInITunes)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {

                Toast.makeText(this@SearchInITunesActivity, "search...", Toast.LENGTH_SHORT).show()
                //TODO: search in iTunes

                return true
            }

        })
    }

    private fun initSwipeToRefresh() {

        swipeToRefresh.setOnRefreshListener {
            setViewUpdating(false)
            //TODO: search in iTunes
        }
    }

    private fun initITunesItemList() {

        //iTunesItemListAdapter = ITunesItemListAdapter()

        iTunesItemList.layoutManager = LinearLayoutManager(this@SearchInITunesActivity, RecyclerView.VERTICAL, false)
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

    private fun showSearchCriteriaDialog(): Boolean {

        SearchOptionsDialog
                .getInstance(supportFragmentManager)
                .show(supportFragmentManager, SearchOptionsDialog.getTag())

        return true
    }

    private fun setViewUpdating(updating: Boolean) {

        swipeToRefresh.isRefreshing = updating
    }
}
