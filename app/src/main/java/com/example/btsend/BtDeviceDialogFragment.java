package com.example.btsend;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.btsend.ui.gallery.GalleryFragment;

import java.io.OutputStream;

public class BtDeviceDialogFragment extends AppCompatDialogFragment {
    String data = "";
    ConnectThread connectThread = new ConnectThread();
    BluetoothSocket socket;
    ThreadSend threadSend;
    private DialogListener dialogListener;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        data = getArguments().getString("name");
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ///AlertDialog builder = new AlertDialog.Builder(getActivity())
        ///        .setTitle(data)
         ///       .setPositiveButton("Подключиться",null)
          ///      .show();

        builder.setTitle(data);
        final EditText input = new EditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setHint("Введите данные");
        builder.setView(input);
                builder.setMessage("Покормите кота!");
                builder.setIcon(R.drawable.ic_launcher_background);
                builder.setPositiveButton("Подключиться", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        connectThread.setMessage(input.getText().toString());
                        socket = connectThread.connect("50:2E:5C:1D:62:A7");
                       // connectThread.sendMessage();
                        //dialogListener.sendInformation(socket);
                    }
                });


        builder.setNegativeButton("Отправить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                connectThread.brakeConnect();
               // connectThread.send();
                //Toast.makeText(getActivity(), "Возможно вы правы", Toast.LENGTH_LONG)
               //         .show();
            }
        });


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
      //      dialogListener = (DialogListener)getTargetFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  interface DialogListener {
        void sendInformation (BluetoothSocket socket);
    }

}
