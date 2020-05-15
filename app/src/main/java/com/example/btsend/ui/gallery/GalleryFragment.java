package com.example.btsend.ui.gallery;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.btsend.BtDeviceDialogFragment;
import com.example.btsend.DialogForm;
import com.example.btsend.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class GalleryFragment extends Fragment implements BtDeviceDialogFragment.DialogListener {

    private GalleryViewModel galleryViewModel;

    public static  final int REQUEST_ACCESS_COARSE_LOCATION = 1;
    public  static  final  int REQUEST_ENABLE_BLUETOOTH = 11;
    private ListView devicesList;
    private ArrayList <String> stringArrayList = new ArrayList<String>();
    private Button scanButton;
    private EditText sendText;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> listAdapter;
    BluetoothAdapter myAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice device;
    BluetoothSocket btSock;
    OutputStream outStream;
    Bundle bundle= new Bundle();
    private int numVest = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {






        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        scanButton = root.findViewById(R.id.scanBtn);
        devicesList = root.findViewById(R.id.scanListView);

        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Object o = parent.getAdapter().getItem(position);
                String data = (String)o;

               // FragmentManager manager = getActivity().getSupportFragmentManager();
               // BtDeviceDialogFragment myDialogFragment = new BtDeviceDialogFragment();
                //bundle = new Bundle();
                bundle.putString("name",data);
                openDialog(bundle);
               //// myDialogFragment.setArguments(bundle);
              //  myDialogFragment.show(manager, "myDialog");



                //myDialogFragment.setTargetFragment(GalleryFragment.this,1);
               // Intent intent = new Intent(GalleryFragment.this, );
               // String message = "abc";
              //  intent.putExtra(EXTRA_MESSAGE, message);
              //  startActivity(intent);
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAdapter.clear();
                myAdapter.startDiscovery();

            }
        });
        /*
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = sendText.getText().toString();
                try {
                    outStream = btSock.getOutputStream();
                    outStream.flush();
                    outStream.write(txt.getBytes());
                    outStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        */

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(myReciever,intentFilter);

        listAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,stringArrayList);
        devicesList.setAdapter(listAdapter);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);

            }
        });
        return root;
    }

    public void openDialog(Bundle bundle) {
        bundle.putInt("num",numVest);
        DialogForm dialogForm = new DialogForm();
        dialogForm.setArguments(bundle);
        dialogForm.show(getActivity().getSupportFragmentManager(),"dialogForm");
    }

    BroadcastReceiver myReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getName()!=null) {
                    if (!stringArrayList.contains(device.getName() + "\n" + device.getAddress())) {
                        stringArrayList.add(device.getName() + "\n" + device.getAddress());

                        //if (device.getName().contains("VEST")) {
                        bundle.putString(String.valueOf(numVest), device.getName());
                        numVest++;
                        // }
                    }
                    listAdapter.notifyDataSetChanged();
                }
            }
        }
    };


    @Override
    public void sendInformation(BluetoothSocket socket) {
        btSock = socket;
    }
}
