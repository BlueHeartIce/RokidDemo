package com.blue.glassesapp.feature.home.model

const val NOTIFICATION_ID = 1
const val NOTIFICATION_CHANNEL_ID = "notificationChannelId"

const val DATA_STORE_PREFERENCES_SETTINGS_NAME = "dataStoreSettings"

const val MOUSE_SPEED_DEFAULT_VALUE = 1.5f

const val SOURCE_CODE_LINK = "https://gitlab.com/Atharok/BtRemote"
const val WEB_SITE_LINK = "https://atharok.gitlab.io/site/projects/bt-remote/"

const val KEYBOARD_REPORT_ID = 0x01
const val REMOTE_REPORT_ID = 0x02
const val MOUSE_REPORT_ID = 0x03

const val DELAY_BETWEEN_KEY_PRESSES_IN_MILLIS = 25L

val REMOTE_INPUT_NONE = byteArrayOf(0x00, 0x00)

val bluetoothHidDescriptor = byteArrayOf(
    // Remote Control
    0x05.toByte(), 0x0C.toByte(),                    // Usage Page (Consumer Devices)
    0x09.toByte(), 0x01.toByte(),                    // Usage (Consumer Control)
    0xA1.toByte(), 0x01.toByte(),                    // Collection (Application)
    0x85.toByte(), REMOTE_REPORT_ID.toByte(),        //   Report ID (2)
    0x19.toByte(), 0x00.toByte(),                    //   Usage Minimum (Unassigned)
    0x2A.toByte(), 0xFF.toByte(), 0x03.toByte(),     //   Usage Maximum (1023)
    0x75.toByte(), 0x10.toByte(),                    //   Report Size (16)
    0x95.toByte(), 0x01.toByte(),                    //   Report Count (1)
    0x15.toByte(), 0x00.toByte(),                    //   Logical Minimum (0)
    0x26.toByte(), 0xFF.toByte(), 0x03.toByte(),     //   Logical Maximum (1023)
    0x81.toByte(), 0x00.toByte(),                    //   Input (Data,Array,Absolute)
    0xC0.toByte(),                                   // End Collection

    // Keyboard
    0x05.toByte(), 0x01.toByte(),                    // Usage Page (Generic Desktop)
    0x09.toByte(), 0x06.toByte(),                    // Usage (Keyboard)
    0xA1.toByte(), 0x01.toByte(),                    // Collection (Application)
    0x85.toByte(), KEYBOARD_REPORT_ID.toByte(),      //   Report ID (1)
    0x05.toByte(), 0x07.toByte(),                    //   Usage Page (Keyboard Key Codes)
    0x19.toByte(), 0xE0.toByte(),                    //   Usage Minimum (224)
    0x29.toByte(), 0xE7.toByte(),                    //   Usage Maximum (231)
    0x15.toByte(), 0x00.toByte(),                    //   Logical Minimum (0)
    0x25.toByte(), 0x01.toByte(),                    //   Logical Maximum (1)
    0x75.toByte(), 0x01.toByte(),                    //   Report Size (1)
    0x95.toByte(), 0x08.toByte(),                    //   Report Count (8)
    0x81.toByte(), 0x02.toByte(),                    //   Input (Data,Variable,Absolute)
    0x75.toByte(), 0x08.toByte(),                    //    Report Size (8)
    0x95.toByte(), 0x01.toByte(),                    //    Report Count (1)
    0x15.toByte(), 0x00.toByte(),                    //    Logical Minimum (0)
    0x26.toByte(), 0xFF.toByte(), 0x00.toByte(),     //    Logical Maximum (255)
    0x05.toByte(), 0x07.toByte(),                    //    Usage Page (Keyboard Key Codes)
    0x19.toByte(), 0x00.toByte(),                    //    Usage Minimum (0)
    0x29.toByte(), 0xFF.toByte(),                    //    Usage Maximum (255)
    0x81.toByte(), 0x00.toByte(),                    //    Input (Data,Array,Absolute)
    0xC0.toByte(),                                   // End Collection

    // Mouse
    0x05.toByte(), 0x01.toByte(),                    // Usage Page (Generic Desktop)
    0x09.toByte(), 0x02.toByte(),                    // Usage (Mouse)
    0xA1.toByte(), 0x01.toByte(),                    // Collection (Application)
    0x85.toByte(), MOUSE_REPORT_ID.toByte(),         //   Report ID (3)
    0x09.toByte(), 0x01.toByte(),                    //   Usage (Pointer)
    0xA1.toByte(), 0x00.toByte(),                    //   Collection (Physical)
    0x05.toByte(), 0x09.toByte(),                    //     Usage Page (Button)
    0x19.toByte(), 0x01.toByte(),                    //     Usage Minimum (1)
    0x29.toByte(), 0x03.toByte(),                    //     Usage Maximum (3)
    0x15.toByte(), 0x00.toByte(),                    //     Logical Minimum (0)
    0x25.toByte(), 0x01.toByte(),                    //     Logical Maximum (1)
    0x75.toByte(), 0x01.toByte(),                    //     Report Size (1)
    0x95.toByte(), 0x03.toByte(),                    //     Report Count (3)
    0x81.toByte(), 0x02.toByte(),                    //     Input (Data,Variable,Absolute)
    0x75.toByte(), 0x05.toByte(),                    //     Report Size (5)
    0x95.toByte(), 0x01.toByte(),                    //     Report Count (1)
    0x81.toByte(), 0x01.toByte(),                    //     Input (Constant,Array,Absolute)
    0x05.toByte(), 0x01.toByte(),                    //     Usage Page (Generic Desktop)
    0x09.toByte(), 0x30.toByte(),                    //     Usage (X)
    0x09.toByte(), 0x31.toByte(),                    //     Usage (Y)
    0x09.toByte(), 0x38.toByte(),                    //     Usage (Wheel)
    0x15.toByte(), 0x81.toByte(),                    //     Logical Minimum (-127)
    0x25.toByte(), 0x7F.toByte(),                    //     Logical Maximum (127)
    0x75.toByte(), 0x08.toByte(),                    //     Report Size (8)
    0x95.toByte(), 0x03.toByte(),                    //     Report Count (3)
    0x81.toByte(), 0x06.toByte(),                    //     Input (Data,Variable,Relative)
    0xC0.toByte(),                                   //   End Collection
    0xC0.toByte()                                    // End Collection
)