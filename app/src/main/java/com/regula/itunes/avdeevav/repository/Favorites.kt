package com.regula.itunes.avdeevav.repository

import io.realm.Realm
import io.realm.RealmConfiguration

import com.regula.itunes.avdeevav.repository.data.SearchResult
import com.regula.itunes.avdeevav.view.ListAdapter

class Favorites {

    companion object {
        private const val DATABASE_NAME = "favorites.realm"
        private const val DATABASE_VERSION = 0L
    }

    private lateinit var realm: Realm
    private var realmConfig: RealmConfiguration

    init {
        realmConfig = RealmConfiguration.Builder()
                .name(DATABASE_NAME)
                .schemaVersion(DATABASE_VERSION)
                .build()
    }


    fun get(favoritesCallback: FavoritesCallback) {

        var favorites: List<SearchResult> = ArrayList()

        realm = Realm.getInstance(realmConfig)
        realm.executeTransactionAsync(
                { realm ->
                    favorites = realm.where(SearchResult::class.java).findAll().toList()
                },
                {
                    favoritesCallback.onResult(favorites)
                },
                { }
        )
    }

    fun add(listAdapter: ListAdapter, searchResult: SearchResult) {

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

    fun remove(listAdapter: ListAdapter, searchResult: SearchResult) {

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
