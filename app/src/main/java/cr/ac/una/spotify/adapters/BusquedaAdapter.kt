package cr.ac.una.spotify.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.spotify.R
import cr.ac.una.spotify.entity.Busqueda

class BusquedaAdapter(
    private val context: Context,
    private var busquedas: List<Busqueda?>,
    private val onSelect: (String) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.busqueda_item,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return busquedas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = busquedas[position]
        item?.let { (holder as ViewHolder).bind(it) }
    }

    fun updateData(newData: ArrayList<Busqueda?>) {
        busquedas = newData
        if (newData.isNotEmpty())
            notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item = itemView.findViewById<TextView>(R.id.item)

        fun bind(busqueda: Busqueda) {
            item.setText(busqueda.texto)
            item.setOnClickListener {
                onSelect(busqueda.texto)
            }
        }
    }
}