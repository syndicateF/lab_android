package com.fajrin222280062.kelas_c.kelascti2022

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditMahasiswaActivity : AppCompatActivity() {

    private lateinit var tvNim: TextView
    private lateinit var etNama: EditText
    private lateinit var etKelas: EditText
    private lateinit var etNohp: EditText
    private lateinit var spinnerJenisKelamin: Spinner
    private lateinit var etTempatLahir: EditText
    private lateinit var etTanggalLahir: EditText
    private lateinit var etAlamat: EditText
    private lateinit var btnSimpan: Button

    private lateinit var nim: String
    private lateinit var idFakultas: String
    private lateinit var idProdi: String

    private var jenisKelaminTerpilih: String = "Laki-laki"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_mahasiswa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
        isiDataDariIntent()
    }

    private fun initView() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        tvNim = findViewById(R.id.tvNim)
        etNama = findViewById(R.id.etNama)
        etKelas = findViewById(R.id.etKelas)
        etNohp = findViewById(R.id.etNohp)
        spinnerJenisKelamin = findViewById(R.id.spinnerJenisKelamin)
        etTempatLahir = findViewById(R.id.etTempatLahir)
        etTanggalLahir = findViewById(R.id.etTanggalLahir)
        etAlamat = findViewById(R.id.etAlamat)
        btnSimpan = findViewById(R.id.btnSimpan)

        setupSpinnerJenisKelamin()

        btnSimpan.setOnClickListener {
            simpanPerubahan()
        }
    }

    private fun setupSpinnerJenisKelamin() {
        val jenisKelaminList = listOf("Laki-laki", "Perempuan")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisKelaminList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerJenisKelamin.adapter = adapter

        spinnerJenisKelamin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                jenisKelaminTerpilih = jenisKelaminList[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun isiDataDariIntent() {
        nim = intent.getStringExtra("nim") ?: ""
        val nama = intent.getStringExtra("nama") ?: ""
        val kelas = intent.getStringExtra("kelas") ?: ""
        val nohp = intent.getStringExtra("nohp") ?: ""
        val jenisKelamin = intent.getStringExtra("jeniskelamin") ?: "Laki-laki"
        val tempatLahir = intent.getStringExtra("tempatLahir") ?: ""
        val tanggalLahir = intent.getStringExtra("tanggalLahir") ?: ""
        val alamat = intent.getStringExtra("alamat") ?: ""
        idFakultas = intent.getStringExtra("idFakultas") ?: ""
        idProdi = intent.getStringExtra("idProdi") ?: ""

        tvNim.text = nim
        etNama.setText(nama)
        etKelas.setText(kelas)
        etNohp.setText(nohp)
        etTempatLahir.setText(tempatLahir)
        etTanggalLahir.setText(tanggalLahir)
        etAlamat.setText(alamat)

        if (jenisKelamin == "Perempuan") {
            spinnerJenisKelamin.setSelection(1)
        } else {
            spinnerJenisKelamin.setSelection(0)
        }
    }

    private fun simpanPerubahan() {
        val nama = etNama.text.toString()
        val kelas = etKelas.text.toString()
        val nohp = etNohp.text.toString()
        val tempatLahir = etTempatLahir.text.toString()
        val tanggalLahir = etTanggalLahir.text.toString()
        val alamat = etAlamat.text.toString()

        if (nama.isEmpty() || kelas.isEmpty()) {
            Toast.makeText(this, "Nama dan Kelas wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        val client = ApiClient.getApiClient().create(ApiInterface::class.java)
        val call = client.ubahMahasiswa(
            nim = nim,
            nama = nama,
            kelas = kelas,
            nohp = nohp,
            jeniskelamin = jenisKelaminTerpilih,
            tempatLahir = tempatLahir,
            tanggalLahir = tanggalLahir,
            alamat = alamat,
            idFakultas = idFakultas,
            idProdi = idProdi
        )

        call.enqueue(object : Callback<DataValue> {
            override fun onResponse(call: Call<DataValue?>, response: Response<DataValue?>) {
                if (response.isSuccessful) {
                    val hasil = response.body()
                    if (hasil?.sukses == true) {
                        Toast.makeText(this@EditMahasiswaActivity, "Data berhasil diubah!", Toast.LENGTH_SHORT).show()
                        finish() // Kembali ke halaman sebelumnya
                    } else {
                        Toast.makeText(this@EditMahasiswaActivity, hasil?.notif ?: "Gagal mengubah", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@EditMahasiswaActivity, "Gagal mengubah data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DataValue?>, t: Throwable) {
                Toast.makeText(this@EditMahasiswaActivity, "Gagal koneksi", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
