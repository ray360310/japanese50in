package com.rayhung.japanesemp3.Adapter.RV

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.rayhung.japanesemp3.Model.NavRVItem
import com.rayhung.japanesemp3.R

class NavMenuRVAdapter
    (private var navRVItemList: ArrayList<NavRVItem>): RecyclerView.Adapter<NavMenuViewHolder>() {

    fun setNavItemList(navRVItemList: ArrayList<NavRVItem>){
        this.navRVItemList.clear()
        this.navRVItemList.addAll(navRVItemList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: NavMenuViewHolder, position: Int) {
        if (navRVItemList != null) {
            if (navRVItemList.size != 0) {
                holder.bind(navRVItemList[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavMenuViewHolder {
        val mView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_main_nav_list_item, parent, false)
        return NavMenuViewHolder(mView)
    }

    override fun getItemCount(): Int {
        return navRVItemList.size
    }

}

class NavMenuViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val optionImg = itemView.findViewById<ImageView>(R.id.nav_list_item_option_img)
    val optionTxw = itemView.findViewById<TextView>(R.id.nav_list_item_option_txw)

    fun bind(item: NavRVItem){
        optionImg.setImageResource(item.resourceImg!!)
        optionTxw.text = item.option
    }

}