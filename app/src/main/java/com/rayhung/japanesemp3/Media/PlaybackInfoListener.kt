package com.rayhung.japanesemp3.Media

import android.support.annotation.IntDef
import java.lang.annotation.RetentionPolicy


open abstract class PlaybackInfoListener {

    companion object {
        @IntDef(INVALID, PLAYING, PAUSED, RESET, COMPLETED)
        @Retention(AnnotationRetention.SOURCE)
        annotation class State

        const val INVALID = -1
        const val PLAYING = 0
        const val PAUSED = 1
        const val RESET = 2
        const val COMPLETED = 3

        fun convertStateToString(@State state: Int): String {
            val stateString: String
            when (state) {
                COMPLETED -> stateString = "COMPLETED"
                INVALID -> stateString = "INVALID"
                PAUSED -> stateString = "PAUSED"
                PLAYING -> stateString = "PLAYING"
                RESET -> stateString = "RESET"
                else -> stateString = "N/A"
            }
            return stateString
        }

    }

    open fun onLogUpdated(formattedMessage: String) {}

    open fun onDurationChanged(duration: Int) {}

    open fun onPositionChanged(position: Int) {}

    open fun onStateChanged(@State state: Int) {}

    open fun onPlaybackCompleted() {}

}