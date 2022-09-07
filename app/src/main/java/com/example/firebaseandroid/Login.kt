package com.example.firebaseandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setup()
    }
    //private lateinit var auth: FirebaseAuth
    // Initialize Firebase Auth

    //var userId = ""


    private fun setup() {

        title = "Auntenticación"

        //auth = Firebase.auth

        //final
        registerButton.setOnClickListener{
            if (inputEmail.text.isNotEmpty() && inputPass.text.isNotEmpty()) {
                Firebase.auth.createUserWithEmailAndPassword(
                    inputEmail.text.toString(),
                    inputPass.text.toString()
                ).addOnCompleteListener(this) {
                    if(it.isSuccessful) {
                        val userId = Firebase.auth.currentUser?.uid ?: ""
                        //userId =
                        val userData = hashMapOf(
                            "id" to userId,
                            "email" to inputEmail.text.toString()
                        )
                        Firebase.firestore.collection("Users").document(userId).set(userData)
                        showProfile(it.result?.user?.email?: "", ProviderType.BASIC, userId)

                        Firebase.firestore.collection("Locations").document(userId).set(hashMapOf(
                            "user" to inputEmail.text.toString(),
                            "lat" to "",
                            "long" to ""

                        ))
                        inputEmail.text.clear()
                        inputPass.text.clear()
                    }
                    else {
                        showAlert()
                    }
                }
            } else {
                showAlert()
            }
        }

        loginButton.setOnClickListener{
            if (inputEmail.text.isNotEmpty() && inputPass.text.isNotEmpty()) {
                Firebase.auth.signInWithEmailAndPassword(
                    inputEmail.text.toString(),
                    inputPass.text.toString()
                ).addOnCompleteListener (this) {
                    if(it.isSuccessful) {
                        val userId = Firebase.auth.currentUser?.uid ?: ""
                        showProfile(it.result?.user?.email ?: "", ProviderType.BASIC, userId )
                        inputEmail.text.clear()
                        inputPass.text.clear()
                    }
                    else {
                        showAlert()
                    }
                }
            }
            else {
                showAlert()
            }
        }
    }

    private fun showAlert() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun showProfile(email: String, provider: ProviderType, userId: String) {

        val profileIntent = Intent(this, Profile::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
            putExtra("userId", userId)

        }
        startActivity(profileIntent)
    }

}