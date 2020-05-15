package com.example.btsend;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;

public class ThreadSend extends Thread {
    public BluetoothSocket btSk;
    public OutputStream out;
    public ThreadSend(BluetoothSocket socket) {
        btSk = socket;

    }
    public void run () {
        while (true) {
            try {
                out = btSk.getOutputStream();
                String init = "test\r\n";
                out.write(init.getBytes());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
