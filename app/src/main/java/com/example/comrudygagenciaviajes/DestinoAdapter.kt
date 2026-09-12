package com.example.comrudygagenciaviajes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

class DestinoAdapter(
    private var lista: List<Destino>,
    private val onClick: (Destino) -> Unit
) : RecyclerView.Adapter<DestinoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv: ImageView = view.findViewById(R.id.ivDestino)
        val nombre: TextView = view.findViewById(R.id.tvNombre)
        val precio: TextView = view.findViewById(R.id.tvPrecio)
        val descripcion: TextView = view.findViewById(R.id.tvDescripcion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_destino, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val destino = lista[position]
        holder.nombre.text = destino.nombre
        holder.precio.text = "$${destino.precio}"
        holder.descripcion.text = destino.descripcion
        Glide.with(holder.iv.context)
            .load(File(destino.imagenPath))
            .into(holder.iv)
        holder.itemView.setOnClickListener { onClick(destino) }
    }

    override fun getItemCount() = lista.size

    fun actualizarLista(nuevaLista: List<Destino>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}