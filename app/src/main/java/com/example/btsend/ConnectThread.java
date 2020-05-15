package com.example.btsend;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.example.btsend.ui.gallery.GalleryFragment;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class ConnectThread extends Thread{
    private String dataToSend;
    private BluetoothSocket bTSocket;
    private OutputStream outputStream;
    BluetoothDevice device;
    private  BluetoothAdapter myAdapter = BluetoothAdapter.getDefaultAdapter();
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothSocket connect(String address) {
        BluetoothSocket temp = null;
        device = myAdapter.getRemoteDevice(address);
        myAdapter.cancelDiscovery();
        try {
            bTSocket = device.createRfcommSocketToServiceRecord(MY_UUID);

        } catch (IOException e) {
           // Log.d("CONNECTTHREAD","Could not create RFCOMM socket:" + e.toString());
            return bTSocket;
        }
       // try {
           // bTSocket.connect();
            try {
                Method method = device.getClass().getMethod("createBond", (Class[]) null);
                method.invoke(device, (Object[]) null);
               // Log.d("CONNECTTHREAD","OOOOKKKKK");

                bTSocket.connect();

            } catch (Exception e) {

                e.printStackTrace();
                try {
                    bTSocket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                    bTSocket.connect();
                } catch (Exception e2) {

                    //   errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                }

            }


      //      Log.d("CONNECTTHREAD","OOOOKKKKK");
     //   } catch(IOException e) {
       //     Log.d("CONNECTTHREAD","Could not connect: " + e.toString());
        //    try {
        //        bTSocket.close();
        //    } catch(IOException close) {
         //       Log.d("CONNECTTHREAD", "Could not close connection:" + e.toString());
        //        return false;
        //    }
      //  }
        return bTSocket;
    }

    public void setMessage (String data) {
        dataToSend = data;
    }

    public void sendMessage () {
        new Thread(new Runnable() {
            @Override
            public void run() {
               // while (true) {

                    try {

                        outputStream = bTSocket.getOutputStream();
                        String init = dataToSend+"\r\n";
                        //  String init = dataToSend;
                        //  while (true) {
                        outputStream.flush();
                        outputStream.write(init.getBytes());
                        outputStream.flush();
                    //    outputStream.close();
                        //  }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        //    }
        }).start();

    }
    public  void send() {

        while (true) {
    try {
        outputStream = bTSocket.getOutputStream();
        String init = dataToSend;
        outputStream.write(init.getBytes());
        outputStream.flush();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    }

    public void brakeConnect () {
        try {
            bTSocket.close();
            outputStream.close();
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void unpairDevice() {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}