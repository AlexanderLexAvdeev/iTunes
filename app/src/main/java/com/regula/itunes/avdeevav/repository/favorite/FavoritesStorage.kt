package com.regula.itunes.avdeevav.repository.favorite

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults

import com.regula.itunes.avdeevav.repository.search.data.SearchResult
import com.regula.itunes.avdeevav.view.ListAdapter

class FavoritesStorage {

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


    fun get(favoritesStorageCallback: FavoritesStorageCallback) {

        var favorites: List<SearchResult> = ArrayList()

        realm = Realm.getInstance(realmConfig)
        realm.executeTransactionAsync(
                { realm ->
                    favorites = realm.where(Favorite::class.java).findAll().toList()
                },
                {
                    favoritesStorageCallback.onResult(favorites)
                },
                { }
        )
    }

    fun setFavorites(searchResults: List<SearchResult>) {

        for (searchResult in searchResults) {
            realm = Realm.getInstance(realmConfig)
            val realmResults: RealmResults<Favorite> = realm
                    .where(Favorite::class.java)
                    .equalTo("trackId", searchResult.trackId)
                    .findAll()
            if (realmResults.isNotEmpty()) {
                searchResult.favorite = true
            }
        }
    }

    fun add(listAdapter: ListAdapter, searchResult: SearchResult) {

        var index: Int? = null

        realm = Realm.getInstance(realmConfig)
        realm.executeTransactionAsync(
                { realm: Realm ->
                    val realmResults: RealmResults<Favorite> = realm
                            .where(Favorite::class.java)
                            .equalTo("trackId", searchResult.trackId)
                            .findAll()
                    if (realmResults.isEmpty()) {
                        realm.insertOrUpdate(searchResult.toFavorite())
                    }

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

    fun remove(listAdapter: ListAdapter, searchResult: SearchResult, removeFromList: Boolean) {

        var position: Int? = null

        realm = Realm.getInstance(realmConfig)
        realm.executeTransactionAsync(
                { realm: Realm ->
                    realm
                            .where(Favorite::class.java)
                            .equalTo("trackId", searchResult.trackId)
                            .findAll()
                            .deleteAllFromRealm()

                    position = listAdapter.getList().indexOf(searchResult)
                },
                {
                    position?.let { position: Int ->
                        if (removeFromList) {
                            (listAdapter.getList() as ArrayList).removeAt(position)
                            if (position == 0) {
                                listAdapter.notifyDataSetChanged()
                            } else {
                                listAdapter.notifyItemRemoved(position)
                            }
                        } else {
                            listAdapter.getList()[position].favorite = false
                            listAdapter.notifyItemChanged(position)
                        }
                    }
                },
                {
                    // error callback not implemented
                }
        )
    }


    private fun RealmResults<Favorite>.toList(): List<SearchResult> {

        val dataList = ArrayList<SearchResult>()

        for (realmResult: Favorite in this) {
            dataList.add(
                    SearchResult(
                            realmResult.trackId,
                            realmResult.artworkUrl100,
                            realmResult.artworkImage,
                            realmResult.trackName,
                            realmResult.artistName,
                            realmResult.kind,
                            realmResult.formattedPrice,
                            realmResult.trackPrice,
                            realmResult.price,
                            realmResult.currency,
                            realmResult.favorite
                    )
            )
        }

        return dataList
    }

    private fun SearchResult.toFavorite(): Favorite {

        return Favorite(
                this.trackId,
                this.artworkUrl100,
                this.artworkImage,
                this.trackName,
                this.artistName,
                this.kind,
                this.formattedPrice,
                this.trackPrice,
                this.price,
                this.currency,
                true
        )
    }
}
