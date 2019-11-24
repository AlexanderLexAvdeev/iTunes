package com.regula.itunes.avdeevav

import android.media.RingtoneManager

object PlaySound {

    fun remove() {

        RingtoneManager.getRingtone(
                App.getContext(),
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        ).play()
    }
}
