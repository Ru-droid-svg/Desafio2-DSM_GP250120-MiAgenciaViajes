package com.example.comrudygagenciaviajes
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.FileOutputStream

class CrearDestinoActivity : AppCompatActivity() {

    private lateinit var repo: FirestoreRepository
    private var imagenUri: Uri? = null
    private var imagenGuardadaPath: String? = null

    private val seleccionarImagen = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imagenUri = uri
            findViewById<ImageView>(R.id.ivPreview).setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_destino)

        repo = FirestoreRepository()

        val spinner = findViewById<Spinner>(R.id.spinnerPais)
        val adapterSpinner = ArrayAdapter.createFromResource(
            this, R.array.paises_array, android.R.layout.simple_spinner_item
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpinner

        findViewById<Button>(R.id.btnSeleccionarImagen).setOnClickListener {
            seleccionarImagen.launch("image/*")
        }

        findViewById<Button>(R.id.btnGuardar).setOnClickListener {
            guardarDestino()
        }
    }

    private fun guardarDestino() {
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etPrecio = findViewById<EditText>(R.id.etPrecio)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val spinner = findViewById<Spinner>(R.id.spinnerPais)
        val tvError = findViewById<TextView>(R.id.tvErrorCrear)

        val nombre = etNombre.text.toString().trim()
        val precioTexto = etPrecio.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val pais = spinner.selectedItem.toString()

        // Validaciones obligatorias
        if (nombre.isEmpty() || precioTexto.isEmpty() || descripcion.isEmpty()) {
            mostrarError(tvError, getString(R.string.error_campos_vacios))
            return
        }
        val precio = precioTexto.toDoubleOrNull()
        if (precio == null || precio <= 0) {
            mostrarError(tvError, getString(R.string.error_precio_invalido))
            return
        }
        if (descripcion.length < 20) {
            mostrarError(tvError, getString(R.string.error_descripcion_corta))
            return
        }
        if (imagenUri == null) {
            mostrarError(tvError, getString(R.string.error_imagen_requerida))
            return
        }

        // Guardar imagen en almacenamiento local
        imagenGuardadaPath = guardarImagenLocal(imagenUri!!)

        val destino = Destino(
            nombre = nombre,
            pais = pais,
            precio = precio,
            descripcion = descripcion,
            imagenPath = imagenGuardadaPath ?: "",
            uidUsuario = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        )

        repo.agregarDestino(destino, onSuccess = {
            finish()
        }, onError = {
            mostrarError(tvError, it)
        })
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

    private fun mostrarError(tv: TextView, mensaje: String) {
        tv.text = mensaje
        tv.visibility = TextView.VISIBLE
    }
}