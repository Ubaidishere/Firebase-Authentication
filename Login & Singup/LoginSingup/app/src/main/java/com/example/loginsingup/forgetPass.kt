package com.example.loginsingup

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.loginsingup.databinding.ActivityForgetPassBinding
import com.google.firebase.auth.FirebaseAuth
import com.saadahmedev.popupdialog.PopupDialog

class forgetPass : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding : ActivityForgetPassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Singin.setOnClickListener {
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }

        auth = FirebaseAuth.getInstance()
        binding.reset.setOnClickListener {
            val email = binding.emailText.text.toString() // Move the email initialization here
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailLayout.error = "Please enter a valid Email"
            } else {
                binding.emailLayout.error = null
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            PopupDialog.getInstance(this@forgetPass)
                                .statusDialogBuilder()
                                .createSuccessDialog()
                                .setHeading("Password Reset")
                                .setDescription("A reset link has been sent to $email.")
                                .build(Dialog::dismiss)
                                .show()
                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}
