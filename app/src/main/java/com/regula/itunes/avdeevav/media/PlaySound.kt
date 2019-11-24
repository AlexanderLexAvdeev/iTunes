package com.regula.itunes.avdeevav.media

import android.media.RingtoneManager
import com.regula.itunes.avdeevav.App

object PlaySound {

    fun remove() {

        RingtoneManager.getRingtone(
                App.getContext(),
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        ).play()
    }
}
