package com.rayhung.japanesemp3.Conf

import android.content.Context
import com.rayhung.japanesemp3.Model.ContentRVItem
import com.rayhung.japanesemp3.R

object GetResources {

    private val CHAPTER_01 = 0
    private val CHAPTER_02 = 1
    private val CHAPTER_03 = 2
    private val CHAPTER_04 = 3
    private val CHAPTER_05 = 4
    private val CHAPTER_PRACTICE_01 = 5
    private val CHAPTER_06 = 6
    private val CHAPTER_07 = 7
    private val CHAPTER_08 = 8
    private val CHAPTER_09 = 9
    private val CHAPTER_10 = 10
    private val CHAPTER_11 = 11
    private val CHAPTER_PRACTICE_02 = 12

    fun checkNavList(context: Context, position: Int): ContentRVItem{
        var strList: MutableList<String> ?= null
        var numList: MutableList<String> ?= null
        when(position){
            CHAPTER_01 -> {
                strList = (context.resources.getStringArray(R.array.mp3_chapter01_str)).toMutableList()
                numList = (context.resources.getStringArray(R.array.mp3_chapter01_num)).toMutableList()
            }
            CHAPTER_02 -> {
                strList = (context.resources.getStringArray(R.array.mp3_chapter02_str)).toMutableList()
                numList = (context.resources.getStringArray(R.array.mp3_chapter02_num)).toMutableList()
            }
            CHAPTER_03 -> {
                strList = (context.resources.getStringArray(R.array.mp3_chapter03_str)).toMutableList()
                numList = (context.resources.getStringArray(R.array.mp3_chapter03_num)).toMutableList()
            }
            CHAPTER_04 -> {
                strList = (context.resources.getStringArray(R.array.mp3_chapter04_str)).toMutableList()
                numList = (context.resources.getStringArray(R.array.mp3_chapter04_num)).toMutableList()
            }
            CHAPTER_05 -> {
                strList = (context.resources.getStringArray(R.array.mp3_chapter05_str)).toMutableList()
                numList = (context.resources.getStringArray(R.array.mp3_chapter05_num)).toMutableList()
            }
            CHAPTER_PRACTICE_01 -> {
                strList = (context.resources.getStringArray(R.array.mp3_special_practice_01_str)).toMutableList()
                numList = (context.resources.getStringArray(R.array.mp3_special_practice_01_str)).toMutableList()
            }
            CHAPTER_06 -> {
                strList = (context.resources.getStringArray(R.array.mp3_chapter06_str)).toMutableList()
                numList = (context.resources.getStringArray(R.array.mp3_chapter06_num)).toMutableList()
            }
            CHAPTER_07 -> {
                strList = (context.resources.getStringArray(R.array.mp3_chapter07_str)).toMutableList()
                numList = (context.resources.getStringArray(R.array.mp3_chapter07_num)).toMutableList()
            }
            CHAPTER_08 -> {
                strList = (context.resources.getStringArray(R.array.mp3_chapter08_str)).toMutableList()
                numList = (context.resources.getStringArray(R.array.mp3_chapter08_num)).toMutableList()
            }
            CHAPTER_09 -> {
                strList = (context.resources.getStringArray(R.array.mp3_chapter09_str)).toMutableList()
                numList = (context.resources.getStringArray(R.array.mp3_chapter09_num)).toMutableList()
            }
            CHAPTER_10 -> {
                strList = (context.resources.getStringArray(R.array.mp3_chapter10_str)).toMutableList()
                numList = (context.resources.getStringArray(R.array.mp3_chapter10_num)).toMutableList()
            }
            CHAPTER_11 -> {
                strList = (context.resources.getStringArray(R.array.mp3_chapter11_str)).toMutableList()
                numList = (context.resources.getStringArray(R.array.mp3_chapter11_num)).toMutableList()
            }
            CHAPTER_PRACTICE_02 -> {
                strList = (context.resources.getStringArray(R.array.mp3_special_practice_02_str)).toMutableList()
                numList = (context.resources.getStringArray(R.array.mp3_special_practice_02_str)).toMutableList()
            }
        }
        val contentRVItem = ContentRVItem()
        contentRVItem.strList = strList
        contentRVItem.numList = numList
        return contentRVItem
    }


}