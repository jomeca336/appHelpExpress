package com.camilo.helpexpress.ui.adapter

import android.app.AlertDialog
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
import com.camilo.helpexpress.ui.provider.TaskProvider

class AdminTaskAdapter(private val context: Context, private var tasks: MutableList<Task>): RecyclerView.Adapter<AdminTaskAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val categoryTextView: TextView = view.findViewById(R.id.categoryTextView)
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        val locationTextView: TextView = view.findViewById(R.id.locationTextView)
        val priceTextView: TextView = view.findViewById(R.id.priceTextView)
        val applyButton: TextView = view.findViewById(R.id.applyButton)
        val  timeTextView: TextView = view.findViewById(R.id.timeTextView)
    }

    var mTaskProvider: TaskProvider = TaskProvider()

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

        // Actualizar los textos en el card
        holder.titleTextView.text = task.title
        holder.categoryTextView.text = task.category
        holder.descriptionTextView.text = task.description
        holder.locationTextView.text = task.location
        holder.priceTextView.text = task.price.toString()

        holder.applyButton.text = "Eliminar publicación"
        holder.applyButton.isEnabled = true
        holder.applyButton.setBackgroundColor(Color.RED)

        // Convertir el Timestamp de Firebase a milisegundos
        val timestampMillis = task.timestamp.seconds * 1000 + task.timestamp.nanoseconds / 1000000
        val now = System.currentTimeMillis()

        // Mostrar el tiempo relativo (por ejemplo, "hace 2 minutos")
        val relativeTime = DateUtils.getRelativeTimeSpanString(timestampMillis, now, DateUtils.MINUTE_IN_MILLIS)
        holder.timeTextView.text = relativeTime  // Mostrar el tiempo relativo

        // Manejo de clic en el botón
        holder.applyButton.setOnClickListener {
            // Mostrar el diálogo de confirmación
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar Publicación")
            builder.setMessage("¿Estás seguro de que quieres eliminar esta publicación?")

            // Configurar botón "Sí"
            builder.setPositiveButton("Sí") { dialog, _ ->
                // Si el usuario confirma, eliminamos la tarea
                mTaskProvider.deleteTask(task.id).addOnSuccessListener {
                    Toast.makeText(context, "Publicación eliminada", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Error al eliminar la publicación", Toast.LENGTH_SHORT).show()
                }

                // Eliminar la tarea de la lista y notificar al adaptador
                tasks.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, tasks.size)

                dialog.dismiss()  // Cerrar el diálogo
            }

            // Configurar botón "No"
            builder.setNegativeButton("No") { dialog, _ ->
                // Si el usuario cancela, simplemente cerramos el diálogo
                dialog.dismiss()
            }

            // Mostrar el diálogo
            builder.show()
        }

    }

}