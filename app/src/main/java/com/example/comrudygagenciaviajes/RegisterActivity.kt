package com.example.comrudygagenciaviajes
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val etEmail = findViewById<EditText>(R.id.etEmailReg)
        val etPassword = findViewById<EditText>(R.id.etPasswordReg)
        val tvError = findViewById<TextView>(R.id.tvErrorReg)
        val btnRegistro = findViewById<Button>(R.id.btnRegistro)
        val tvIrLogin = findViewById<TextView>(R.id.tvIrLogin)

        btnRegistro.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                tvError.text = getString(R.string.error_campos_vacios)
                tvError.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    startActivity(Intent(this, CatalogoActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    tvError.text = it.message
                    tvError.visibility = TextView.VISIBLE
                }
        }

        tvIrLogin.setOnClickListener { finish() }
    }
}