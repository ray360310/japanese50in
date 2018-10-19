package com.rayhung.japanesemp3.Media

import android.content.Context
import android.media.MediaPlayer
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import android.content.res.AssetFileDescriptor;

class MediaPlayerHolder(context: Context) : PlayerAdapter {

    private val mContext: Context
    private var mMediaPlayer: MediaPlayer? = null
    private var mResourceId: Int = 0
    private var mPlaybackInfoListener: PlaybackInfoListener? = null
    private var mExecutor: ScheduledExecutorService? = null
    private var mSeekbarPositionUpdateTask: Runnable? = null

    init {
        mContext = context.getApplicationContext()
    }
    private fun initializeMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
            mMediaPlayer!!.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
                override fun onCompletion(mediaPlayer: MediaPlayer) {
                    stopUpdatingCallbackWithPosition(true)
                    logToUI("MediaPlayer playback completed")
                    if (mPlaybackInfoListener != null) {
                        mPlaybackInfoListener!!.onStateChanged(PlaybackInfoListener.COMPLETED)
                        mPlaybackInfoListener!!.onPlaybackCompleted()
                    }
                }
            })


            logToUI("mMediaPlayer = new MediaPlayer()")
        }
    }

    fun setPlaybackInfoListener(listener: PlaybackInfoListener) {
        mPlaybackInfoListener = listener
    }

    // Implements PlaybackControl.
    override fun loadMedia(resourceId: Int) {
        mResourceId = resourceId

        initializeMediaPlayer()

        val assetFileDescriptor = mContext.getResources().openRawResourceFd(mResourceId)
        try {
            logToUI("load() {1. setDataSource}")
            mMediaPlayer!!.setDataSource(assetFileDescriptor)
        } catch (e: Exception) {
            logToUI(e.toString())
        }

        try {
            logToUI("load() {2. prepare}")
            mMediaPlayer!!.prepare()
        } catch (e: Exception) {
            logToUI(e.toString())
        }

        initializeProgressCallback()
        logToUI("initializeProgressCallback()")
    }

    override fun release() {
        if (mMediaPlayer != null) {
            logToUI("release() and mMediaPlayer = null")
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    override fun isPlaying(): Boolean {
        return if (mMediaPlayer != null) {
            mMediaPlayer!!.isPlaying()
        } else false
    }

    override fun play() {
        if (mMediaPlayer != null && !mMediaPlayer!!.isPlaying()) {
            logToUI(
                String.format(
                    "playbackStart() %s",
                    mContext.getResources().getResourceEntryName(mResourceId)
                )
            )
            mMediaPlayer!!.start()
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener!!.onStateChanged(PlaybackInfoListener.PLAYING)
            }
            startUpdatingCallbackWithPosition()
        }
    }

    override fun reset() {
        if (mMediaPlayer != null) {
            logToUI("playbackReset()")
            mMediaPlayer!!.reset()
            loadMedia(mResourceId)
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener!!.onStateChanged(PlaybackInfoListener.RESET)
            }
            stopUpdatingCallbackWithPosition(true)
        }
    }

    override fun pause() {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying()) {
            mMediaPlayer!!.pause()
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener!!.onStateChanged(PlaybackInfoListener.PAUSED)
            }
            logToUI("playbackPause()")
        }
    }

    override fun seekTo(position: Int) {
        if (mMediaPlayer != null) {
            logToUI(String.format("seekTo() %d ms", position))
            mMediaPlayer!!.seekTo(position)
        }
    }

    /**
     * Syncs the mMediaPlayer position with mPlaybackProgressCallback via recurring task.
     */
    private fun startUpdatingCallbackWithPosition() {
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadScheduledExecutor()
        }
        if (mSeekbarPositionUpdateTask == null) {
            mSeekbarPositionUpdateTask = Runnable { updateProgressCallbackTask() }
        }
        mExecutor!!.scheduleAtFixedRate(
            mSeekbarPositionUpdateTask,
            0,
            PLAYBACK_POSITION_REFRESH_INTERVAL_MS.toLong(),
            TimeUnit.MILLISECONDS
        )
    }

    // Reports media playback position to mPlaybackProgressCallback.
    private fun stopUpdatingCallbackWithPosition(resetUIPlaybackPosition: Boolean) {
        if (mExecutor != null) {
            mExecutor!!.shutdownNow()
            mExecutor = null
            mSeekbarPositionUpdateTask = null
            if (resetUIPlaybackPosition && mPlaybackInfoListener != null) {
                mPlaybackInfoListener!!.onPositionChanged(0)
            }
        }
    }

    private fun updateProgressCallbackTask() {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying()) {
            val currentPosition = mMediaPlayer!!.getCurrentPosition()
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener!!.onPositionChanged(currentPosition)
            }
        }
    }

    override fun initializeProgressCallback() {
        val duration = mMediaPlayer!!.getDuration()
        if (mPlaybackInfoListener != null) {
            mPlaybackInfoListener!!.onDurationChanged(duration)
            mPlaybackInfoListener!!.onPositionChanged(0)
            logToUI(
                String.format(
                    "firing setPlaybackDuration(%d sec)",
                    TimeUnit.MILLISECONDS.toSeconds(duration.toLong())
                )
            )
            logToUI("firing setPlaybackPosition(0)")
        }
    }

    private fun logToUI(message: String) {
        if (mPlaybackInfoListener != null) {
            mPlaybackInfoListener!!.onLogUpdated(message)
        }
    }

    companion object {

        val PLAYBACK_POSITION_REFRESH_INTERVAL_MS = 1000
    }

}