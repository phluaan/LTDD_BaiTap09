package com.example.baitap09;


import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BlueControl extends AppCompatActivity {

    ImageButton btnTb1, btnTb2, btnDis;
    TextView txt1, txtMAC;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    Set<BluetoothDevice> pairedDevices1;
    String address = null;
    private ProgressDialog progress;
    int flaglamp1 = 0;
    int flaglamp2 = 0;

    // UUID chuẩn cho giao tiếp SPP
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        // Nhận địa chỉ MAC của thiết bị Bluetooth từ Intent
        Intent newint = getIntent();
        address = newint.getStringExtra(BlueActivity.EXTRA_ADDRESS);

        // Ánh xạ UI
        btnTb1 = findViewById(R.id.btnTb1);
        btnTb2 = findViewById(R.id.btnTb2);
        txt1 = findViewById(R.id.textV1);
        txtMAC = findViewById(R.id.textViewMAC);
        btnDis = findViewById(R.id.btnDisc);

        new ConnectBT().execute(); // Bắt đầu kết nối Bluetooth

        btnTb1.setOnClickListener(v -> thietTbil()); // Điều khiển thiết bị 1
        btnTb2.setOnClickListener(v -> thiettbi7()); // Điều khiển thiết bị 2
        btnDis.setOnClickListener(v -> Disconnect()); // Ngắt kết nối
    }
    // viet hom
    private void Disconnect() {
        if (btSocket != null) // If the btSocket is busy
        {
            try {
                btSocket.close(); // close connection
            } catch (IOException e) {
                msg(" Lỗi ");
            }
        }
        finish(); // return to the first layout
    }

    private void thietTbil() {
        if (btSocket != null) {
            try {
                if (this.flaglamp1 == 0) {
                    this.flaglamp1 = 1;
                    this.btnTb1.setBackgroundResource(R.drawable.ic_launcher_background);
                    btSocket.getOutputStream().write("1".toString().getBytes());
                    txt1.setText(" Thiết bị số 1 đang bật ");
                    return;
                } else {
                    if (this.flaglamp1 != 1) return;
                    {
                        this.flaglamp1 = 0;
                        this.btnTb1.setBackgroundResource(R.drawable.ic_launcher_foreground);
                        btSocket.getOutputStream().write("A".toString().getBytes());
                        txt1.setText(" Thiết bị số 1 đang tật ");
                        return;
                    }
                }
            } catch (IOException e) {
                msg(" Loi ");
            }
        }
    }
    private void thiettbi7() {
        if (btSocket != null) {
            try {
                if (flaglamp2 == 0) {
                    flaglamp2 = 1;
                    btnTb2.setBackgroundResource(R.drawable.ic_launcher_background);
                    btSocket.getOutputStream().write("7".getBytes());
                    txt1.setText("Thiết bị số 7 đang bật");
                } else {
                    flaglamp2 = 0;
                    btnTb2.setBackgroundResource(R.drawable.ic_launcher_foreground);
                    btSocket.getOutputStream().write("G".getBytes());
                    txt1.setText("Thiết bị số 7 đang tắt");
                }
            } catch (IOException e) {
                msg("Lỗi khi gửi dữ liệu");
            }
        }
    }

    private class ConnectBT extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(BlueControl.this, "Đang kết nối...", "Vui lòng đợi!");
        }

        @Override
        protected Boolean doInBackground(Void... devices) {
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            progress.dismiss();

            if (!success) {
                msg("Kết nối thất bại! Kiểm tra thiết bị.");
                finish();
            } else {
                msg("Kết nối thành công.");
                isBtConnected = true;
                pairedDevicesList1();
            }
        }
    }


    private void pairedDevicesList1() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
            return;
        }

        pairedDevices1 = myBluetooth.getBondedDevices();
        if (!pairedDevices1.isEmpty()) {
            for (BluetoothDevice bt : pairedDevices1) {
                txtMAC.setText(bt.getName() + "\n" + bt.getAddress());
            }
        } else {
            msg("Không tìm thấy thiết bị đã kết nối.");
        }
    }

    private void msg(String s) {
        runOnUiThread(() -> Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show());
    }
}