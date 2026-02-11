package com.fajrin222280062.kelas_c.kelascti2022

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var swipeFakultas: SwipeRefreshLayout
    private lateinit var recyclerFakultas: RecyclerView
    private lateinit var adapter: FakultasAdapter
    private var fakultasAsli: List<DataValue> = listOf()
    private var fakultasTampil: List<DataValue> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initView(view)
        setupToolbar(view)
    }

    private fun setupToolbar(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Fakultas"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Cari fakultas..."
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
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun filterData(keyword: String) {
        if (keyword.isEmpty()) {
            tampilkanData(fakultasAsli)
            return
        }
        val hasil = fakultasAsli.filter { fakultas ->
            fakultas.namaFakultas?.contains(keyword, ignoreCase = true) == true
        }
        tampilkanData(hasil)
    }

    private fun initView(view: View) {
        swipeFakultas = view.findViewById(R.id.swipeFakultas)
        recyclerFakultas = view.findViewById(R.id.listFakultas)
        recyclerFakultas.layoutManager = GridLayoutManager(requireContext(), 2)
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
                            fakultasAsli = data
                            tampilkanData(data)
                        } else {
                            Toast.makeText(requireContext(), "Data kosong", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<DataValue>?>, t: Throwable) {
                    hideLoading()
                    Toast.makeText(requireContext(), "Gagal koneksi", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun showLoading() {
        swipeFakultas.isRefreshing = true
    }

    private fun hideLoading() {
        swipeFakultas.isRefreshing = false
    }

    private fun tampilkanData(data: List<DataValue>) {
        fakultasTampil = data
        adapter = FakultasAdapter(requireContext(), data, object : FakultasAdapter.ItemClickListener {
            override fun onItemClick(view: View?, adapterPosition: Int) {
                val fakultas = fakultasTampil[adapterPosition]
                val kode = fakultas.idFakultas
                val nama = fakultas.namaFakultas
                val intent = Intent(requireContext(), ProdiActivity::class.java)
                intent.putExtra("kode", kode)
                intent.putExtra("nama", nama)
                startActivity(intent)
            }
        })
        recyclerFakultas.adapter = adapter
    }
}
