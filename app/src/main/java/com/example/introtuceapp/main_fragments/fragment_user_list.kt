package com.example.introtuceapp.main_fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.introtuceapp.R
import com.example.introtuceapp.adapter.UserRecylerAdapter
import com.example.introtuceapp.models.User
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.user_list_fragment.*

class fragment_user_list: Fragment(R.layout.user_list_fragment) {
private lateinit var adapter:UserRecylerAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        val db = Firebase.firestore
        val collection = db.collection("users").orderBy("created", Query.Direction.DESCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<User>().setQuery(collection,User::class.java).build()
        adapter = UserRecylerAdapter(recyclerViewOption)
        userList.adapter = adapter
        userList.layoutManager = LinearLayoutManager(context)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }
    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}