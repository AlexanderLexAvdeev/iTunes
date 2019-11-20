package com.regula.itunes.avdeevav.repository

import android.widget.ImageView

import com.squareup.picasso.Picasso

object ImageLoader {

    fun load(fromUrl: String, intoView: ImageView) {

        Picasso.get().load(fromUrl).into(intoView)
    }
}