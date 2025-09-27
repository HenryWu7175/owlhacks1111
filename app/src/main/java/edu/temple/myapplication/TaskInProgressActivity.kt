package edu.temple.myapplication

import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TaskInProgressActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var tvTimer: TextView
    private lateinit var progress: ProgressBar
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_in_progress)

        // Keep screen on during the session
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        tvStatus = findViewById(R.id.tvStatus)
        tvTimer  = findViewById(R.id.tvTimer)
        progress = findViewById(R.id.progress)

        // Fake loading for 2 seconds, then start the 10-min timer
        progress.postDelayed({
            tvStatus.text = "Session running…"
            progress.isIndeterminate = false
            progress.max = 600   // 600 ticks = 600 seconds (10 min)
            startTenMinuteTimer()
        }, 2000)
    }

    private fun startTenMinuteTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(600_000, 1_000) { // 10 min, tick every 1s
            override fun onTick(msLeft: Long) {
                val totalSec = (msLeft / 1000).toInt()
                val m = totalSec / 60
                val s = totalSec % 60
                tvTimer.text = String.format("%02d:%02d", m, s)
                val elapsed = 600 - totalSec
                progress.progress = elapsed
            }
            override fun onFinish() {
                tvTimer.text = "00:00"
                tvStatus.text = "Time’s up! Great effort 👏"
            }
        }.start()
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }
}