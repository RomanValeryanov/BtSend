package com.example.btsend;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

public class DialogForm extends AppCompatDialogFragment {
    private ArrayList<String> stringArrayList = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter;

    private int numVest;
    private EditText editText;
    BluetoothSocket socket;
    ListView vestList;
    Button connectBtn,sendBtn;
    String address="";
    ConnectThread connectThread = new ConnectThread();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);
        editText = view.findViewById(R.id.editText2);
        connectBtn = view.findViewById(R.id.dialogButtonConnect);
        vestList = view.findViewById(R.id.vestList);
        sendBtn = view.findViewById(R.id.dialogButtonSend);
        sendBtn.setEnabled(false);
        sendBtn.setBackgroundColor(Color.GRAY);
        connectBtn.setEnabled(true);
        listAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,stringArrayList);
        Log.d("gg",getArguments().getString("name"));
        String lines[] = getArguments().getString("name").split("(?<=\n)");
        address = lines[1];

        numVest = getArguments().getInt("num");
        for (int i =0; i<numVest; i++) {
            stringArrayList.add(getArguments().getString(String.valueOf(i)));
        }
        vestList.setAdapter(listAdapter);


        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //connectThread.setMessage(editText.getText().toString());
               // socket = connectThread.connect("50:2E:5C:1D:62:A7");
                //connectThread.setMessage(editText.getText().toString());
                //connectThread.sendMessage();
               // socket = connectThread.connect("DC:66:72:15:11:26");
                socket = connectThread.connect(address);
                connectBtn.setBackgroundColor(Color.GRAY);
                connectBtn.setEnabled(false);
                sendBtn.setBackgroundColor(Color.parseColor("#03E3CE"));
                sendBtn.setEnabled(true);
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addr = editText.getText().toString()+"\r\n";
                connectThread.setMessage(addr);
                connectThread.sendMessage();
            }
        });


        builder.setView(view)
                .setTitle (getArguments().getString("name"))
                .setPositiveButton("Завершить соединение", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            connectThread.brakeConnect();
                    }
                });

        return builder.create();
    }
    public interface DialogListener {
        void sendData(BluetoothSocket socket);
    }

    public String parseAddress (String data) {

        return data;
    }
}
