package com.camilo.helpexpress.ui.adapter

import android.content.Context
import android.graphics.Color
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.entity.Task

class TaskAdapter(private val context: Context, private var tasks: MutableList<Task>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val categoryTextView: TextView = view.findViewById(R.id.categoryTextView)
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        val locationTextView: TextView = view.findViewById(R.id.locationTextView)
        val priceTextView: TextView = view.findViewById(R.id.priceTextView)
        val applyButton: TextView = view.findViewById(R.id.applyButton)
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)  // Añadimos timeTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = task.title
        holder.categoryTextView.text = task.category
        holder.descriptionTextView.text = task.description
        holder.locationTextView.text = task.location
        holder.priceTextView.text = task.price.toString()

        // Mostrar el tiempo relativo (por ejemplo, "hace 2 minutos")
        val now = System.currentTimeMillis()
        val relativeTime = DateUtils.getRelativeTimeSpanString(task.timestamp.seconds * 1000, now, DateUtils.MINUTE_IN_MILLIS)
        holder.timeTextView.text = relativeTime  // Mostrar el tiempo relativo

        holder.applyButton.setOnClickListener {
            holder.applyButton.text = "Postulado"
            holder.applyButton.isEnabled = false
            holder.applyButton.setBackgroundColor(Color.GRAY)
            Toast.makeText(context, "Postulado con éxito", Toast.LENGTH_SHORT).show()
        }
    }

    // Ordenar las tareas por fecha (timestamp) de manera descendente
    fun sortTasksByDate() {
        tasks.sortByDescending { it.timestamp.seconds }  // Ordenamos por el campo timestamp
        notifyDataSetChanged()  // Notificamos al adaptador que los datos han cambiado
    }

    // Llamar esta función después de actualizar la lista de tareas
    init {
        sortTasksByDate()  // Ordenar cuando se inicializa el adaptador
    }
}
