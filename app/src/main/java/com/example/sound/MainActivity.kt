package com.example.sound

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sound.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object{
        const val TAG = "MainActivity"
    }

    private var mService: Messenger? = null
    private lateinit var mBoundServiceIntent: Intent
    private var mServiceBound = false

    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            mServiceBound = false
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mService = Messenger(service)
            mServiceBound = true
        }
    }
  //  private var mMediaPlayer: MediaPlayer? = null
  //  private var isReady: Boolean = false
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPlay.setOnClickListener {
        //    play()

            if (mServiceBound){
                try {
                    mService?.send(Message.obtain(null, MediaService.PLAY, 0, 0))
                }catch (e: RemoteException){
                    e.printStackTrace()
                }
            }

        }

        binding.btnStop.setOnClickListener {
       //     stop()

            if (mServiceBound) {
                try {
                    mService?.send(Message.obtain(null, MediaService.STOP, 0, 0))
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }

        mBoundServiceIntent = Intent(this@MainActivity, MediaService::class.java)
        mBoundServiceIntent.action = MediaService.ACTION_CREATE
        startService(mBoundServiceIntent)
        bindService(mBoundServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)

       // init()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        unbindService(mServiceConnection)
        mBoundServiceIntent.action = MediaService.ACTION_DESTROY

        startService(mBoundServiceIntent)
    }

/* private fun init() {
     mMediaPlayer = MediaPlayer()
     val attribute = AudioAttributes.Builder()
         .setUsage(AudioAttributes.USAGE_MEDIA)
         .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
         .build()
     mMediaPlayer?.setAudioAttributes(attribute)
     val afd = applicationContext.resources.openRawResourceFd(R.raw.gitar)
     try {
         mMediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
     } catch (e: IOException) {
         e.printStackTrace()
     }
     mMediaPlayer?.setOnPreparedListener {
         isReady = true
         mMediaPlayer?.start()
     }
     mMediaPlayer?.setOnErrorListener { _, _, _ -> false }
 }

 private fun play() {
     if (!isReady) {
         mMediaPlayer?.prepareAsync()
     } else {
         if (mMediaPlayer?.isPlaying as Boolean) {
             mMediaPlayer?.pause()
         } else {
             mMediaPlayer?.start()
         }
     }
 }

 private fun stop() {
     if (mMediaPlayer?.isPlaying as Boolean || isReady) {
         mMediaPlayer?.stop()
         isReady = false
     }
 }*/
}