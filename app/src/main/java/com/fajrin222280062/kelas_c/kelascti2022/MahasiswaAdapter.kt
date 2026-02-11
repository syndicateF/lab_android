package com.fajrin222280062.kelas_c.kelascti2022

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MahasiswaAdapter(
    private val context: Context,
    private val listMahasiswa: List<DataValue>,
    private val listener: ActionListener
): RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MahasiswaAdapter.MahasiswaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_mahasiswa, parent, false)
        return MahasiswaViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MahasiswaAdapter.MahasiswaViewHolder,
        position: Int
    ) {
        val mahasiswa = listMahasiswa[position]
        holder.bind(mahasiswa)
    }

    override fun getItemCount(): Int = listMahasiswa.size

    inner class MahasiswaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvNim = itemView.findViewById<TextView>(R.id.tvNim)
        private val tvNama = itemView.findViewById<TextView>(R.id.tvNama)
        private val tvKelas = itemView.findViewById<TextView>(R.id.tvKelas)
        private val tvGender = itemView.findViewById<TextView>(R.id.tvGender)
        private val btnUbah = itemView.findViewById<Button>(R.id.btnUbah)
        private val btnHapus = itemView.findViewById<Button>(R.id.btnHapus)

        fun bind(mahasiswa: DataValue) {
            tvNim.text = "NIM: ${mahasiswa.nim}"
            tvNama.text = mahasiswa.nama
            tvKelas.text = "Kelas: ${mahasiswa.kelas}"

            if (mahasiswa.jeniskelamin == "Perempuan") {
                tvGender.text = "👩"
            } else {
                tvGender.text = "👨"
            }

            btnUbah.setOnClickListener {
                listener.onUbahClick(mahasiswa)
            }

            btnHapus.setOnClickListener {
                listener.onHapusClick(mahasiswa)
            }
        }
    }

    interface ActionListener {
        fun onUbahClick(mahasiswa: DataValue)
        fun onHapusClick(mahasiswa: DataValue)
    }
}
