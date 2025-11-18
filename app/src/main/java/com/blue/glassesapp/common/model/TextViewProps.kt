package com.blue.glassesapp.common.model

class TextViewProps {
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
    var text: String = "NONE"

    var textColor: String? = null
        set(value) {
            field = if (value == null) {
                value
            } else {
                processColorValue(value)
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

    var textSize: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[0-9]+sp"))) {
                value
            } else if (value.matches(Regex("[0-9]+"))) {
                value + "sp"
            } else {
                throw IllegalArgumentException("textSize must be a valid size")
            }
        }
    var gravity: String? = null
        set(value) {
            field = when (value) {
                "center", "center_vertical", "center_horizontal", "start", "end", "top", "bottom" -> value
                else -> throw IllegalArgumentException("gravity must be one of the following: center, center_vertical, center_horizontal, start, end, top, bottom")
            }
        }
    var textStyle: String? = null
        set(value) {
            field = when (value) {
                "bold", "italic", "bold_italic" -> value
                else -> throw IllegalArgumentException("textStyle must be one of the following: bold, italic, bold_italic")
            }
        }
    var paddingStart: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[0-9]+dp"))) {
                value
            } else if (value.matches(Regex("[0-9]+"))) {
                value + "dp"
            } else {
                throw IllegalArgumentException("paddingStart must be a valid size")
            }
        }
    var paddingEnd: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[0-9]+dp"))) {
                value
            } else if (value.matches(Regex("[0-9]+"))) {
                value + "dp"
            } else {
                throw IllegalArgumentException("paddingEnd must be a valid size")
            }
        }
    var paddingTop: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[0-9]+dp"))) {
                value
            } else if (value.matches(Regex("[0-9]+"))) {
                value + "dp"
            } else {
                throw IllegalArgumentException("paddingTop must be a valid size")
            }
        }
    var paddingBottom: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[0-9]+dp"))) {
                value
            } else if (value.matches(Regex("[0-9]+"))) {
                value + "dp"
            } else {
                throw IllegalArgumentException("paddingBottom must be a valid size")
            }
        }
    var marginStart: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[0-9]+dp"))) {
                value
            } else if (value.matches(Regex("[0-9]+"))) {
                value + "dp"
            } else {
                throw IllegalArgumentException("marginStart must be a valid size")
            }
        }
    var marginEnd: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[0-9]+dp"))) {
                value
            } else if (value.matches(Regex("[0-9]+"))) {
                value + "dp"
            } else {
                throw IllegalArgumentException("marginEnd must be a valid size")
            }
        }
    var marginTop: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[0-9]+dp"))) {
                value
            } else if (value.matches(Regex("[0-9]+"))) {
                value + "dp"
            } else {
                throw IllegalArgumentException("marginTop must be a valid size")
            }
        }
    var marginBottom: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[0-9]+dp"))) {
                value
            } else if (value.matches(Regex("[0-9]+"))) {
                value + "dp"
            } else {
                throw IllegalArgumentException("marginBottom must be a valid size")
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
                .append(",\"text\":\"$text\"")
            if (textColor != null)
                sb.append(",\"textColor\":\"$textColor\"")
            if (textSize != null)
                sb.append(",\"textSize\":\"$textSize\"")
            if (gravity != null)
                sb.append(",\"gravity\":\"$gravity\"")
            if (textStyle != null)
                sb.append(",\"textStyle\":\"$textStyle\"")
            if (paddingStart != null)
                sb.append(",\"paddingStart\":\"$paddingStart\"")
            if (paddingEnd != null)
                sb.append(",\"paddingEnd\":\"$paddingEnd\"")
            if (paddingTop != null)
                sb.append(",\"paddingTop\":\"$paddingTop\"")
            if (paddingBottom != null)
                sb.append(",\"paddingBottom\":\"$paddingBottom\"")
            if (marginStart != null)
                sb.append(",\"marginStart\":\"$marginStart\"")
            if (marginEnd != null)
                sb.append(",\"marginEnd\":\"$marginEnd\"")
            if (marginTop != null)
                sb.append(",\"marginTop\":\"$marginTop\"")
            if (marginBottom != null)
                sb.append(",\"marginBottom\":\"$marginBottom\"")
            sb.append("}").toString()
        }
    }
}