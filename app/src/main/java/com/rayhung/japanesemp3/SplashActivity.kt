package com.rayhung.japanesemp3

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    private val SPLASH_MILLIS: Long = 550

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        intentToMainActivity()
    }

    private fun intentToMainActivity() {
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent()
            intent.setClass(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }, SPLASH_MILLIS)
    }

}
