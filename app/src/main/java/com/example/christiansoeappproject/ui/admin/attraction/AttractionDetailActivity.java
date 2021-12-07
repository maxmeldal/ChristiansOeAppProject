package com.example.christiansoeappproject.ui.admin.attraction;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.service.AttractionService;
import com.example.christiansoeappproject.ui.Updatable;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.Arrays;

public class AttractionDetailActivity extends AppCompatActivity {
    private static final int REQUEST_GET_AUDIO_ARRAY = 0;
    private EditText nameEditText;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private Bundle extras;
    private byte[] video;
    private byte[] audio;
    private String id;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_detail);
        nameEditText = findViewById(R.id.nameEditText);
        latitudeEditText = findViewById(R.id.latitudeEditText);
        longitudeEditText = findViewById(R.id.longitudeEditText);

        extras = getIntent().getExtras();
        if (extras != null) {
            nameEditText.setText(extras.getString("name"));

            latitudeEditText.setText(String.valueOf(extras.getDouble("longitude")));
            longitudeEditText.setText(String.valueOf(extras.getDouble("longitude")));

            id = extras.getString("id");
        }

        setupGalleryLauncher();
    }

    private void setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    System.out.println("back from gallery");
                    try {
                        InputStream is = getContentResolver().openInputStream(result.getData().getData());
                        video = IOUtils.toByteArray(is);
                    } catch (Exception e) {
                        System.out.println("error: " + e.getMessage());
                    }
                }
        );
    }

    public void galleryPressed(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }


    /**
     * Method to update the fields for the attraction
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateAttractionPressed(View view) {
        Attraction update = new Attraction(id, Double.parseDouble(latitudeEditText.getText().toString()), Double.parseDouble(longitudeEditText.getText().toString()), nameEditText.getText().toString());
        update.setVideo(video);
        update.setAudio(audio);
        AttractionsActivity.service.update(update);
        AttractionsActivity.adapter.notifyDataSetChanged();
        finish();
    }

    /**
     * Method to delete the attraction from the database, also removes the attraction from any trips that might have it
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteAttractionPressed(View view) {
        AttractionsActivity.service.delete(id);
        AttractionsActivity.tripService.deleteAttractionFromTrips(id);
        AttractionsActivity.adapter.notifyDataSetChanged();
        finish();
    }

    public void audioPressed(View view){
        Intent intent = new Intent(this, AudioActivity.class);
        startActivityForResult(intent, REQUEST_GET_AUDIO_ARRAY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GET_AUDIO_ARRAY && resultCode == Activity.RESULT_OK) {
            audio = data.getByteArrayExtra("audio");
        }
    }
}