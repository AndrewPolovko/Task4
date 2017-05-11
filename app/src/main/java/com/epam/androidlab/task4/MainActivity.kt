package com.epam.androidlab.task4

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.joda.time.DateTime
import org.joda.time.Seconds

class MainActivity : AppCompatActivity() {

    val OLD_GRAVITY_INDEX_KEY = "${BuildConfig.APPLICATION_ID}_old_gravity_index"
    val gravityStates = intArrayOf(
            Gravity.TOP or Gravity.START,
            Gravity.TOP or Gravity.END,
            Gravity.BOTTOM or Gravity.END,
            Gravity.BOTTOM or Gravity.START
    )
    var oldGravityIndex: Int = 0
    var newGravityIndex: Int = 0
    var timeDiff: Int = 0
    var timeHandler = Handler()
    lateinit var oldParams: FrameLayout.LayoutParams
    lateinit var imageView: ImageView
    lateinit var helloTextView: TextView
    lateinit var timeLeftTextView: TextView
    lateinit var currentDate: DateTime
    lateinit var ioDate: DateTime
    lateinit var timeUpdater: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.image_container) as ImageView
        helloTextView = findViewById(R.id.hello_text_view) as TextView
        timeLeftTextView = findViewById(R.id.time_left_text_view) as TextView

        Glide.with(this).load("https://goo.gl/dnt4Lb").into(imageView)

        updateTime()

        imageView.setOnLongClickListener {
            changeTextPosition()
            true
        }
    }

    fun updateTime() {
        timeHandler = Handler()
        timeUpdater = Runnable {
            currentDate = DateTime()
            ioDate = DateTime(2017, 5, 17, 20, 0)
            timeDiff = Seconds.secondsBetween(currentDate, ioDate).seconds
            if (timeDiff < 0)
                timeLeftTextView.visibility = View.GONE
            else {
                timeLeftTextView.text = "Time left: ${
                String.format("%dd %dh %dm %ds",
                        timeDiff / (60 * 60 * 24),
                        (timeDiff / (60 * 60)) % 24,
                        (timeDiff / 60) % 60,
                        timeDiff % 60
                )
                }"
                timeHandler.postDelayed(timeUpdater, 1000)
            }
        }
        timeHandler.post(timeUpdater)
    }

    override fun onRestoreInstanceState(savedState: Bundle?) {
        savedState?.let {
            getCurrentParams()
            newGravityIndex = savedState.getInt(OLD_GRAVITY_INDEX_KEY)
            updateTextPosition()

        }
        super.onRestoreInstanceState(savedState)
    }

    fun changeTextPosition() {
        getCurrentParams()
        if (oldGravityIndex == gravityStates.size - 1)
            newGravityIndex = 0
        else
            newGravityIndex = oldGravityIndex + 1

        updateTextPosition()
    }

    fun getCurrentParams() {
        oldParams = helloTextView.layoutParams as FrameLayout.LayoutParams
        oldGravityIndex = gravityStates.indexOf(oldParams.gravity)
    }

    fun updateTextPosition() {
        oldParams.gravity = gravityStates[newGravityIndex]
        helloTextView.layoutParams = oldParams
    }

    override fun onSaveInstanceState(outState: Bundle) {
        getCurrentParams()
        outState.putInt(OLD_GRAVITY_INDEX_KEY, oldGravityIndex)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        timeHandler.removeCallbacks { timeUpdater }
        super.onDestroy()
    }
}
