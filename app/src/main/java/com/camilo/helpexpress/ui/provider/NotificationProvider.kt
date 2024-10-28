package com.camilo.helpexpress.ui.provider

import com.camilo.helpexpress.ui.entity.Notification
import com.camilo.helpexpress.ui.entity.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class NotificationProvider {

    val collection: CollectionReference = FirebaseFirestore.getInstance().collection("notifications")

    fun getNotifications(): CollectionReference {
        return collection
    }

    fun getNotificationsByUid(uid: String): Query {
        return collection.whereEqualTo("uid", uid)
    }

    fun addNotification(noti: Notification): com.google.android.gms.tasks.Task<Void>{

        val id = collection.document().id
        noti.id = id
        return collection.document(id).set(noti)

    }
}