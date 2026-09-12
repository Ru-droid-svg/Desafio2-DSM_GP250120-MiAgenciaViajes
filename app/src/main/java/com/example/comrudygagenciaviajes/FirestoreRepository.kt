package com.example.comrudygagenciaviajes
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("destinos")

    fun agregarDestino(destino: Destino, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val doc = coleccion.document()
        destino.id = doc.id
        doc.set(destino)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Error al guardar") }
    }

    fun escucharDestinos(onData: (List<Destino>) -> Unit): ListenerRegistration {
        return coleccion.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener
            val lista = snapshot.documents.mapNotNull { it.toObject(Destino::class.java) }
            onData(lista)
        }
    }

    fun actualizarDestino(destino: Destino, onSuccess: () -> Unit, onError: (String) -> Unit) {
        coleccion.document(destino.id).set(destino)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Error al actualizar") }
    }

    fun eliminarDestino(id: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        coleccion.document(id).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Error al eliminar") }
    }
}