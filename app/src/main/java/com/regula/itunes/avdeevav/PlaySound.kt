package com.regula.itunes.avdeevav

import android.media.MediaActionSound
import android.media.RingtoneManager
import android.os.Build

object PlaySound {

    fun remove() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            MediaActionSound().play(MediaActionSound.STOP_VIDEO_RECORDING)
        } else {
            delete()
        }
    }

    fun delete() {

        RingtoneManager.getRingtone(
                App.getContext(),
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        ).play()
    }
}
