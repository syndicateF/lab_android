package com.fajrin222280062.kelas_c.kelascti2022

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class FakultasAdapter(
    private val context: Context,
    private val listFakultas: List<DataValue>,
    private val itemClickListener: ItemClickListener
): RecyclerView.Adapter<FakultasAdapter.FakultasViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FakultasAdapter.FakultasViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_fakultas, parent, false)
        return FakultasViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(
        holder: FakultasAdapter.FakultasViewHolder,
        position: Int
    ) {
        val value = listFakultas[position]
        holder.bind(value)
    }

    override fun getItemCount(): Int = listFakultas.size
    inner class FakultasViewHolder (
        itemView: View,
        private val itemClickListener: ItemClickListener
    ): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val linearFakultas = itemView.findViewById<LinearLayout>(R.id.LinearLayout)
        private val tvFakultas = itemView.findViewById<TextView>(R.id.tvFakultas)

        init {
            linearFakultas.setOnClickListener(this)
        }
        fun bind(value: DataValue) {

            tvFakultas.text = value.namaFakultas
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