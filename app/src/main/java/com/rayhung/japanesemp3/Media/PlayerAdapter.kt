package com.rayhung.japanesemp3.Media

interface PlayerAdapter {

    fun loadMedia(resourceId: Int)

    fun release()

    fun isPlaying(): Boolean

    fun play()

    fun reset()

    fun pause()

    fun initializeProgressCallback()

    fun seekTo(position: Int)

}