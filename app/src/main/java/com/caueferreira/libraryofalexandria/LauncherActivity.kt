package com.caueferreira.libraryofalexandria

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        var intent = Intent(this, Class.forName("com.caueferreira.cards.view.activity.CardsActivity"))
        startActivity(intent)
    }
}
