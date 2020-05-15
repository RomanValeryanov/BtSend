package com.example.btsend.ui.gallery;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {
    public static  final int REQUEST_ACCESS_COARSE_LOCATION = 1;
    public  static  final  int REQUEST_ENABLE_BLUETOOTH = 11;
    private ListView devicesList;
    private Button scanButton;
    private BluetoothAdapter bluetoothAdapter;
    private MutableLiveData<String> mText;
    private ArrayAdapter<String> listAdapter;


    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Список устройств");
    }

    public LiveData<String> getText() {
        return mText;
    }





}