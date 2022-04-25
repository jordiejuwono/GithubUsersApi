package com.example.githubuserapp.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.githubuserapp.R
import com.example.githubuserapp.ui.main.GithubUserActivity

class SplashScreenActivity : AppCompatActivity() {

    companion object {
        const val millisInFuture = 2000L
        const val countDown = 1000L
    }

    private val timer: CountDownTimer by lazy {
        object: CountDownTimer(millisInFuture, countDown){
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                val intent = Intent(this@SplashScreenActivity, GithubUserActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        setSplashScreenTimer()
    }

    private fun setSplashScreenTimer() {
        timer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}