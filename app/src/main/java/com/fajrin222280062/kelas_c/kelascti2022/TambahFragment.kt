package com.fajrin222280062.kelas_c.kelascti2022

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahFragment : Fragment() {

    private lateinit var etNim: EditText
    private lateinit var etNama: EditText
    private lateinit var etKelas: EditText
    private lateinit var etNohp: EditText
    private lateinit var spinnerJenisKelamin: Spinner
    private lateinit var etTempatLahir: EditText
    private lateinit var etTanggalLahir: EditText
    private lateinit var etAlamat: EditText
    private lateinit var spinnerFakultas: Spinner
    private lateinit var spinnerProdi: Spinner
    private lateinit var btnSimpan: Button

    private var daftarFakultas: List<DataValue> = listOf()
    private var daftarProdi: List<DataValue> = listOf()

    private var jenisKelaminTerpilih: String = "Laki-laki"
    private var idFakultasTerpilih: String = ""
    private var idProdiTerpilih: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tambah, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupSpinnerJenisKelamin()
        ambilDataFakultas()
    }

    private fun initView(view: View) {
        etNim = view.findViewById(R.id.etNim)
        etNama = view.findViewById(R.id.etNama)
        etKelas = view.findViewById(R.id.etKelas)
        etNohp = view.findViewById(R.id.etNohp)
        spinnerJenisKelamin = view.findViewById(R.id.spinnerJenisKelamin)
        etTempatLahir = view.findViewById(R.id.etTempatLahir)
        etTanggalLahir = view.findViewById(R.id.etTanggalLahir)
        etAlamat = view.findViewById(R.id.etAlamat)
        spinnerFakultas = view.findViewById(R.id.spinnerFakultas)
        spinnerProdi = view.findViewById(R.id.spinnerProdi)
        btnSimpan = view.findViewById(R.id.btnSimpan)

        btnSimpan.setOnClickListener {
            simpanMahasiswa()
        }
    }

    private fun setupSpinnerJenisKelamin() {
        val jenisKelaminList = listOf("Laki-laki", "Perempuan")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, jenisKelaminList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerJenisKelamin.adapter = adapter

        spinnerJenisKelamin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                jenisKelaminTerpilih = jenisKelaminList[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun ambilDataFakultas() {
        val client = ApiClient.getApiClient().create(ApiInterface::class.java)
        val call = client.getDaftarFakultas()

        call.enqueue(object : Callback<List<DataValue>> {
            override fun onResponse(call: Call<List<DataValue>?>, response: Response<List<DataValue>?>) {
                if (response.isSuccessful) {
                    daftarFakultas = response.body() ?: listOf()
                    tampilkanSpinnerFakultas()
                }
            }

            override fun onFailure(call: Call<List<DataValue>?>, t: Throwable) {
                Toast.makeText(requireContext(), "Gagal ambil data fakultas", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun tampilkanSpinnerFakultas() {
        val namaFakultas = daftarFakultas.map { it.namaFakultas ?: "" }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, namaFakultas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFakultas.adapter = adapter

        spinnerFakultas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                idFakultasTerpilih = daftarFakultas[position].idFakultas ?: ""
                ambilDataProdi(idFakultasTerpilih)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun ambilDataProdi(idFakultas: String) {
        val client = ApiClient.getApiClient().create(ApiInterface::class.java)
        val call = client.getDaftarProdi(idFakultas)

        call.enqueue(object : Callback<List<DataValue>> {
            override fun onResponse(call: Call<List<DataValue>?>, response: Response<List<DataValue>?>) {
                if (response.isSuccessful) {
                    daftarProdi = response.body() ?: listOf()
                    tampilkanSpinnerProdi()
                }
            }

            override fun onFailure(call: Call<List<DataValue>?>, t: Throwable) {
                Toast.makeText(requireContext(), "Gagal ambil data prodi", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun tampilkanSpinnerProdi() {
        val namaProdi = daftarProdi.map { it.namaProdi ?: "" }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, namaProdi)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProdi.adapter = adapter

        spinnerProdi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                idProdiTerpilih = daftarProdi[position].idProdi ?: ""
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun simpanMahasiswa() {
        val nim = etNim.text.toString()
        val nama = etNama.text.toString()
        val kelas = etKelas.text.toString()
        val nohp = etNohp.text.toString()
        val tempatLahir = etTempatLahir.text.toString()
        val tanggalLahir = etTanggalLahir.text.toString()
        val alamat = etAlamat.text.toString()

        if (nim.isEmpty() || nama.isEmpty() || kelas.isEmpty()) {
            Toast.makeText(requireContext(), "NIM, Nama, dan Kelas wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        if (idFakultasTerpilih.isEmpty() || idProdiTerpilih.isEmpty()) {
            Toast.makeText(requireContext(), "Pilih Fakultas dan Prodi!", Toast.LENGTH_SHORT).show()
            return
        }

        val client = ApiClient.getApiClient().create(ApiInterface::class.java)
        val call = client.tambahMahasiswa(
            nim = nim,
            nama = nama,
            kelas = kelas,
            nohp = nohp,
            jeniskelamin = jenisKelaminTerpilih,
            tempatLahir = tempatLahir,
            tanggalLahir = tanggalLahir,
            alamat = alamat,
            idFakultas = idFakultasTerpilih,
            idProdi = idProdiTerpilih
        )

        call.enqueue(object : Callback<DataValue> {
            override fun onResponse(call: Call<DataValue?>, response: Response<DataValue?>) {
                if (response.isSuccessful) {
                    val hasil = response.body()
                    if (hasil?.sukses == true) {
                        Toast.makeText(requireContext(), "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        // Kosongkan form
                        etNim.text.clear()
                        etNama.text.clear()
                        etKelas.text.clear()
                        etNohp.text.clear()
                        etTempatLahir.text.clear()
                        etTanggalLahir.text.clear()
                        etAlamat.text.clear()
                    } else {
                        Toast.makeText(requireContext(), hasil?.notif ?: "Gagal menyimpan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DataValue?>, t: Throwable) {
                Toast.makeText(requireContext(), "Gagal koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
