package com.blue.glassesapp.common.model

/**
 * 处理颜色值，如果输入的是有效的颜色值，则只保留绿色通道
 */
fun processColorValue(color: String): String {
    // 检查是否为有效的ARGB格式 (#AARRGGBB)
    if (color.matches(Regex("#[0-9a-fA-F]{8}"))) {
        // 解析ARGB各通道值
        val alpha = color.substring(1, 3)
        val red = color.substring(3, 5)
        val green = color.substring(5, 7)
        val blue = color.substring(7, 9)

        // 返回只保留绿色通道的颜色值，红色和蓝色通道设为00
        return "#$alpha${"00"}$green${"00"}"
    }
    // 检查是否为有效的RGB格式 (#RRGGBB)
    else if (color.matches(Regex("#[0-9a-fA-F]{6}"))) {
        // 解析RGB各通道值
        val red = color.substring(1, 3)
        val green = color.substring(3, 5)
        val blue = color.substring(5, 7)

        // 返回只保留绿色通道的颜色值，红色和蓝色通道设为00
        return "#FF${"00"}$green${"00"}"
    }
    // 如果不是有效的颜色格式，直接返回原值
    else {
        throw IllegalArgumentException("Invalid color format: $color")
    }
}