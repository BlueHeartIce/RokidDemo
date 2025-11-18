package com.blue.glassesapp.common.model

/**
 * <pre>
 *
 * </pre>
 *
 * <p>创建人: zxh</p>
 * <p>日期: 2025/11/3</p>
 *
void onConnectionInfo(String var1, String var2, String var3, int var4);

void onConnected();

void onDisconnected();

void onFailed(ValueUtil.CxrBluetoothErrorCode var1);
 */
enum class BluetoothLinkState(val value: String) {
    UNPAIRED("未配对"),
    CONNECTING("连接中"),
    CONNECTED("已连接"),
    ON_CONNECTION_INFO("连接信息"),
    DISCONNECTED("断开连接"),
    FAILED("连接失败");

    companion object {
        fun fromValue(type: String): BluetoothLinkState {
            return entries.firstOrNull { it.value == type } ?: UNPAIRED
        }
    }
}