package com.regula.itunes.avdeevav.repository.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.regula.itunes.avdeevav.repository.data.SearchResult
import com.regula.itunes.avdeevav.view.ListAdapter
import io.realm.Realm
import io.realm.RealmConfiguration

class FavoritesViewModel : ViewModel() {

    companion object {
        private const val DATABASE_NAME = "favorites.realm"
        private const val DATABASE_VERSION = 0L
    }

    private lateinit var realm: Realm
    private var realmConfig: RealmConfiguration

    private var resultList: MutableLiveData<List<SearchResult>> = MutableLiveData()

    init {
        realmConfig = RealmConfiguration.Builder()
                .name(DATABASE_NAME)
                .schemaVersion(DATABASE_VERSION)
                .build()
    }


    fun getFavoritesListObservable() = resultList

    fun getFavorites() {

        realm = Realm.getInstance(realmConfig)
        realm.executeTransactionAsync(
                { realm ->
                    realm.where(SearchResult::class.java).findAll().toList()
                },
                {
                    //
                },
                { }
        )
    }

    fun addToFavorites(listAdapter: ListAdapter, searchResult: SearchResult) {

        var index: Int? = null

        realm = Realm.getInstance(realmConfig)
        realm.executeTransactionAsync(
                { realm: Realm ->
                    realm.insertOrUpdate(searchResult)

                    index = listAdapter.getList().indexOf(searchResult)
                },
                {
                    index?.let { index: Int ->
                        listAdapter.getList()[index].favorite = true
                        listAdapter.notifyItemChanged(index)
                    }
                },
                {
                    // error callback not implemented
                }
        )
    }

    fun removeFromFavorites(listAdapter: ListAdapter, searchResult: SearchResult) {

        var index: Int? = null

        realm = Realm.getInstance(realmConfig)
        realm.executeTransactionAsync(
                { realm: Realm ->
                    realm
                            .where(SearchResult::class.java)
                            .equalTo("trackId", searchResult.trackId)
                            .findAll()
                            .deleteAllFromRealm()

                    index = listAdapter.getList().indexOf(searchResult)
                },
                {
                    index?.let { index: Int ->
                        listAdapter.getList()[index].favorite = false
                        listAdapter.notifyItemChanged(index)
                    }
                },
                {
                    // error callback not implemented
                }
        )
    }
}
