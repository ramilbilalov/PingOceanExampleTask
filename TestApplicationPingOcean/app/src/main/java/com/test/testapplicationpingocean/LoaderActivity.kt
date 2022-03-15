package com.test.testapplicationpingocean

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.test.testapplicationpingocean.databinding.ActivityLoaderBinding
import com.test.testapplicationpingocean.settingsStorage.SettingCheckerStorageImpl

class LoaderActivity : AppCompatActivity() {
    private val storage = SettingCheckerStorageImpl

    @Volatile
    private var transitionListener: MotionLayout.TransitionListener =
        object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                //stub
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                //stub
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                Log.e("TTT", "Start activity")
                runOnUiThread {
                    if (storage.getChecked()) {
                        startActivity(Intent(this@LoaderActivity, ActivityProfile::class.java))
                    }else{
                        Log.e("TTT", "first check")
                        startActivity(Intent(this@LoaderActivity, MainActivity::class.java))
                    }
                }

            }
            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }

        }
    private lateinit var binding: ActivityLoaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoaderBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.motionLayoutMain.setTransitionListener(transitionListener)
        binding.motionLayoutMain.startLayoutAnimation()
    }

    override fun onPause() {
        super.onPause()
        binding.motionLayoutMain.removeTransitionListener(transitionListener)
    }


}