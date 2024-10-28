package com.camilo.helpexpress.ui.task


import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.about.AboutActivity
import com.camilo.helpexpress.ui.adapter.TaskAdapter
import com.camilo.helpexpress.ui.chat.CreateChatActivity
import com.camilo.helpexpress.ui.entity.Task
import com.camilo.helpexpress.ui.fragment.ChatsFragment
import com.camilo.helpexpress.ui.fragment.HomeFragment
import com.camilo.helpexpress.ui.fragment.ProfileFragment
import com.camilo.helpexpress.ui.provider.TaskProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var createButton: FloatingActionButton
    private lateinit var mAuth: FirebaseAuth
    private lateinit var newChatButton: FloatingActionButton


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            createButton = findViewById<FloatingActionButton>(R.id.createTaskButton)
            newChatButton = findViewById<FloatingActionButton>(R.id.newChatButton)
            mAuth = FirebaseAuth.getInstance()
            bottomNavigation.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_home -> {
                        loadFragment(HomeFragment())
                        showCreateFloatingActionButton(true)
                        showNewFloatingActionButton(false)
                        true
                    }
                    R.id.menu_messages -> {
                        loadFragment(ChatsFragment())
                        showCreateFloatingActionButton(false)
                        showNewFloatingActionButton(true)
                        true
                    }
                    R.id.menu_profile -> {
                        loadFragment(ProfileFragment())
                        showCreateFloatingActionButton(false)
                        showNewFloatingActionButton(false)
                        true
                    }
                    else -> false
                }
            }

            if (savedInstanceState == null) {
                loadFragment(HomeFragment())
                bottomNavigation.selectedItemId = R.id.menu_home
                showCreateFloatingActionButton(true)
            }


            createButton.setOnClickListener {
                val intent= Intent(this, CreateTaskActivity::class.java)
                startActivity(intent)
            }

            newChatButton.setOnClickListener {
                val intent= Intent(this, CreateChatActivity::class.java)
                startActivity(intent)
            }
        }


        private fun loadFragment(fragment: Fragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }




        private fun showCreateFloatingActionButton(show: Boolean) {
            if (show) {
                createButton.show()
            } else {
                createButton.hide()
            }
        }

        private fun showNewFloatingActionButton(show: Boolean) {
        if (show) {
            newChatButton.show()
        } else {
            newChatButton.hide()
        }
    }

    }
