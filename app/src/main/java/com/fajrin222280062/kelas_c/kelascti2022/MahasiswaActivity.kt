package com.fajrin222280062.kelas_c.kelascti2022

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MahasiswaActivity : AppCompatActivity() {

    private lateinit var swipeMahasiswa: SwipeRefreshLayout
    private lateinit var recyclerMahasiswa: RecyclerView
    private lateinit var adapter: MahasiswaAdapter
    private var mahasiswaAsli: List<DataValue> = listOf()
    private var mahasiswaTampil: List<DataValue> = listOf()
    private lateinit var idFakultas: String
    private lateinit var idProdi: String
    private lateinit var namaProdi: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mahasiswa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
        setupToolbar()
    }

    override fun onResume() {
        super.onResume()
        // Refresh data saat kembali dari halaman edit
        ambilDataMahasiswa()
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Mahasiswa"
        supportActionBar?.subtitle = namaProdi
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Cari mahasiswa..."
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
            tampilkanData(mahasiswaAsli)
            return
        }
        val hasil = mahasiswaAsli.filter { mahasiswa ->
            mahasiswa.nama?.contains(keyword, ignoreCase = true) == true ||
            mahasiswa.nim?.contains(keyword, ignoreCase = true) == true
        }
        tampilkanData(hasil)
    }

    private fun initView() {
        swipeMahasiswa = findViewById(R.id.swipeMahasiswa)
        recyclerMahasiswa = findViewById(R.id.listMahasiswa)
        val intent = intent
        idFakultas = intent.getStringExtra("idFakultas").toString()
        idProdi = intent.getStringExtra("idProdi").toString()
        namaProdi = intent.getStringExtra("namaProdi").toString()

        recyclerMahasiswa.layoutManager = LinearLayoutManager(this)
        recyclerMahasiswa.setHasFixedSize(true)

        swipeMahasiswa.setOnRefreshListener {
            ambilDataMahasiswa()
        }
    }

    private fun ambilDataMahasiswa() {
        showLoading()
        val client = ApiClient.getApiClient().create(ApiInterface::class.java)
        val call = client.getDaftarMahasiswa(idFakultas, idProdi)
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
                            mahasiswaAsli = data
                            tampilkanData(data)
                        } else {
                            Toast.makeText(this@MahasiswaActivity, "Data kosong", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MahasiswaActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<DataValue>?>, t: Throwable) {
                    hideLoading()
                    Toast.makeText(this@MahasiswaActivity, "Gagal koneksi", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun showLoading() {
        swipeMahasiswa.isRefreshing = true
    }

    private fun hideLoading() {
        swipeMahasiswa.isRefreshing = false
    }

    private fun tampilkanData(data: List<DataValue>) {
        mahasiswaTampil = data
        adapter = MahasiswaAdapter(this, data, object : MahasiswaAdapter.ActionListener {

            
            override fun onUbahClick(mahasiswa: DataValue) {
                val intent = Intent(this@MahasiswaActivity, EditMahasiswaActivity::class.java)
                intent.putExtra("nim", mahasiswa.nim)
                intent.putExtra("nama", mahasiswa.nama)
                intent.putExtra("kelas", mahasiswa.kelas)
                intent.putExtra("nohp", mahasiswa.nohp)
                intent.putExtra("jeniskelamin", mahasiswa.jeniskelamin)
                intent.putExtra("tempatLahir", mahasiswa.tempatLahir)
                intent.putExtra("tanggalLahir", mahasiswa.tanggalLahir)
                intent.putExtra("alamat", mahasiswa.alamat)
                intent.putExtra("idFakultas", idFakultas)
                intent.putExtra("idProdi", idProdi)
                startActivity(intent)
            }

            override fun onHapusClick(mahasiswa: DataValue) {
                AlertDialog.Builder(this@MahasiswaActivity)
                    .setTitle("Hapus Mahasiswa")
                    .setMessage("Yakin ingin menghapus ${mahasiswa.nama}?")
                    .setPositiveButton("Ya") { _, _ ->
                        hapusMahasiswa(mahasiswa.nim ?: "")
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        })
        recyclerMahasiswa.adapter = adapter
    }

    private fun hapusMahasiswa(nim: String) {
        val client = ApiClient.getApiClient().create(ApiInterface::class.java)
        val call = client.hapusMahasiswa(nim)

        call.enqueue(object : Callback<DataValue> {
            override fun onResponse(call: Call<DataValue?>, response: Response<DataValue?>) {
                if (response.isSuccessful) {
                    val hasil = response.body()
                    if (hasil?.sukses == true) {
                        Toast.makeText(this@MahasiswaActivity, "Data berhasil dihapus!", Toast.LENGTH_SHORT).show()
                        ambilDataMahasiswa() 
                    } else {
                        Toast.makeText(this@MahasiswaActivity, hasil?.notif ?: "Gagal menghapus", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MahasiswaActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DataValue?>, t: Throwable) {
                Toast.makeText(this@MahasiswaActivity, "Gagal koneksi", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
