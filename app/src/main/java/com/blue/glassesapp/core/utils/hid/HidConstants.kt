package com.blue.glassesapp.core.utils.hid

import android.bluetooth.BluetoothHidDevice
import android.bluetooth.BluetoothHidDeviceAppQosSettings
import android.bluetooth.BluetoothHidDeviceAppSdpSettings
import android.os.ParcelUuid

/**
 * Constants for the HID Report Descriptor and SDP configuration.
 * Useful links:
 * https://source.android.com/devices/input/keyboard-devices#hid-consumer-page-0x0c
 * hut1_12v2 (HID Codes).pdf
 * file:///C:/Users/ahmed/Downloads/hut1_12v2%20(HID%20Codes).pdf
 */
object HidConstants {
    // HID related UUIDs
    val HOGP_UUID: ParcelUuid? = ParcelUuid.fromString("00001812-0000-1000-8000-00805f9b34fb")
    val HID_UUID: ParcelUuid? = ParcelUuid.fromString("00001124-0000-1000-8000-00805f9b34fb")

    val DIS_UUID: ParcelUuid? = ParcelUuid.fromString("0000180A-0000-1000-8000-00805F9B34FB")

    val BAS_UUID: ParcelUuid? = ParcelUuid.fromString("0000180F-0000-1000-8000-00805F9B34FB")

    const val ID_KEYBOARD: Int = 1
    const val ID_REMOTE_CONTROL: Int = 2
    const val ID_MOUSE: Int = 3


    val HID_REPORT_DESC_TEST: ByteArray = byteArrayOf(
        0x05.toByte(), 0x01.toByte(),  // Usage Page (Generic Desktop)        0
        0x09.toByte(), 0x06.toByte(),  // Usage (Keyboard)                    2
        0xa1.toByte(), 0x01.toByte(),  // Collection (Application)            4
        0x85.toByte(), 0x01.toByte(),  //  Report ID (1)                      6
        0x05.toByte(), 0x07.toByte(),  //  Usage Page (Keyboard)              8
        0x19.toByte(), 0xe0.toByte(),  //  Usage Minimum (224)                10
        0x29.toByte(), 0xe7.toByte(),  //  Usage Maximum (231)                12
        0x15.toByte(), 0x00.toByte(),  //  Logical Minimum (0)                14
        0x25.toByte(), 0x01.toByte(),  //  Logical Maximum (1)                16
        0x75.toByte(), 0x01.toByte(),  //  Report Size (1)                    18
        0x95.toByte(), 0x08.toByte(),  //  Report Count (8)                   20
        0x81.toByte(), 0x02.toByte(),  //  Input (Data,Var,Abs)               22
        0x75.toByte(), 0x08.toByte(),  //  Report Size (8)                    24
        0x95.toByte(), 0x01.toByte(),  //  Report Count (1)                   26
        0x81.toByte(), 0x01.toByte(),  //  Input (Cnst,Arr,Abs)               28
        0x75.toByte(), 0x08.toByte(),  //  Report Size (8)                    30
        0x95.toByte(), 0x05.toByte(),  //  Report Count (5)                   32
        0x15.toByte(), 0x00.toByte(),  //  Logical Minimum (0)                34
        0x25.toByte(), 0xff.toByte(),  //  Logical Maximum (255)              36
        0x05.toByte(), 0x07.toByte(),  //  Usage Page (Keyboard)              38
        0x19.toByte(), 0x00.toByte(),  //  Usage Minimum (0)                  40
        0x29.toByte(), 0xff.toByte(),  //  Usage Maximum (255)                42
        0x81.toByte(), 0x00.toByte(),  //  Input (Data,Arr,Abs)               44
        0xc0.toByte(),  // End Collection                      46
        0x05.toByte(), 0x0c.toByte(),  // Usage Page (Consumer Devices)       47
        0x09.toByte(), 0x01.toByte(),  // Usage (Consumer Control)            49
        0xa1.toByte(), 0x01.toByte(),  // Collection (Application)            51
        0x85.toByte(), 0x03.toByte(),  //  Report ID (3)                      53
        0x19.toByte(), 0x00.toByte(),  //  Usage Minimum (0)                  55
        0x2a.toByte(), 0xff.toByte(), 0x03.toByte(),  //  Usage Maximum (1023)               57
        0x75.toByte(), 0x0c.toByte(),  //  Report Size (12)                   60
        0x95.toByte(), 0x01.toByte(),  //  Report Count (1)                   62
        0x15.toByte(), 0x00.toByte(),  //  Logical Minimum (0)                64
        0x26.toByte(), 0xff.toByte(), 0x03.toByte(),  //  Logical Maximum (1023)             66
        0x81.toByte(), 0x00.toByte(),  //  Input (Data,Arr,Abs)               69
        0x75.toByte(), 0x04.toByte(),  //  Report Size (4)                    71
        0x95.toByte(), 0x01.toByte(),  //  Report Count (1)                   73
        0x81.toByte(), 0x01.toByte(),  //  Input (Cnst,Arr,Abs)               75
        0xc0.toByte(),  // End Collection                      77
        0x05.toByte(), 0x01.toByte(),  // Usage Page (Generic Desktop)        78
        0x09.toByte(), 0x02.toByte(),  // Usage (Mouse)                       80
        0xa1.toByte(), 0x01.toByte(),  // Collection (Application)            82
        0x85.toByte(), 0x02.toByte(),  //  Report ID (2)                      84
        0x09.toByte(), 0x01.toByte(),  //  Usage (Pointer)                    86
        0xa1.toByte(), 0x00.toByte(),  //  Collection (Physical)              88
        0x05.toByte(), 0x09.toByte(),  //   Usage Page (Button)               90
        0x19.toByte(), 0x01.toByte(),  //   Usage Minimum (1)                 92
        0x29.toByte(), 0x05.toByte(),  //   Usage Maximum (5)                 94
        0x15.toByte(), 0x00.toByte(),  //   Logical Minimum (0)               96
        0x25.toByte(), 0x01.toByte(),  //   Logical Maximum (1)               98
        0x75.toByte(), 0x01.toByte(),  //   Report Size (1)                   100
        0x95.toByte(), 0x05.toByte(),  //   Report Count (5)                  102
        0x81.toByte(), 0x02.toByte(),  //   Input (Data,Var,Abs)              104
        0x75.toByte(), 0x03.toByte(),  //   Report Size (3)                   106
        0x95.toByte(), 0x01.toByte(),  //   Report Count (1)                  108
        0x81.toByte(), 0x01.toByte(),  //   Input (Cnst,Arr,Abs)              110
        0x05.toByte(), 0x01.toByte(),  //   Usage Page (Generic Desktop)      112
        0x09.toByte(), 0x30.toByte(),  //   Usage (X)                         114
        0x09.toByte(), 0x31.toByte(),  //   Usage (Y)                         116
        0x09.toByte(), 0x38.toByte(),  //   Usage (Wheel)                     118
        0x15.toByte(), 0x81.toByte(),  //   Logical Minimum (-127)            120
        0x25.toByte(), 0x7f.toByte(),  //   Logical Maximum (127)             122
        0x75.toByte(), 0x08.toByte(),  //   Report Size (8)                   124
        0x95.toByte(), 0x03.toByte(),  //   Report Count (3)                  126
        0x81.toByte(), 0x06.toByte(),  //   Input (Data,Var,Rel)              128
        0x05.toByte(), 0x0c.toByte(),  //   Usage Page (Consumer Devices)     130
        0x0a.toByte(), 0x38.toByte(), 0x02.toByte(),  //   Usage (AC Pan)                    132
        0x95.toByte(), 0x01.toByte(),  //   Report Count (1)                  135
        0x81.toByte(), 0x06.toByte(),  //   Input (Data,Var,Rel)              137
        0xc0.toByte(),  //  End Collection                     139
        0xc0.toByte(),  // End Collection                      140
        // 142 bytes

    )


    val HID_REPORT_DESC: ByteArray = byteArrayOf(
        // Keyboard
        0x05.toByte(), 0x01.toByte(),  // Usage page (Generic Desktop)
        0x09.toByte(), 0x06.toByte(),  // Usage (Keyboard)
        0xA1.toByte(), 0x01.toByte(),  // Collection (Application)
        0x85.toByte(), ID_KEYBOARD.toByte(),  //   Report ID (1)
        0x05.toByte(), 0x07.toByte(),  //   Usage page (Keyboard Key Codes)
        0x19.toByte(), 0xE0.toByte(),  //   Usage minimum (224) Keyboard LeftControl
        0x29.toByte(), 0xE7.toByte(),  //   Usage maximum (231) Keyboard Right GUI
        0x15.toByte(), 0x00.toByte(),  //   Logical minimum (0)
        0x25.toByte(), 0x01.toByte(),  //   Logical maximum (1)
        0x75.toByte(), 0x01.toByte(),  //   Report size (1) bit
        0x95.toByte(), 0x08.toByte(),  //   Report count (8)
        0x81.toByte(), 0x02.toByte(),  //   Input (Data, Variable, Absolute)     ; Modifier byte
        // no need for a reserve byte i think...
        //            (byte) 0x75, (byte) 0x08,               //       Report size (8)
        //            (byte) 0x95, (byte) 0x01,               //       Report count (1)
        //            (byte) 0x81, (byte) 0x01,               //       Input (Constant)                   ; Reserved byte
        // Keyboard Key
        0x75.toByte(), 0x08.toByte(),  //    Report size (8) 8 bits
        0x95.toByte(), 0x01.toByte(),  //    Report count (1)
        0x15.toByte(), 0x00.toByte(),  //    Logical Minimum (0)
        0x26.toByte(), 0xFF.toByte(), 0x00.toByte(),  //    Logical Maximum (255)
        0x05.toByte(), 0x07.toByte(),  //    Usage page (Keyboard Key Codes)
        0x19.toByte(), 0x00.toByte(),  //    Usage Minimum (0)
        0x29.toByte(), 0xFF.toByte(),  //    Usage Maximum (255)
        0x81.toByte(), 0x00.toByte(),  //    Input (Data, Arr, Absolute)     ; Key array (1 key)
        0xC0.toByte(),  // End Collection
        // Remote control

        0x05.toByte(), 0x0c.toByte(),  //     USAGE_PAGE (Consumer Devices)
        0x09.toByte(), 0x01.toByte(),  //     USAGE (Consumer Control)
        0xa1.toByte(), 0x01.toByte(),  //     COLLECTION (Application)
        0x85.toByte(), ID_REMOTE_CONTROL.toByte(),  //       REPORT_ID (2)
        0x19.toByte(), 0x00.toByte(),  //       USAGE_MINIMUM (Unassigned)
        0x2a.toByte(), 0xff.toByte(), 0x03.toByte(),  //       USAGE_MAXIMUM (1023)
        0x75.toByte(), 0x0a.toByte(),  //       REPORT_SIZE (10) bit
        0x95.toByte(), 0x01.toByte(),  //       REPORT_COUNT (1)       10x1=1byte
        0x15.toByte(), 0x00.toByte(),  //       LOGICAL_MINIMUM (0)
        0x26.toByte(), 0xff.toByte(), 0x03.toByte(),  //       LOGICAL_MAXIMUM (1023)
        0x81.toByte(), 0x00.toByte(),  //       INPUT (Data,Ary,Abs)
        0xc0.toByte(),  //     END_COLLECTION
        // Mouse

        0x05.toByte(), 0x01.toByte(),  // Usage Page (Generic Desktop)
        0x09.toByte(), 0x02.toByte(),  // Usage (Mouse)
        0xA1.toByte(), 0x01.toByte(),  // Collection (Application)
        0x85.toByte(), ID_MOUSE.toByte(),  //    Report ID
        0x09.toByte(), 0x01.toByte(),  //    Usage (Pointer)
        0xA1.toByte(), 0x00.toByte(),  //    Collection (Physical)
        0x05.toByte(), 0x09.toByte(),  //       Usage Page (Buttons)
        0x19.toByte(), 0x01.toByte(),  //       Usage minimum (1)
        0x29.toByte(), 0x03.toByte(),  //       Usage maximum (3)
        0x15.toByte(), 0x00.toByte(),  //       Logical minimum (0)
        0x25.toByte(), 0x01.toByte(),  //       Logical maximum (1)
        0x75.toByte(), 0x01.toByte(),  //       Report size (1)
        0x95.toByte(), 0x03.toByte(),  //       Report count (3)
        0x81.toByte(), 0x02.toByte(),  //       Input (Data, Variable, Absolute)
        0x75.toByte(), 0x05.toByte(),  //       Report size (5)
        0x95.toByte(), 0x01.toByte(),  //       Report count (1)
        0x81.toByte(), 0x01.toByte(),  //       Input (constant)                 ; 5 bit padding
        0x05.toByte(), 0x01.toByte(),  //       Usage page (Generic Desktop)
        0x09.toByte(), 0x30.toByte(),  //       Usage (X)
        0x09.toByte(), 0x31.toByte(),  //       Usage (Y)
        0x09.toByte(), 0x38.toByte(),  //       Usage (Wheel)
        0x15.toByte(), 0x81.toByte(),  //       Logical minimum (-127)
        0x25.toByte(), 0x7F.toByte(),  //       Logical maximum (127)
        0x75.toByte(), 0x08.toByte(),  //       Report size (8)
        0x95.toByte(), 0x03.toByte(),  //       Report count (3)
        0x81.toByte(), 0x06.toByte(),  //       Input (Data, Variable, Relative)
        0xC0.toByte(),  //    End Collection
        0xC0.toByte(),  // End Collection


    )

    private const val SDP_NAME = "BTRemote"
    private const val SDP_DESCRIPTION = "BTRemote"
    private const val SDP_PROVIDER = "Demo"
    private const val QOS_TOKEN_RATE = 800 // 9 bytes * 1000000 us / 11250 us
    private const val QOS_TOKEN_BUCKET_SIZE = 9
    private const val QOS_PEAK_BANDWIDTH = 0
    private const val QOS_LATENCY = 11250

    val SDP_RECORD: BluetoothHidDeviceAppSdpSettings = BluetoothHidDeviceAppSdpSettings(
        SDP_NAME,
        SDP_DESCRIPTION,
        SDP_PROVIDER,  //                    BluetoothHidDevice.SUBCLASS1_MOUSE,
        //                    BluetoothHidDevice.SUBCLASS1_KEYBOARD,
        BluetoothHidDevice.SUBCLASS1_COMBO,  //                    BluetoothHidDevice.SUBCLASS2_REMOTE_CONTROL,
        //                    BluetoothHidDevice.SUBCLASS2_UNCATEGORIZED,
        //                    BluetoothHidDevice.SUBCLASS1_NONE,
        HID_REPORT_DESC
    )

    //                    Constants.HID_REPORT_DESC_TEST);
    val QOS_OUT: BluetoothHidDeviceAppQosSettings = BluetoothHidDeviceAppQosSettings(
        BluetoothHidDeviceAppQosSettings.SERVICE_BEST_EFFORT,
        QOS_TOKEN_RATE,
        QOS_TOKEN_BUCKET_SIZE,
        QOS_PEAK_BANDWIDTH,
        QOS_LATENCY,
        BluetoothHidDeviceAppQosSettings.MAX
    )
}
