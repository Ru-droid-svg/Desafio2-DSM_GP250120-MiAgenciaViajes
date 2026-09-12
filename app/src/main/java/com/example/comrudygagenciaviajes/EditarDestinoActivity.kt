package com.example.comrudygagenciaviajes
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream

class EditarDestinoActivity : AppCompatActivity() {

    private lateinit var repo: FirestoreRepository
    private var destinoActual: Destino? = null
    private var nuevaImagenUri: Uri? = null

    private val seleccionarImagen = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            nuevaImagenUri = uri
            findViewById<ImageView>(R.id.ivPreview).setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_destino)

        repo = FirestoreRepository()
        val id = intent.getStringExtra("destino_id") ?: return

        val spinner = findViewById<Spinner>(R.id.spinnerPais)
        val adapterSpinner = ArrayAdapter.createFromResource(
            this, R.array.paises_array, android.R.layout.simple_spinner_item
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpinner

        FirebaseFirestore.getInstance().collection("destinos").document(id)
            .get().addOnSuccessListener { doc ->
                val destino = doc.toObject(Destino::class.java) ?: return@addOnSuccessListener
                destinoActual = destino
                findViewById<EditText>(R.id.etNombre).setText(destino.nombre)
                findViewById<EditText>(R.id.etPrecio).setText(destino.precio.toString())
                findViewById<EditText>(R.id.etDescripcion).setText(destino.descripcion)
                Glide.with(this).load(File(destino.imagenPath)).into(findViewById(R.id.ivPreview))
                val posicion = resources.getStringArray(R.array.paises_array).indexOf(destino.pais)
                if (posicion >= 0) spinner.setSelection(posicion)
            }

        findViewById<Button>(R.id.btnSeleccionarImagen).setOnClickListener {
            seleccionarImagen.launch("image/*")
        }

        findViewById<Button>(R.id.btnActualizar).setOnClickListener { actualizar() }
        findViewById<Button>(R.id.btnEliminar).setOnClickListener { confirmarEliminar() }
    }

    private fun actualizar() {
        val destino = destinoActual ?: return
        val tvError = findViewById<TextView>(R.id.tvErrorCrear)

        val nombre = findViewById<EditText>(R.id.etNombre).text.toString().trim()
        val precioTexto = findViewById<EditText>(R.id.etPrecio).text.toString().trim()
        val descripcion = findViewById<EditText>(R.id.etDescripcion).text.toString().trim()
        val pais = findViewById<Spinner>(R.id.spinnerPais).selectedItem.toString()

        if (nombre.isEmpty() || precioTexto.isEmpty() || descripcion.isEmpty()) {
            tvError.text = getString(R.string.error_campos_vacios); tvError.visibility = TextView.VISIBLE; return
        }
        val precio = precioTexto.toDoubleOrNull()
        if (precio == null || precio <= 0) {
            tvError.text = getString(R.string.error_precio_invalido); tvError.visibility = TextView.VISIBLE; return
        }
        if (descripcion.length < 20) {
            tvError.text = getString(R.string.error_descripcion_corta); tvError.visibility = TextView.VISIBLE; return
        }

        var pathImagen = destino.imagenPath
        if (nuevaImagenUri != null) {
            pathImagen = guardarImagenLocal(nuevaImagenUri!!)
        }

        val actualizado = destino.copy(
            nombre = nombre, pais = pais, precio = precio,
            descripcion = descripcion, imagenPath = pathImagen
        )

        repo.actualizarDestino(actualizado, onSuccess = { finish() }, onError = {
            tvError.text = it; tvError.visibility = TextView.VISIBLE
        })
    }

    private fun confirmarEliminar() {
        val destino = destinoActual ?: return
        AlertDialog.Builder(this)
            .setTitle(R.string.confirmar_eliminar_titulo)
            .setMessage(R.string.confirmar_eliminar_mensaje)
            .setPositiveButton(R.string.aceptar) { _, _ ->
                repo.eliminarDestino(destino.id, onSuccess = { finish() }, onError = {})
            }
            .setNegativeButton(R.string.cancelar, null)
            .show()
    }

    private fun guardarImagenLocal(uri: Uri): String {
        val inputStream = contentResolver.openInputStream(uri)
        val archivo = File(filesDir, "destino_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(archivo)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return archivo.absolutePath
    }
}