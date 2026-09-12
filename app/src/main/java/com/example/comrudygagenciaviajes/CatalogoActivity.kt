package com.tunombre.agenciaviajes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ListenerRegistration

class CatalogoActivity : AppCompatActivity() {

    private lateinit var repo: FirestoreRepository
    private lateinit var adapter: DestinoAdapter
    private var listener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)

        repo = FirestoreRepository()

        val rv = findViewById<RecyclerView>(R.id.rvDestinos)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = DestinoAdapter(emptyList()) { destino ->
            val intent = Intent(this, EditarDestinoActivity::class.java)
            intent.putExtra("destino_id", destino.id)
            startActivity(intent)
        }
        rv.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAgregar).setOnClickListener {
            startActivity(Intent(this, CrearDestinoActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        listener = repo.escucharDestinos { lista ->
            adapter.actualizarLista(lista)
        }
    }

    override fun onStop() {
        super.onStop()
        listener?.remove()
    }
}