package net.borkiss.stepcounter.ui.stat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.borkiss.stepcounter.R

fun goToStat(context: Context) {
    val intent = Intent(context, StatActivity::class.java)
    context.startActivity(intent)
}

class StatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stat)
    }
}
