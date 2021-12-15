package com.example.christiansoeappproject.ui.admin.attraction;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.christiansoeappproject.R;
import com.example.christiansoeappproject.model.Attraction;
import com.example.christiansoeappproject.ui.admin.SelectLocationOnMapActivity;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AttractionDetailActivity extends AppCompatActivity {
    private static final int REQUEST_GET_AUDIO_ARRAY = 0;
    private static final int REQUEST_GET_LATLNG =1;
    private EditText nameEditText;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private EditText descriptionEditText;
    private ImageView selectedImage;
    private Bundle extras;
    private byte[] video;
    private byte[] audio;
    private byte[] image;
    private String id;
    private ActivityResultLauncher<Intent> videoGalleryLauncher;
    private ActivityResultLauncher<Intent> imageGalleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_detail);
        nameEditText = findViewById(R.id.nameEditText);
        latitudeEditText = findViewById(R.id.latitudeEditText);
        longitudeEditText = findViewById(R.id.longitudeEditText);
        selectedImage = findViewById(R.id.selectedImage);
        descriptionEditText = findViewById(R.id.descriptionEditText);

        extras = getIntent().getExtras();
        if (extras != null) {
            nameEditText.setText(extras.getString("name"));

            latitudeEditText.setText(String.valueOf(extras.getDouble("latitude")));
            longitudeEditText.setText(String.valueOf(extras.getDouble("longitude")));
            descriptionEditText.setText(extras.getString("description"));

            byte[] imageBytes = extras.getByteArray("image");
            if (imageBytes!=null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes,0, imageBytes.length);
                selectedImage.setImageBitmap(bitmap);
            }

            id = extras.getString("id");
        }

        setupVideoGalleryLauncher();
        setupImageGalleryLauncher();
    }

    public void locationAttractionPressed(View view){
        Intent intent = new Intent(this, SelectLocationOnMapActivity.class);
        double lat = Double.parseDouble(latitudeEditText.getText().toString());
        double longg = Double.parseDouble(longitudeEditText.getText().toString());
        if (lat!=0 && longg!=0){
            intent.putExtra("lat", lat);
            intent.putExtra("long", longg);
        }
        startActivityForResult(intent, REQUEST_GET_LATLNG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GET_LATLNG && resultCode==1){
            Bundle dataExtras = data.getExtras();
            latitudeEditText.setText(String.valueOf(dataExtras.getDouble("lat")));
            longitudeEditText.setText(String.valueOf(dataExtras.getDouble("long")));
        }
        /*
        if (requestCode == REQUEST_GET_AUDIO_ARRAY && resultCode == Activity.RESULT_OK) {
            audio = data.getByteArrayExtra("audio");
        }

         */
    }

    private void setupImageGalleryLauncher() {
        imageGalleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    System.out.println("Back from gallery");
                    try {
                        InputStream is = getContentResolver().openInputStream(result.getData().getData());
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        selectedImage.setImageBitmap(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        image = baos.toByteArray();

                    } catch (Exception e) {
                        System.out.println("error: " + e.getMessage());
                    }
                }
        );
    }

    public void imagePressed(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageGalleryLauncher.launch(intent);
    }

    private void setupVideoGalleryLauncher() {
        videoGalleryLauncher = registerForActivityResult(
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

    public void videoPressed(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        videoGalleryLauncher.launch(intent);
    }


    /**
     * Method to update the fields for the attraction
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateAttractionPressed(View view) {
        Attraction update = new Attraction(id, Double.parseDouble(latitudeEditText.getText().toString()), Double.parseDouble(longitudeEditText.getText().toString()), nameEditText.getText().toString(), descriptionEditText.getText().toString());
        update.setVideo(video);
        update.setAudio(audio);
        update.setImage(image);
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

    public void audioPressed(View view) {
        Intent intent = new Intent(this, AudioActivity.class);
        startActivityForResult(intent, REQUEST_GET_AUDIO_ARRAY);
    }
}