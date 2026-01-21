package com.fajrin222280062.kelas_c.kelascti2022

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProdiAdapter(
    private val context: Context,
    private val listProdi: List<DataValue>,
    private val itemClickListener: ItemClickListener
): RecyclerView.Adapter<ProdiAdapter.ProdiViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProdiAdapter.ProdiViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_prodi, parent, false)
        return ProdiViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(
        holder: ProdiAdapter.ProdiViewHolder,
        position: Int
    ) {
        val value = listProdi[position]
        holder.bind(value)
    }

    override fun getItemCount(): Int = listProdi.size
    inner class ProdiViewHolder (
        itemView: View,
        private val itemClickListener: ItemClickListener
    ): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val linearProdi = itemView.findViewById<LinearLayout>(R.id.LinearLayout)
        private val tvProdi = itemView.findViewById<TextView>(R.id.tvProdi)

        init {
            linearProdi.setOnClickListener(this)
        }
        fun bind(value: DataValue) {

            tvProdi.text = value.namaProdi
        }
        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(
                    view = v,
                    adapterPosition
                )
            }
        }
    }
    interface ItemClickListener {
        fun onItemClick(view: View?, adapterPosition: Int)
    }
}
