package com.blue.glassesapp.core.utils.hid

// https://source.android.com/docs/core/interaction/input/keyboard-devices#hid-consumer-page-0x0c
object RemoteInput {

    val REMOTE_INPUT_NONE = byteArrayOf(0x00, 0x00)
    // Navigation
    val REMOTE_INPUT_MENU by lazy { byteArrayOf(0x40, 0x00) }
    val REMOTE_INPUT_MENU_PICK by lazy { byteArrayOf(0x41, 0x00) }
    val REMOTE_INPUT_MENU_UP by lazy { byteArrayOf(0x42, 0x00) }
    val REMOTE_INPUT_MENU_DOWN by lazy { byteArrayOf(0x43, 0x00) }
    val REMOTE_INPUT_MENU_LEFT by lazy { byteArrayOf(0x44, 0x00) }
    val REMOTE_INPUT_MENU_RIGHT by lazy { byteArrayOf(0x45, 0x00) }

    // Multimedia
    val REMOTE_INPUT_PLAY_PAUSE by lazy { byteArrayOf(0xCD.toByte(), 0x00) }
    val REMOTE_INPUT_PREVIOUS by lazy { byteArrayOf(0xB4.toByte(), 0x00) }
    val REMOTE_INPUT_NEXT by lazy { byteArrayOf(0xB3.toByte(), 0x00) }
    val REMOTE_INPUT_CLOSED_CAPTIONS by lazy { byteArrayOf(0x61, 0x00) }

    // Volume
    val REMOTE_INPUT_VOLUME_INC by lazy { byteArrayOf(0xE9.toByte(), 0x00) }
    val REMOTE_INPUT_VOLUME_DEC by lazy { byteArrayOf(0xEA.toByte(), 0x00) }
    val REMOTE_INPUT_VOLUME_MUTE by lazy { byteArrayOf(0xE2.toByte(), 0x00) }

    // Brightness
    val REMOTE_INPUT_BRIGHTNESS_INC by lazy { byteArrayOf(0x6F.toByte(), 0x00) }
    val REMOTE_INPUT_BRIGHTNESS_DEC by lazy { byteArrayOf(0x70.toByte(), 0x00) }

    // Channel
    val REMOTE_INPUT_CHANNEL_INC by lazy { byteArrayOf(0x9C.toByte(), 0x00) }
    val REMOTE_INPUT_CHANNEL_DEC by lazy { byteArrayOf(0x9D.toByte(), 0x00) }

    // Others
    val REMOTE_INPUT_HOME by lazy { byteArrayOf(0x23, 0x02) }
    val REMOTE_INPUT_BACK by lazy { byteArrayOf(0x24, 0x02) }
    val REMOTE_INPUT_POWER by lazy { byteArrayOf(0x30, 0x00) }

}