package com.mk.audiotest

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mk.audiotest.ui.theme.AudioTestTheme

@SuppressLint("MissingPermission")
class MainActivity : ComponentActivity() {

    private var audioRecord: AudioRecord? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AudioTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AudioRecordButton()
                }
            }
        }

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    Toast.makeText(this, "权限申请通过了", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Denny", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                Manifest.permission.RECORD_AUDIO
            )
        }
    }

    @Composable
    fun AudioRecordButton() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Hello Fuck")
            Spacer(Modifier.size(16.dp))
            Button(onClick = {
                audioRecord?.run {
                    stop()
                    release()
                }

                val bufferSize = AudioRecord.getMinBufferSize(
                    44100,
                    AudioFormat.CHANNEL_IN_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT
                )

                audioRecord = if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    AudioRecord(
                        MediaRecorder.AudioSource.MIC,
                        44100,
                        AudioFormat.CHANNEL_IN_STEREO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize
                    )
                } else null
            }, modifier = Modifier.size(width = 100.dp, height = 40.dp)) {
                Text(text = "Open", fontWeight = FontWeight.W500)
            }
            Spacer(Modifier.size(16.dp))
            Button(onClick = {
                audioRecord?.startRecording()
            }, modifier = Modifier.size(width = 100.dp, height = 40.dp)) {
                Text(text = "Start", fontWeight = FontWeight.W500)
            }
            Spacer(Modifier.size(16.dp))
            Button(onClick = {
                audioRecord?.stop()
            }, modifier = Modifier.size(width = 100.dp, height = 40.dp)) {
                Text(text = "Stop", fontWeight = FontWeight.W500)
            }
            Spacer(Modifier.size(16.dp))
            Button(onClick = {
                if (audioRecord == null) return@Button
                audioRecord?.stop()
                audioRecord?.release()
                audioRecord = null
            }, modifier = Modifier.size(width = 100.dp, height = 40.dp)) {
                Text(text = "Release", fontWeight = FontWeight.W500)
            }
            Spacer(Modifier.size(16.dp))
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        AudioTestTheme {
            AudioRecordButton()
        }
    }
}