package com.example.christiansoeappproject.ui.home;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.christiansoeappproject.repository.WeatherRepository;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Bitmap> mImage;

    public HomeViewModel() {
        mImage = new MutableLiveData<>();
        Thread thread = new Thread(){
            @Override
            public void run() {
                Bitmap bitmap = WeatherRepository.getBitmap();
                if (bitmap!=null){
                    mImage.postValue(bitmap);
                }
            }
        };
        thread.start();

    }

    public LiveData<Bitmap> getImage(){
        return mImage;
    }
}