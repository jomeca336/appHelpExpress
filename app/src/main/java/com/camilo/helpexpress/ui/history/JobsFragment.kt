package com.camilo.helpexpress.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.adapter.JobsAdapter
import com.camilo.helpexpress.ui.adapter.TaskAdapter
import com.camilo.helpexpress.ui.entity.Task
import com.google.firebase.Timestamp

class JobsFragment : Fragment() {

    private lateinit var jobsRecyclerView: RecyclerView
    private lateinit var taskAdapter: JobsAdapter
    private lateinit var taskList: MutableList<Task>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trabajos, container, false)

        // Inicializar la lista estática de trabajos


                taskList = mutableListOf(
            Task("1", "uid1", "Pintura de Casa", "Pintar la fachada de una casa", "Pintura", "Madrid", 150.0, true, Timestamp.now()), // Ahora
            Task("2", "uid2", "Reparación de Tubería", "Reparar una tubería en el baño", "Plomería", "Barcelona", 80.0, false, Timestamp(Timestamp.now().seconds - 1800, 0)), // Hace 30 minutos
            Task("3", "uid3", "Instalación de Electricidad", "Instalar electricidad en una nueva oficina", "Electricidad", "Valencia", 200.0, true, Timestamp(Timestamp.now().seconds - 7200, 0)), // Hace 2 horas
            Task("4", "uid4", "Reparación de Techo", "Reparar el techo dañado", "Construcción", "Sevilla", 300.0, false, Timestamp(Timestamp.now().seconds - 86400, 0)) // Hace 1 día
        )



        // Configurar el RecyclerView
        jobsRecyclerView = view.findViewById(R.id.recycler_jobs)
        jobsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        taskAdapter = JobsAdapter(taskList)
        jobsRecyclerView.adapter = taskAdapter

        return view
    }
}
