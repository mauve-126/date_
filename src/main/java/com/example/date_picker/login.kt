package com.example.date_picker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseUser
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.firebase.auth.FirebaseAuth
import androidx.core.app.ComponentActivity.ExtraData
import android.util.Log
import android.widget.Toast
import android.widget.EditText
import com.google.firebase.auth.AuthResult
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.util.Patterns
import android.view.View
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern


class login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    // 이메일과 비밀번호
    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        var editTextEmail: EditText? =  findViewById(R.id.emailInput)
        var editTextPassword: EditText? =  findViewById(R.id.passwordInput)

        signupButton.setOnClickListener{
            email = editTextEmail?.getText().toString()
            password = editTextPassword?.getText().toString()
            createUser(email, password)
        }


    }

    // 회원가입
    private fun createUser(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful()) {
                        // 회원가입 성공
                        Toast.makeText(
                            this@login,
                            "successs",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // 회원가입 실패
                        Toast.makeText(
                            this@login,
                           "failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

}
