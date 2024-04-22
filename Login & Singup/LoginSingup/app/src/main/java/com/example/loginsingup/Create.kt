package com.example.loginsingup

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginsingup.databinding.ActivityCreateBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.saadahmedev.popupdialog.PopupDialog

class Create : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        binding.textView6.setOnClickListener {
            startActivity(Intent(this,LoginPage::class.java))
            finish()
        }
        binding.Create.setOnClickListener {
            val userName = binding.UserText.text.toString()
            val Email = binding.EmailText.text.toString()
            val Pass = binding.PassText.text.toString()
            val cnfrmPass = binding.rePassText.text.toString()

            if (inputChecker(userName, Email, Pass, cnfrmPass)) {
                auth.createUserWithEmailAndPassword(Email, Pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            auth.currentUser?.sendEmailVerification()
                                ?.addOnSuccessListener {
                                    PopupDialog.getInstance(this@Create)
                                        .statusDialogBuilder()
                                        .createSuccessDialog()
                                        .setHeading("Registration successfully done")
                                        .setDescription("A verification link has been sent to $Email.")
                                        .build(Dialog::dismiss)
                                        .show();
                                    saveData(userName,Email)
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        startActivity(Intent(this,LoginPage::class.java))
                                        finish()
                                    }, 3000)
                                }
                                ?.addOnFailureListener {
                                    Toast.makeText(
                                        this,
                                        "Error: ${task.exception?.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            Toast.makeText(
                                this,
                                "Error: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    private fun saveData(userName: String,Email: String) {
        val user = userData(userName,Email)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("userInfo").child(uid).setValue(user)
    }

    private fun inputChecker(
        userName: String,
        Email: String,
        Pass: String,
        cnfrmPass: String
    ): Boolean {
        var isValid = true

        if (userName.isEmpty()) {
            binding.UserLayout.error = "Please Enter User Name"
            isValid = false
        } else if (userName.length < 6) {
            binding.UserLayout.error = "Username must consist of at least six characters"
            isValid = false
        } else {
            binding.UserLayout.error = null
        }

        if (Email.isEmpty()) {
            binding.EmailLayout.error = "Please enter the Email"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            binding.EmailLayout.error = "Please enter a valid Email"
            isValid = false
        } else {
            binding.EmailLayout.error = null
        }

        if (Pass.isEmpty()) {
            binding.PassLayput.error = "Please enter the password"
            isValid = false
        } else if (Pass.length < 8) {
            binding.PassLayput.error = "Password must be at least eight characters"
            isValid = false
        } else {
            binding.PassLayput.error = null
        }

        if (cnfrmPass.isEmpty()) {
            binding.rePassLayput.error = "Please confirm the password"
            isValid = false
        } else if (Pass != cnfrmPass) {
            binding.rePassLayput.error = "Passwords do not match"
            isValid = false
        } else {
            binding.rePassLayput.error = null
        }

        return isValid
    }
}
