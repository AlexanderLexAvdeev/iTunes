package com.regula.itunes.avdeevav

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView

import kotlinx.android.synthetic.main.activity_itunes.*


class ITunesActivity : AppCompatActivity(), ISearchCriteriaDialog {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_itunes)
        setSupportActionBar(toolbar)
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


    override fun onSearchCriteriaSelected(searchCriteria: SearchCriteria) {

        Toast.makeText(this@ITunesActivity, searchCriteria.value, Toast.LENGTH_SHORT).show()
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

                Toast.makeText(this@ITunesActivity, "search...", Toast.LENGTH_SHORT).show()

                return true
            }

        })
    }

    private fun showSearchCriteriaDialog(): Boolean {

        SearchCriteriaDialog
                .getInstance(supportFragmentManager)
                .show(supportFragmentManager, SearchCriteriaDialog.getTag())

        return true
    }
}
