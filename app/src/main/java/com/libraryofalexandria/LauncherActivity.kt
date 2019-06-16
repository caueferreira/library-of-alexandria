package com.libraryofalexandria

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.libraryofalexandria.core.Activities
import com.libraryofalexandria.core.intentTo

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        startActivity(
            intentTo(Activities.Cards)
        )
    }
}