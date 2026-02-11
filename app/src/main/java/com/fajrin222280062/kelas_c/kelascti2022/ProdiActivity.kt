package com.fajrin222280062.kelas_c.kelascti2022

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdiActivity : AppCompatActivity() {

    private lateinit var swipeProdi: SwipeRefreshLayout
    private lateinit var recyclerProdi: RecyclerView
    private lateinit var adapter: ProdiAdapter
    private var prodiAsli: List<DataValue> = listOf()
    private var prodiTampil: List<DataValue> = listOf()
    private lateinit var kode: String
    private lateinit var nama: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_prodi)
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
        supportActionBar?.title = "Prodi"
        supportActionBar?.subtitle = nama
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Cari prodi..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterData(query ?: "")
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filterData(newText ?: "")
                return true
            }
        })
        return true
    }

    private fun filterData(keyword: String) {
        if (keyword.isEmpty()) {
            tampilkanData(prodiAsli)
            return
        }
        val hasil = prodiAsli.filter { prodi ->
            prodi.namaProdi?.contains(keyword, ignoreCase = true) == true
        }
        tampilkanData(hasil)
    }

    private fun initView() {
        swipeProdi = findViewById(R.id.swipeProdi)
        recyclerProdi = findViewById(R.id.listProdi)
        val intent = intent
        kode = intent.getStringExtra("kode").toString()
        nama = intent.getStringExtra("nama").toString()
        recyclerProdi.layoutManager = GridLayoutManager(this, 2)
        recyclerProdi.setHasFixedSize(true)
        ambilDataProdi(kode)
        swipeProdi.setOnRefreshListener {
            ambilDataProdi(kode)
        }
    }

    private fun ambilDataProdi(kode: String) {
        showLoading()
        val client = ApiClient.getApiClient().create(ApiInterface::class.java)
        val call = client.getDaftarProdi(kode)
        call.enqueue(
            object : Callback<List<DataValue>> {
                override fun onResponse(
                    call: Call<List<DataValue>?>,
                    response: Response<List<DataValue>?>
                ) {
                    hideLoading()
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            prodiAsli = data
                            tampilkanData(data)
                        } else {
                            Toast.makeText(this@ProdiActivity, "Data kosong", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ProdiActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<DataValue>?>, t: Throwable) {
                    hideLoading()
                    Toast.makeText(this@ProdiActivity, "Gagal koneksi", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun showLoading() {
        swipeProdi.isRefreshing = true
    }

    private fun hideLoading() {
        swipeProdi.isRefreshing = false
    }

    private fun tampilkanData(data: List<DataValue>) {
        prodiTampil = data
        adapter = ProdiAdapter(this, data, object : ProdiAdapter.ItemClickListener {
            override fun onItemClick(view: View?, adapterPosition: Int) {
                val prodi = prodiTampil[adapterPosition]
                val kodeFakultas = prodi.idFakultas
                val kodeProdi = prodi.idProdi
                val namaProdi = prodi.namaProdi
                val intent = Intent(this@ProdiActivity, MahasiswaActivity::class.java)
                intent.putExtra("idFakultas", kodeFakultas)
                intent.putExtra("idProdi", kodeProdi)
                intent.putExtra("namaProdi", namaProdi)
                startActivity(intent)
            }
        })
        recyclerProdi.adapter = adapter
    }
}
