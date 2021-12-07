package com.example.christiansoeappproject.ui.admin.attraction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;

import com.example.christiansoeappproject.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AudioActivity extends AppCompatActivity {
    private final MediaRecorder recorder = new MediaRecorder();
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private byte[] audio;

    public void start(View view){
        ParcelFileDescriptor[] descriptors = new ParcelFileDescriptor[0];
        try {
            descriptors = ParcelFileDescriptor.createPipe();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        ParcelFileDescriptor parcelRead = new ParcelFileDescriptor(descriptors[0]);
        ParcelFileDescriptor parcelWrite = new ParcelFileDescriptor(descriptors[1]);

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(parcelWrite.getFileDescriptor());

        InputStream inputStream = new ParcelFileDescriptor.AutoCloseInputStream(parcelRead);
        try {
            recorder.prepare();
            recorder.start();

            int read;
            byte[] data = new byte[16384];

            while ((read = inputStream.read(data, 0, data.length)) != -1){
                byteArrayOutputStream.write(data,0, read);
            }
            audio= byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void stop(View view){
        recorder.stop();
        recorder.reset();
        recorder.release();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
    }

    public void savePressed(View view){
        setResult(Activity.RESULT_OK, new Intent().putExtra("audio", audio));
        finish();
    }
}