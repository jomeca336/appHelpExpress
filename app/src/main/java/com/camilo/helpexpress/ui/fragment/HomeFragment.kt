package com.camilo.helpexpress.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.about.AboutActivity
import com.camilo.helpexpress.ui.adapter.TaskAdapter
import com.camilo.helpexpress.ui.entity.Task
import com.camilo.helpexpress.ui.provider.TaskProvider
import com.camilo.helpexpress.ui.task.NotificationsActivity
import com.google.firebase.auth.FirebaseAuth

class HomeFragment:Fragment() {
    lateinit var mTaskProvider: TaskProvider
    lateinit var mAuth: FirebaseAuth
    lateinit var tasks: MutableList<Task>
    lateinit var adapter: TaskAdapter
    lateinit var progressBar: ProgressBar
    lateinit var linearLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        val notificationsImageView = view.findViewById<ImageView>(R.id.notificationsImageView)
        notificationsImageView.setOnClickListener {
            val intent = Intent(requireContext(), NotificationsActivity::class.java)
            startActivity(intent)
        }
        val aboutImageView = view.findViewById<ImageView>(R.id.aboutImageView)
        aboutImageView.setOnClickListener {
            val intent = Intent(requireContext(), AboutActivity::class.java)
            startActivity(intent)
        }
        mTaskProvider = TaskProvider()
        mAuth = FirebaseAuth.getInstance()
        tasks = mutableListOf()
        progressBar = view.findViewById(R.id.progressBar)
        linearLayout = view.findViewById(R.id.homeLinear)
        val tasksRecyclerView = view.findViewById<RecyclerView>(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = TaskAdapter(requireContext(), tasks)
        tasksRecyclerView.adapter = adapter



        loadTask()

        return view
    }





    private fun loadTask() {

        mTaskProvider.getTasks().get()
            .addOnSuccessListener { documents ->
                tasks.clear()
                for (document in documents) {
                    val task = document.toObject(Task::class.java)
                    tasks.add(task)
                }



                adapter.notifyDataSetChanged()

                progressBar.visibility = View.GONE
                linearLayout.visibility = View.VISIBLE

            }
            .addOnFailureListener{
                Toast.makeText(requireContext(), "Error al cargar notificaciones", Toast.LENGTH_SHORT).show()
            }
    }
}