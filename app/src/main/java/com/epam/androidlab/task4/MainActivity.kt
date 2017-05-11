package com.epam.androidlab.task4

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

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
    lateinit var oldParams: FrameLayout.LayoutParams
    lateinit var imageView: ImageView
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.image_container) as ImageView
        textView = findViewById(R.id.io_text) as TextView

        Glide.with(this).load("https://goo.gl/dnt4Lb").into(imageView)

        imageView.setOnLongClickListener {
            changeTextPosition()
            true
        }
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
        oldParams = textView.layoutParams as FrameLayout.LayoutParams
        oldGravityIndex = gravityStates.indexOf(oldParams.gravity)
    }

    fun updateTextPosition() {
        oldParams.gravity = gravityStates[newGravityIndex]
        textView.layoutParams = oldParams
    }

    override fun onSaveInstanceState(outState: Bundle) {
        getCurrentParams()
        outState.putInt(OLD_GRAVITY_INDEX_KEY, oldGravityIndex)
        super.onSaveInstanceState(outState)
    }
}
