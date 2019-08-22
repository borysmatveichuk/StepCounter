package net.borkiss.stepcounter.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.borkiss.stepcounter.R
import net.borkiss.stepcounter.ui.main.MainFragment

fun createMainIntent(context: Context): Intent {
    return Intent(context, MainActivity::class.java)
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stat)

        val container = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (container == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MainFragment.newInstance())
                .commit()
        }
    }

}
