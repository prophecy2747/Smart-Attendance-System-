package com.example.smartattendancesystem

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private lateinit var usbBroadcastReceiver: UsbBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usbBroadcastReceiver = UsbBroadcastReceiver()
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        registerReceiver(usbBroadcastReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(usbBroadcastReceiver)
    }
}

class UsbBroadcastReceiver : BroadcastReceiver() {
    private val expectedVid = 1234
    private val expectedPid = 5678

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == UsbManager.ACTION_USB_DEVICE_ATTACHED) {
            val usbDevice = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
            if (usbDevice?.vendorId == expectedVid && usbDevice.productId == expectedPid) {
                val intent = Intent(context, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("usbDetected", true)
                context?.startActivity(intent)
            }
        } else if (intent?.action == UsbManager.ACTION_USB_DEVICE_DETACHED) {
            val usbDevice = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
            if (usbDevice?.vendorId == expectedVid && usbDevice.productId == expectedPid) {
                val intent = Intent(context, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("usbDetected", false)
                context?.startActivity(intent)
            }
        }
    }
}