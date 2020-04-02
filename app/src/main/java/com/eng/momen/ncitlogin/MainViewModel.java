package com.eng.momen.ncitlogin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.eng.momen.ncitlogin.user.AppDatabase;
import com.eng.momen.ncitlogin.user.User;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<User> user;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        user = database.userDao().loadUser();
    }

    public LiveData<User> getUser(){
        return user;
    }



}
