package com.example.introtuceapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.introtuceapp.R
import com.example.introtuceapp.models.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class UserRecylerAdapter(options: FirestoreRecyclerOptions<User>) : FirestoreRecyclerAdapter<User, UserRecylerAdapter.UserViewHolder>(
    options
) {
    class UserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val profileImage:ImageView =itemView.findViewById(R.id.profileImageItem)
        val imageButton: ImageButton =itemView.findViewById(R.id.imageButton)
        val name_item:TextView =itemView.findViewById(R.id.name_item)
        val email_item:TextView =itemView.findViewById(R.id.email_item)
        val address_item:TextView =itemView.findViewById(R.id.address_item)
        val phone_item:TextView =itemView.findViewById(R.id.phone_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.user_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
        holder.name_item.text = model.nameValue
        holder.email_item.text = model.emailValue
        holder.address_item.text = model.addressValue
        holder.phone_item.text = model.phoneValue
        Glide.with(holder.profileImage.context).load(model.profilePic).centerCrop().into(holder.profileImage)
        holder.imageButton.setOnClickListener {
            val ref=Firebase.storage.getReferenceFromUrl(model.profilePic).delete()
            val observableSnapshotArray: ObservableSnapshotArray<User> = snapshots
            val documentReference = observableSnapshotArray.getSnapshot(position).reference
            documentReference.delete()
            Toast.makeText(holder.imageButton.context, "Deleted", Toast.LENGTH_SHORT).show()
        }
    }
}