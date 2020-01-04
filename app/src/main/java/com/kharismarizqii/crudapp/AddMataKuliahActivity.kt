package com.kharismarizqii.crudapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.*

class AddMataKuliahActivity : AppCompatActivity() {

    private lateinit var tvNama : TextView
    private lateinit var etMatkul : EditText
    private lateinit var etSks : EditText
    private lateinit var btnMatkul : Button
    private lateinit var lvMatkul : ListView
    private lateinit var matkulList : MutableList<MataKuliah>
    private lateinit var ref : DatabaseReference


    companion object{
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mata_kuliah)

        val id = intent.getStringExtra(EXTRA_ID)
        val nama = intent.getStringExtra(EXTRA_NAMA)

        matkulList = mutableListOf()

        ref = FirebaseDatabase.getInstance().getReference("mata_kuliah").child(id)

        tvNama = findViewById(R.id.tv_nama)
        etMatkul = findViewById(R.id.et_matkul)
        etSks = findViewById(R.id.et_sks)
        btnMatkul = findViewById(R.id.btn_matkul)
        lvMatkul = findViewById(R.id.lv_matkul)

        btnMatkul.setOnClickListener{
            saveMatkul()
        }
    }

    fun saveMatkul(){
        val namaMatkul = etMatkul.text.toString().trim()
        val sksText = etSks.text.toString().trim()
        val sks = sksText.toInt()

        if(namaMatkul.isEmpty()){
            etMatkul.error = "Matkul harus diisi"
            return
        }
        if(sksText.isEmpty()){
            etSks.error = "SKS harus diisi"
            return
        }

        val matkulId = ref.push().key

        val matkul = MataKuliah(matkulId!!,namaMatkul,sks)

        if (matkulId != null) {
            ref.child(matkulId).setValue(matkul).addOnCompleteListener{
                Toast.makeText(applicationContext, "Matakuliah berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }
        }

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    matkulList.clear()
                    for(h in p0.children){
                        val matakuliah = h.getValue(MataKuliah::class.java)
                        if (matakuliah != null) {
                            matkulList.add(matakuliah)
                        }
                    }

                    val adapter = MataKuliahAdapter(this@AddMataKuliahActivity, R.layout.item_matkul, matkulList)
                    lvMatkul.adapter = adapter
                }
            }

        })
    }
}
