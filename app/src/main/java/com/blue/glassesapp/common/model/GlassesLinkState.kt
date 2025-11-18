package com.blue.glassesapp.common.model

/**
 * <pre>
 *
 * </pre>
 *
 * <p>创建人: zxh</p>
 * <p>日期: 2025/11/3</p>
 *
 * 未配对
 * 未连接
 * 已连接
 * 连接中
 * 连接失败
 */
enum class GlassesLinkState(val value: String) {

    UNPAIRED("未配对"),
    UNCONNECTED("未连接"),
    CONNECTED("已连接"),
    CONNECTING("连接中..."),
    CONNECTION_INFO("连接信息"),
    CONNECT_FAILED(
        "连接失败"
    );

    companion object {
        fun fromValue(type: String): GlassesLinkState {
            return entries.firstOrNull { it.value == type } ?: UNPAIRED
        }
    }

}