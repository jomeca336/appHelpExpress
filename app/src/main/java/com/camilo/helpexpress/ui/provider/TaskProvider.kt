package com.camilo.helpexpress.ui.provider

import com.camilo.helpexpress.ui.entity.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class TaskProvider {

    private var collection:CollectionReference = FirebaseFirestore.getInstance().collection("tasks")
    fun getTasks(): CollectionReference {
        return collection
    }

    fun addTask(task: Task): com.google.android.gms.tasks.Task<Void>{

        val id = collection.document().id
        task.id = id
        return collection.document(id).set(task)

    }
    fun getTasksByUid(uid: String): Query {
        return collection.whereEqualTo("uid", uid)
    }

    fun deleteTask(id: String): com.google.android.gms.tasks.Task<Void> {
        return collection.document(id).delete()
    }




}