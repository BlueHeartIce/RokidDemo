package com.blue.glassesapp.common.model

class LinearLayoutProps {

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

    var layout_height: String = "match_parent"
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
    var orientation: String = "vertical"
        set(value) {
            field = when (value) {
                "vertical", "horizontal" -> value
                else -> throw IllegalArgumentException("orientation must be 'vertical' or 'horizontal'")
            }
        }
    var gravity: String? = null
        set(value) {
            field = when (value) {
                "center", "center_vertical", "center_horizontal", "top", "bottom", "start", "end" -> value
                else -> throw IllegalArgumentException("gravity must be 'center', 'center_vertical', 'center_horizontal', 'top', 'bottom', 'left', or 'right'")
            }
        }

    var layout_gravity: String? = null
        set(value) {
            field = when (value) {
                "center", "center_vertical", "center_horizontal", "top", "bottom", "start", "end" -> value
                else -> throw IllegalArgumentException("layout_gravity must be 'center', 'center_vertical', 'center_horizontal', 'top', 'bottom', 'left', or 'right'")
            }
        }
    var paddingTop: String? = null
        set(value) {
            field = if (value == null || value.endsWith("dp")) {
                value
            } else if (value.matches(Regex("\\d+"))) {
                // 如果只提供了数字，则自动添加dp单位
                "${value}dp"
            } else {
                throw IllegalArgumentException("paddingTop must be null or a value ending with 'dp'")
            }
        }
    var paddingBottom: String? = null
        set(value) {
            field = if (value == null || value.endsWith("dp")) {
                value
            } else if (value.matches(Regex("\\d+"))) {
                // 如果只提供了数字，则自动添加dp单位
                "${value}dp"
            } else {
                throw IllegalArgumentException("paddingBottom must be null or a value ending with 'dp'")
            }
        }
    var paddingStart: String? = null
        set(value) {
            field = if (value == null || value.endsWith("dp")) {
                value
            } else if (value.matches(Regex("\\d+"))) {
                // 如果只提供了数字，则自动添加dp单位
                "${value}dp"
            } else {
                throw IllegalArgumentException("paddingStart must be a value ending with 'dp'")
            }
        }
    var paddingEnd: String? = null
        set(value) {
            field = if (value == null || value.endsWith("dp")) {
                value
            } else if (value.matches(Regex("\\d+"))) {
                // 如果只提供了数字，则自动添加dp单位
                "${value}dp"
            } else {
                throw IllegalArgumentException("paddingEnd must be a value ending with 'dp'")
            }
        }

    // background color that only green with alpha
    var backgroundColor: String? = null
        set(value) {
            field = if (value == null) {
                null
            } else {
                processColorValue(value)
            }
        }

    var marginStart: String? = null
        set(value) {
            field = if (value == null || value.endsWith("dp")) {
                value
            } else if (value.matches(Regex("\\d+"))) {
                // 如果只提供了数字，则自动添加dp单位
                "${value}dp"
            } else {
                throw IllegalArgumentException("marginStart must be null or a value ending with 'dp'")
            }
        }

    var marginEnd: String? = null
        set(value) {
            field = if (value == null || value.endsWith("dp")) {
                value
            } else if (value.matches(Regex("\\d+"))) {
                // 如果只提供了数字，则自动添加dp单位
                "${value}dp"
            } else {
                throw IllegalArgumentException("marginEnd must be null or a value ending with 'dp'")
            }
        }

    var marginTop: String? = null
        set(value) {
            field = if (value == null || value.endsWith("dp")) {
                value
            } else if (value.matches(Regex("\\d+"))) {
                // 如果只提供了数字，则自动添加dp单位
                "${value}dp"
            } else {
                throw IllegalArgumentException("marginTop must be null or a value ending with 'dp'")
            }
        }

    var marginBottom: String? = null
        set(value) {
            field = if (value == null || value.endsWith("dp")) {
                value
            } else if (value.matches(Regex("\\d+"))) {
                // 如果只提供了数字，则自动添加dp单位
                "${value}dp"
            } else {
                throw IllegalArgumentException("marginBottom must be null or a value ending with 'dp'")
            }
        }

    fun toJson(): String {
        return if (id.isEmpty()) {
            throw IllegalArgumentException("id can not be empty")
        } else {
            val sb: StringBuilder = StringBuilder().append("{\"id\":\"$id\"")
                .append(",\"layout_width\":\"$layout_width\"")
                .append(",\"layout_height\":\"$layout_height\"")
                .append(",\"orientation\":\"$orientation\"")
            if (gravity != null) {
                sb.append(",\"gravity\":\"$gravity\"")
            }
            if (layout_gravity != null) {
                sb.append(",\"layout_gravity\":\"$layout_gravity\"")
            }
            if (paddingTop != null) {
                sb.append(",\"paddingTop\":\"$paddingTop\"")
            }
            if (paddingBottom != null) {
                sb.append(",\"paddingBottom\":\"$paddingBottom\"")
            }
            if (paddingStart != null) {
                sb.append(",\"paddingLeft\":\"$paddingStart\"")
            }
            if (paddingEnd != null) {
                sb.append(",\"paddingRight\":\"$paddingEnd\"")
            }
            if (backgroundColor != null) {
                sb.append(",\"backgroundColor\":\"$backgroundColor\"")
            }
            if (marginStart != null) {
                sb.append(",\"marginStart\":\"$marginStart\"")
            }
            if (marginEnd != null) {
                sb.append(",\"marginEnd\":\"$marginEnd\"")
            }
            if (marginTop != null) {
                sb.append(",\"marginTop\":\"$marginTop\"")
            }
            if (marginBottom != null) {
                sb.append(",\"marginBottom\":\"$marginBottom\"")
            }
            sb.append("}").toString()
        }
    }
}