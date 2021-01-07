package com.example.introtuceapp.main_fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.introtuceapp.R
import com.example.introtuceapp.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.*
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.user_add_fragment.*
import kotlinx.android.synthetic.main.user_add_fragment.profileImage
import kotlinx.android.synthetic.main.user_item.*
import java.net.URLEncoder
import java.util.*

class fragment_user_add : Fragment(R.layout.user_add_fragment) {

    private var profilePic: Uri? = null
    private val imageRef = Firebase.storage.reference
    private val db = Firebase.firestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonProfilePic.setOnClickListener {
            picSelect()
        }
        submit.setOnClickListener {
            writeNewUser()
        }

    }

    //picture selction
    private fun picSelect() {
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "image/*"
            startActivityForResult(it, 420)
        }
    }

    //pic selection intent result and setting of imageview
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 420) {
            if (resultCode == Activity.RESULT_OK) {
                profilePic = data?.data!!
                profileImage.setImageURI(profilePic)
            }
        }
    }

    //crete user
    private fun writeNewUser() {

        //input validation
        val emailValue: String = email.text.toString().trim()
        if (emailValue.isEmpty()) {
            email.error = "Email is required"
            email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            email.error = "Please enter a valid email"
            email.requestFocus()
            return
        }
        val nameValue: String = name.text.toString().trim()
        if (nameValue.isEmpty()) {
            name.error = "Name is required"
            name.requestFocus()
            return
        }
        val addressValue: String = address.text.toString().trim()
        if (addressValue.isEmpty()) {
            address.error = "Address is required"
            address.requestFocus()
            return
        }
        val phoneValue: String = phone.text.toString().trim()
        if (phoneValue.isEmpty() || phoneValue.length < 10 || phoneValue.length > 13) {
            phone.error = "Phone is required"
            phone.requestFocus()
            return
        }
        if (!Patterns.PHONE.matcher(phoneValue).matches()) {
            phone.error = "Please enter a valid email"
            phone.requestFocus()
            return
        }
        if (profilePic == null) {
            Toast.makeText(context, "Profile Pic Not Selected ", Toast.LENGTH_LONG)
                .show()
            return
        }
        //after successful validation calling upload function
        upload(nameValue, emailValue, addressValue, phoneValue)
    }

    //function to get extension of the selected picture
    private fun getExtension(fileName: String): String {
        val encoded: String = try {
            URLEncoder.encode(fileName, "UTF-8").replace("+", "%20")
        } catch (e: Exception) {
            fileName
        }

        return MimeTypeMap.getFileExtensionFromUrl(encoded).toLowerCase(Locale.ROOT)
    }

    //upload users first upload pic and after the completion of upload crete user with the uploaded pic
    private fun upload(
        nameValue: String,
        emailValue: String,
        addressValue: String,
        phoneValue: String
    ) {
        val pd = ProgressDialog(context)
        pd.setTitle(" Uploading")
        pd.setMessage("Please wait")
        pd.show()
        profilePic?.let {
            profilePic
            println(profilePic.toString())
            val ref =
                imageRef.child("images/${System.currentTimeMillis()}.${getExtension(profilePic.toString())}")
            val uploadTask: StorageTask<*>
            uploadTask = ref.putFile(profilePic!!)
            uploadTask.continueWithTask Continuation@{ task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        val user = User(
                            nameValue,
                            emailValue,
                            addressValue,
                            phoneValue,
                            downloadUri.toString(),
                            System.currentTimeMillis()
                        )
                        db.collection("users").document(phoneValue).set(user)
                            .addOnSuccessListener {
                                pd.dismiss()
                                view?.let { it1 ->
                                    Snackbar.make(
                                        it1,
                                        "User added",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }
                                Toast.makeText(context, "User added", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                pd.dismiss()
                                view?.let { it1 ->
                                    Snackbar.make(
                                        it1,
                                        "{${it.message}}",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                            }

                    } else {
                        pd.dismiss()
                        view?.let { it1 ->
                            Snackbar.make(
                                it1,
                                "Error ${task.exception?.message}",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        Toast.makeText(
                            context,
                            "Error ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

//            imageRef.child("images/$phoneValue/${profilePic!!.lastPathSegment}")
//               .putFile(profilePic!!)
//                .addOnSuccessListener {
//                    imageRef.downloadUrl.addOnSuccessListener {
//                        val user =
//                            User(nameValue, emailValue, addressValue, phoneValue, it.toString())
//                        db?.collection("users")?.document(phoneValue)?.set(user)
//                            ?.addOnSuccessListener {
//                                pd.dismiss()
//                                Toast.makeText(context, "User added", Toast.LENGTH_SHORT).show()
//                            }
//                            ?.addOnFailureListener {
//                                pd.dismiss()
//                                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
//                            }
//
//                    }
//                        .addOnFailureListener {
//                            pd.dismiss()
//                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
//                        }
//                }
//                .addOnFailureListener {
//                    pd.dismiss()
//                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
//                }
//                .addOnProgressListener { snapshot ->
//                    val percent: Int =
//                        (100 * snapshot.bytesTransferred / snapshot.totalByteCount).toInt()
//                    pd.setMessage("$percent % uploaded")
//                }
        }
    }
}

