package com.camilo.helpexpress.ui.adapter

import android.graphics.Color
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.entity.Task

class JobsAdapter(private val taskList: List<Task>) :
    RecyclerView.Adapter<JobsAdapter.JobViewHolder>() {

    class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val applyButton: Button = itemView.findViewById(R.id.applyButton)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_card, parent, false)
        return JobViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val task = taskList[position]
        holder.titleTextView.text = task.title
        holder.categoryTextView.text = task.category
        holder.descriptionTextView.text = task.description
        holder.locationTextView.text = task.location
        holder.priceTextView.text = "$${task.price}"  // Formato de precio

        // Convertir el Timestamp de Firebase a milisegundos
        val timestampMillis = task.timestamp.seconds * 1000 + task.timestamp.nanoseconds / 1000000
        val now = System.currentTimeMillis()

        val relativeTime = DateUtils.getRelativeTimeSpanString(timestampMillis, now, DateUtils.MINUTE_IN_MILLIS)
        holder.timeTextView.text = relativeTime  // Mostrar el tiempo relativo


        holder.applyButton.setText("Postulado")
        holder.applyButton.isEnabled = false
        holder.applyButton.setBackgroundColor(Color.BLUE)
        holder.applyButton.setTextColor(Color.WHITE)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}
