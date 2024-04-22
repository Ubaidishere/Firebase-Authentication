package com.example.loginsingup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginsingup.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth

class LoginPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.Create.setOnClickListener {
            startActivity(Intent(this, Create::class.java))
            finish()
        }

        binding.Singin.setOnClickListener {
            val email = binding.EmailText.text.toString().trim()
            val pass = binding.PassText.text.toString().trim()

            if (userNameAndPassChecker(email, pass)) {
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val userVerified = auth.currentUser?.isEmailVerified
                            if (userVerified == true) {
                                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@LoginPage, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "Please verify your email.", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            // Provide a generic error message for failed login attempts
                            Toast.makeText(this, "Invalid credentials. Please try again.", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        binding.Forget.setOnClickListener {
            startActivity(Intent(this, forgetPass::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun userNameAndPassChecker(email: String, pass: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            binding.EmailLayout.error = "Please enter the email"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.EmailLayout.error = "Please enter a valid email"
            isValid = false
        } else {
            binding.EmailLayout.error = null
        }

        if (pass.length < 8) {
            binding.PassLayput.error = "Password must be at least eight characters"
            isValid = false
        } else {
            binding.PassLayput.error = null
        }

        return isValid
    }
}
