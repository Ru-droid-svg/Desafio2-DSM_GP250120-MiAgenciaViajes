package com.example.comrudygagenciaviajes
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val tvError = findViewById<TextView>(R.id.tvError)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvIrRegistro = findViewById<TextView>(R.id.tvIrRegistro)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                tvError.text = getString(R.string.error_campos_vacios)
                tvError.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    startActivity(Intent(this, CatalogoActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    tvError.text = it.message ?: getString(R.string.error_login)
                    tvError.visibility = TextView.VISIBLE
                }
        }

        tvIrRegistro.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(Intent(this, CatalogoActivity::class.java))
            finish()
        }
    }
}