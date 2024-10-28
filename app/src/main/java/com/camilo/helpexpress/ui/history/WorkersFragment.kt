package com.camilo.helpexpress.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.adapter.ClientAdapter
import com.camilo.helpexpress.ui.entity.Client

class WorkersFragment:Fragment(){



    private lateinit var clientsRecyclerView: RecyclerView
    private lateinit var clientAdapter: ClientAdapter
    private lateinit var clientList: MutableList<Client>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trabajadores, container, false)

        // Crear una lista estática de clientes
        clientList = mutableListOf(
            Client("1", "Daniel Gonzales", "3218554267", "Daniel20G@gmail.com", "Sin puntuar"),
            Client("2", "Laura Pérez", "3124456798", "laura.perez@example.com", "Puntuado"),
            Client("3", "Carlos Méndez", "3147854692", "carlos.m@example.com", "Sin puntuar")
        )

        // Configurar el RecyclerView
        clientsRecyclerView = view.findViewById(R.id.recycler_clientes)
        clientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        clientAdapter = ClientAdapter(clientList)
        clientsRecyclerView.adapter = clientAdapter

        return view
    }
}