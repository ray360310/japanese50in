package com.rayhung.japanesemp3.Adapter.RV

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.rayhung.japanesemp3.R

class ContentListRVAdapter
    (private var contentList: MutableList<String>): RecyclerView.Adapter<ContentListViewHolder>(){

    fun setContentList(contentList: MutableList<String>){
        this.contentList.clear()
        this.contentList.addAll(contentList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ContentListViewHolder, position: Int) {
        if (contentList != null) {
            if (contentList.size != 0) {
                holder.bind(contentList[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentListViewHolder {
        val mView = LayoutInflater.from(parent.context)
            .inflate(R.layout.content_list_item, parent, false)
        return ContentListViewHolder(mView)
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

}

class ContentListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val optionImg = itemView.findViewById<ImageView>(R.id.content_list_item_status_img)
    val optionTxw = itemView.findViewById<TextView>(R.id.content_list_item_name_txw)

    fun bind(className: String){
        optionTxw.text = className
    }
}