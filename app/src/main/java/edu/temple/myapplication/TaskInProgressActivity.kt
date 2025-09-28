package edu.temple.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TaskInProgressActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var tvTimer: TextView
    private lateinit var progress: ProgressBar

    private lateinit var button : Button
    private var timer: CountDownTimer? = null

    private fun openUploadNewClean(zone: String) {
        val i = Intent(this, UploadNewClean::class.java)
        i.putExtra("zone", zone)
        startActivity(i)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_in_progress)

        // Keep screen awake during the session
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        tvStatus = findViewById(R.id.tvStatus)
        tvTimer  = findViewById(R.id.tvTimer)
        progress = findViewById(R.id.progress)
        button = findViewById(R.id.button)

        // Initial loading state (2s), then start the timer
        tvStatus.text = "Preparing your session…"
        progress.isIndeterminate = true

        // Small fake loading, then configure progress and start 10-min timer
        progress.postDelayed({
            tvStatus.text = "Session running…"
            progress.isIndeterminate = false
            progress.max = 600           // 600 seconds total (10 minutes)
            progress.progress = 0
            startTenMinuteTimer()
        }, 2000)

        button.setOnClickListener {
            stoptenMinuteTimer()
            openUploadNewClean("Bed") }
    }

    private fun startTenMinuteTimer() {
        timer?.cancel()
        // 600_000 ms = 10 minutes; tick every second
        timer = object : CountDownTimer(600_000, 1_000) {
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

                // After timer, send user to upload the AFTER photo
                val zone = intent.getStringExtra("zone") ?: "Unknown"
                startActivity(
                    Intent(this@TaskInProgressActivity, UploadActivity::class.java)
                        .putExtra("zone", zone)
                        .putExtra("expectAfter", true)
                )
                finish()
            }
        }.start()
    }
    private fun stoptenMinuteTimer() {
        timer?.cancel()
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }
}