package com.libraryofalexandria

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.libraryofalexandria.core.base.Activities
import com.libraryofalexandria.core.base.intentTo

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        startActivity(
            intentTo(Activities.Expansions.Main)
        )
    }
}