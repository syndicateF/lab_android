package com.fajrin222280062.kelas_c.kelascti2022

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FakultasActivity : AppCompatActivity() {
    private lateinit var swipeFakultas: SwipeRefreshLayout
    private lateinit var recyclerFakultas: RecyclerView
    private lateinit var adapter: FakultasAdapter
    private var fakultasList: List<DataValue> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fakultas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
        setupToolbar()
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Fakultas"
    }

    private fun initView() {
        swipeFakultas = findViewById(R.id.swipeFakultas)
        recyclerFakultas = findViewById(R.id.listFakultas)
        recyclerFakultas.layoutManager = GridLayoutManager(this, 2)
        recyclerFakultas.setHasFixedSize(true)

        ambilDataFakultas()

        swipeFakultas.setOnRefreshListener {
            ambilDataFakultas()
        }
    }

    private fun ambilDataFakultas() {
        showLoading()
        val client = ApiClient.getApiClient().create(ApiInterface::class.java)
        val call = client.getDaftarFakultas()
        call.enqueue (
            object : Callback<List<DataValue>> {
                override fun onResponse(
                    call: Call<List<DataValue>?>,
                    response: Response<List<DataValue>?>
                ) {
                    hideLoading()
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            onGetFakultas(data)
                        } else {
                            Toast.makeText(this@FakultasActivity, "Data kosong", Toast.LENGTH_SHORT)
                                .show(
                                )
                        }
                    } else {
                        Toast.makeText(
                            this@FakultasActivity,
                            "Gagal mengambil data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<DataValue>?>,
                    t: Throwable
                ) {
                    Toast.makeText(this@FakultasActivity, "Gagal koneksi", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )
    }

    private fun showLoading(){
        swipeFakultas.isRefreshing = true
    }

    private fun hideLoading(){
        swipeFakultas.isRefreshing = false
    }

    private fun onGetFakultas(data: List<DataValue>) {
        fakultasList = data
        adapter = FakultasAdapter(this, data, object : FakultasAdapter.ItemClickListener {
            override fun onItemClick(view: View?, adapterPosition: Int) {
                val fakultas = fakultasList[adapterPosition]
                val kode = fakultas.idFakultas
                val nama = fakultas.namaFakultas
//                Toast.makeText(this@FakultasActivity, "Anda memilih $nama dengan kode $kode", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@FakultasActivity, ProdiActivity::class.java)
                intent.putExtra("kode", kode)
                intent.putExtra("nama", nama)
                startActivity(intent)
            }

        })
        recyclerFakultas.adapter = adapter
    }
}