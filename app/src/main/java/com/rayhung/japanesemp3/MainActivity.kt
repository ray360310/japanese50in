package com.rayhung.japanesemp3

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.rayhung.japanesemp3.Adapter.RV.ContentListRVAdapter
import com.rayhung.japanesemp3.Adapter.RV.NavMenuRVAdapter
import com.rayhung.japanesemp3.Model.NavRVItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.activity_main_nav.*
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle
import android.view.View
import com.rayhung.japanesemp3.Conf.GetResources
import com.rayhung.japanesemp3.Conf.LogD
import com.rayhung.japanesemp3.Listener.RecyclerItemClickListener
import com.rayhung.japanesemp3.Media.PlayerAdapter
import com.rayhung.japanesemp3.Media.MediaPlayerHolder
import com.rayhung.japanesemp3.Media.PlaybackInfoListener
import android.widget.SeekBar




class MainActivity : AppCompatActivity() {

//    private val CHAPTER_01 = 0
//    private val CHAPTER_02 = 1
//    private val CHAPTER_03 = 2
//    private val CHAPTER_04 = 3
//    private val CHAPTER_05 = 4
//    private val CHAPTER_PRACTICE_01 = 5
//    private val CHAPTER_06 = 6
//    private val CHAPTER_07 = 7
//    private val CHAPTER_08 = 8
//    private val CHAPTER_09 = 9
//    private val CHAPTER_10 = 10
//    private val CHAPTER_11 = 11
//    private val CHAPTER_PRACTICE_02 = 12

    private var PLAY_PREVIOUS = 50
    private var PLAY_NEXT = 51

    private var navMenuRVAdapter: NavMenuRVAdapter? = null
    private var contentListRVAdapter: ContentListRVAdapter? = null
    private var chpaterList: MutableList<String> ?= null
    private var contentStrList: MutableList<String> ?= null
    private var contentNumList: MutableList<String> ?= null

    private var mPlayerAdapter: PlayerAdapter? = null
    private var mUserIsSeeking = false
    private var currentListLength = 0
    private var currentSelectedNum = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialView()
    }

    private fun initialView(){
        setSupportActionBar(activity_main_toolbar)
        val drawerToggle = DuoDrawerToggle(this,
            activity_main_drawer,
            activity_main_toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        activity_main_drawer.setDrawerListener(drawerToggle)
        drawerToggle.syncState()
        activity_main_toolbar.title = ""
        activity_main_toolbar.subtitle = ""
        activity_main_toolbar.setTitleTextColor(resources.getColor(android.R.color.white))
        activity_main_maintitletxw.text = resources.getString(R.string.app_name)
        navSetup()
        setupOnClickListener()
        contentSetup()
        initializePlaybackController()
    }

    private fun navSetup(){
        navMenuRVAdapter = NavMenuRVAdapter(ArrayList())
        val itemDecor = DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
        nav_list_rcview!!.addItemDecoration(itemDecor)
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)
        nav_list_rcview!!.adapter = navMenuRVAdapter
        nav_list_rcview!!.setHasFixedSize(true)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        nav_list_rcview!!.layoutManager = linearLayoutManager
        chpaterList = (resources.getStringArray(R.array.list_nav_chapter)).toMutableList()
        var navRVItemList = ArrayList<NavRVItem>()
        for (i in 0..12){
            var tempItem= NavRVItem()
            tempItem.option = chpaterList!![i]
            tempItem.resourceImg = R.drawable.ic_label_light
            navRVItemList.add(tempItem)
        }
        navMenuRVAdapter?.setNavItemList(navRVItemList)
    }

    private fun contentSetup(){
        contentListRVAdapter = ContentListRVAdapter(ArrayList())
        val itemDecor = DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
        content_list_rcview!!.addItemDecoration(itemDecor)
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)
        content_list_rcview!!.adapter = contentListRVAdapter
        content_list_rcview!!.setHasFixedSize(true)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        content_list_rcview!!.layoutManager = linearLayoutManager
    }

    private fun initializePlaybackController() {
        val mMediaPlayerHolder = MediaPlayerHolder(this)
        LogD("initializePlaybackController: created MediaPlayerHolder")
        mMediaPlayerHolder.setPlaybackInfoListener(PlaybackListener())
        mPlayerAdapter = mMediaPlayerHolder
        LogD("initializePlaybackController: MediaPlayerHolder progress callback set")
    }

    private fun setupOnClickListener(){
        nav_list_rcview.addOnItemTouchListener(
            RecyclerItemClickListener(this@MainActivity, nav_list_rcview, object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    // do whatever
                    checkNavList(position)
                    activity_main_drawer.closeDrawer()
                    mPlayerAdapter!!.reset()
                    mp3_play_title_txw.text = resources.getString(R.string.mp3_play_title_txw)
                    mp3_play_pause_imgbtn.setImageResource(R.drawable.ic_play_dark)
                }

                override fun onLongItemClick(view: View?, position: Int) {
                    // do whatever
                }
            })
        )

        content_list_rcview.addOnItemTouchListener(
            RecyclerItemClickListener(this@MainActivity,content_list_rcview, object : RecyclerItemClickListener.OnItemClickListener{
                override fun onItemClick(view: View, position: Int) {
                    checkContentList(position)
                }

                override fun onLongItemClick(view: View?, position: Int) {

                }
            })
        )

        mp3_play_pause_imgbtn.setOnClickListener {

            if (mPlayerAdapter!!.isPlaying()) {
                mp3_play_pause_imgbtn.setImageResource(R.drawable.ic_play_dark)
                mPlayerAdapter!!.pause()
            }else {

                mp3_play_pause_imgbtn.setImageResource(R.drawable.ic_pause_dark)
                mPlayerAdapter!!.play()
            }
        }

        mp3_play_previous_imgbtn.setOnClickListener {
            checkPlayPreviousOrNext(PLAY_PREVIOUS)
        }

        mp3_play_next_imgbtn.setOnClickListener {
            checkPlayPreviousOrNext(PLAY_NEXT)
        }

        mp3_play_seekbar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                internal var userSelectedPosition = 0

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    mUserIsSeeking = true
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        userSelectedPosition = progress
                    }
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    mUserIsSeeking = false
                    mPlayerAdapter!!.seekTo(userSelectedPosition)
                }
            })

    }

    private fun checkNavList(position: Int){
        var contentRVItem = GetResources.checkNavList(this@MainActivity,position)
        contentStrList = contentRVItem.strList
        contentNumList = contentRVItem.numList
        currentListLength = contentNumList!!.size
        contentListRVAdapter!!.setContentList(contentStrList!!)
        content_list_rcview.visibility = View.VISIBLE
        content_title_txw.text = chpaterList!![position]
    }

    private fun checkContentList(position: Int){
        mp3_play_title_txw.text = contentStrList!![position]
        currentSelectedNum = position
        playMp3File(position)
    }

    private fun checkPlayPreviousOrNext(playType: Int){
        when(playType){
            PLAY_PREVIOUS->{
                if (currentSelectedNum > 0){
                    currentSelectedNum--
                }else{
                    currentSelectedNum = currentListLength - 1
                }
            }
            PLAY_NEXT->{
                if (currentSelectedNum < currentListLength - 1){
                    currentSelectedNum++
                }else{
                    currentSelectedNum = 0
                }
            }
        }
        mp3_play_title_txw.text = contentStrList!![currentSelectedNum]
        playMp3File(currentSelectedNum)
    }

    private fun playMp3File(position: Int){
        val mp3nameStr = contentNumList!![position]
        val mp3Name = resources.getIdentifier("$mp3nameStr",
            "raw", packageName)
        mPlayerAdapter!!.loadMedia(mp3Name)
        mPlayerAdapter!!.reset()
        mPlayerAdapter!!.play()
        mp3_play_pause_imgbtn.setImageResource(R.drawable.ic_pause_dark)
    }

    override fun onStop() {
        super.onStop()
        if (isChangingConfigurations && mPlayerAdapter!!.isPlaying()) {
            LogD("onStop: don't release MediaPlayer as screen is rotating & playing")
        } else {
            mPlayerAdapter!!.release()
            LogD("onStop: release MediaPlayer")
        }
    }

    inner class PlaybackListener : PlaybackInfoListener() {

        override fun onDurationChanged(duration: Int) {
            mp3_play_seekbar.setMax(duration)
            LogD(String.format("setPlaybackDuration: setMax(%d)", duration))
        }

        override fun onPositionChanged(position: Int) {
            if (!mUserIsSeeking) {
                mp3_play_seekbar.setProgress(position, true)
                LogD(String.format("setPlaybackPosition: setProgress(%d)", position))
            }
        }

        override fun onStateChanged(@State state: Int) {
            val stateToString = PlaybackInfoListener.convertStateToString(state)
            onLogUpdated(String.format("onStateChanged(%s)", stateToString))
        }

        override fun onPlaybackCompleted() {}

        override fun onLogUpdated(message: String) {

        }
    }

}
