package com.emre.datatypeskotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(var context:Context?=null,var list:List<Data>?=null): RecyclerView.Adapter<ItemHolder>() {



    override fun getItemCount(): Int {
        return list?.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_card,parent,false)
        return ItemHolder(v)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list?.get(position)
        holder.type?.text = data?.type
        holder.value?.text = data?.value
    }
}

class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var type:TextView? = null
    var value:TextView? = null

    init {
        type = itemView.findViewById(R.id.type)
        value = itemView.findViewById(R.id.value)
    }
}