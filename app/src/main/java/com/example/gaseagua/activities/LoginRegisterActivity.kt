package com.example.gaseagua.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.gaseagua.R
import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity

@AndroidEntryPoint
class LoginRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
    }
}
