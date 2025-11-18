package com.blue.glassesapp.common.model

class ImageViewProps {
    var id: String = ""
        set(value) {
            field = if (value.matches(Regex("[a-zA-Z0-9_]+"))) {
                value
            } else {
                throw IllegalArgumentException("id must be a valid identifier")
            }
        }
    var layout_width: String = "match_parent"
        set(value) {
            field =
                if (value == "match_parent" || value == "wrap_content" || value.endsWith("dp")) {
                    value
                } else if (value.matches(Regex("\\d+"))) {
                    // 如果只提供了数字，则自动添加dp单位
                    "${value}dp"
                } else {
                    throw IllegalArgumentException("layout_width must be 'match_parent', 'wrap_content', or a value ending with 'dp'")
                }
        }
    var layout_height: String = "wrap_content"
        set(value) {
            field =
                if (value == "match_parent" || value == "wrap_content" || value.endsWith("dp")) {
                    value
                } else if (value.matches(Regex("\\d+"))) {
                    // 如果只提供了数字，则自动添加dp单位
                    "${value}dp"
                } else {
                    throw IllegalArgumentException("layout_height must be 'match_parent', 'wrap_content', or a value ending with 'dp'")
                }
        }
    var name: String = "NONE"

    var scaleType: String = "center"
        set(value) {
            field = when (value) {
                "center", "center_crop", "center_inside", "fit_center", "fit_end", "fit_start", "fit_xy", "matrix" -> value
                else -> throw IllegalArgumentException("scaleType must be one of center, center_crop, center_inside, fit_center, fit_end, fit_start, fit_xy, matrix")
            }
        }

    /**
     * 处理颜色值，如果输入的是有效的颜色值，则只保留绿色通道
     */
    private fun processColorValue(color: String): String {
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
            return color
        }
    }

    fun toJson(): String {
        return if (id.isEmpty()) {
            throw IllegalArgumentException("id can not be empty")
        } else {
            val sb: StringBuilder = StringBuilder("{")
                .append("\"id\":\"$id\"")
                .append(",\"layout_width\":\"$layout_width\"")
                .append(",\"layout_height\":\"$layout_height\"")
                .append(",\"name\":\"$name\"")
                .append(",\"scaleType\":\"$scaleType\"")
            sb.append("}").toString()
        }
    }
}