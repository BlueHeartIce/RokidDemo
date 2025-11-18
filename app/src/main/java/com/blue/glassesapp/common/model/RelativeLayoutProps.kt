package com.blue.glassesapp.common.model

class RelativeLayoutProps {
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
            field = if (value == "match_parent" || value == "wrap_content" || value.endsWith("dp")) {
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
            field = if (value == "match_parent" || value == "wrap_content" || value.endsWith("dp")) {
                value
            } else if (value.matches(Regex("\\d+"))) {
                "${value}dp"
            } else {
                throw IllegalArgumentException("layout_height must be 'match_parent', 'wrap_content', or a value ending with 'dp'")
            }
        }
    var paddingStart: String? = null
        set(value) {
            field = if (value == null || value.endsWith("dp")) {
                value
            } else if (value.matches(Regex("\\d+"))) {
                "${value}dp"
            } else {
                throw IllegalArgumentException("paddingStart must be null or a value ending with 'dp'")
            }
        }
    var paddingEnd: String? = null
        set(value) {
            field = if (value == null || value.endsWith("dp")) {
                value
            } else if (value.matches(Regex("\\d+"))) {
                "${value}dp"
            } else {
                throw IllegalArgumentException("paddingEnd must be null or a value ending with 'dp'")
            }
        }
    var paddingTop: String? = null
        set(value) {
            field = if (value == null || value.endsWith("dp")) {
                value
            } else if (value.matches(Regex("\\d+"))) {
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
                "${value}dp"
            } else {
                throw IllegalArgumentException("paddingBottom must be null or a value ending with 'dp'")
            }
        }
    var backgroundColor: String? = null
        set(value) {
            field = if (value == null) {
                value
            } else {
                processColorValue(value)
            }
        }

    var marginStart: String? = null
        set(value) {
            field = if (value == null || value.endsWith("dp")) {
                value
            } else if (value.matches(Regex("\\d+"))) {
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
                "${value}dp"
            } else {
                throw IllegalArgumentException("marginBottom must be null or a value ending with 'dp'")
            }
        }
    var layout_toStartOf: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[a-zA-Z0-9_]+"))) {
                value
            } else {
                throw IllegalArgumentException("layout_toStartOf must be null or a valid identifier")
            }
        }
    var layout_toEndOf: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[a-zA-Z0-9_]+"))) {
                value
            } else {
                throw IllegalArgumentException("layout_toEndOf must be null or a valid identifier")
            }
        }
    var layout_above: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[a-zA-Z0-9_]+"))) {
                value
            } else {
                throw IllegalArgumentException("layout_above must be null or a valid identifier")
            }
        }
    var layout_below: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[a-zA-Z0-9_]+"))) {
                value
            } else {
                throw IllegalArgumentException("layout_below must be null or a valid identifier")
            }
        }
    var layout_alignBaseLine: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[a-zA-Z0-9_]+"))) {
                value
            } else {
                throw IllegalArgumentException("layout_alignBaseLine must be null or a valid identifier")
            }
        }
    var layout_alignStart: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[a-zA-Z0-9_]+"))) {
                value
            } else {
                throw IllegalArgumentException("layout_alignStart must be null or a valid identifier")
            }
        }
    var layout_alignEnd: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[a-zA-Z0-9_]+"))) {
                value
            } else {
                throw IllegalArgumentException("layout_alignEnd must be null or a valid identifier")
            }
        }
    var layout_alignTop: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[a-zA-Z0-9_]+"))) {
                value
            } else {
                throw IllegalArgumentException("layout_alignTop must be null or a valid identifier")
            }
        }
    var layout_alignBottom: String? = null
        set(value) {
            field = if (value == null || value.matches(Regex("[a-zA-Z0-9_]+"))) {
                value
            } else {
                throw IllegalArgumentException("layout_alignBottom must be null or a valid identifier")
            }
        }

    var layout_aliginParentStart: String? = null
        set(value) {
            field = if (value == null || value.equals("true", ignoreCase = true) || value.equals("false", ignoreCase = true)){
                value?.lowercase()
            }else{
                throw IllegalArgumentException("layout_aliginParentStart must be null or a valid boolean")
            }
        }
    var layout_aliginParentEnd: String? = null
        set(value) {
            field = if (value == null || value.equals("true", ignoreCase = true) || value.equals("false", ignoreCase = true)){
                value?.lowercase()
            }else{
                throw IllegalArgumentException("layout_aliginParentEnd must be null or a valid boolean")
            }
        }
    var layout_aliginParentTop: String? = null
        set(value) {
            field = if (value == null || value.equals("true", ignoreCase = true) || value.equals("false", ignoreCase = true)){
                value?.lowercase()
            }else{
                throw IllegalArgumentException("layout_aliginParentTop must be null or a valid boolean")
            }
        }
    var layout_aliginParentBottom: String? = null
        set(value) {
            field = if (value == null || value.equals("true", ignoreCase = true) || value.equals("false", ignoreCase = true)){
                value?.lowercase()
            }else{
                throw IllegalArgumentException("layout_aliginParentBottom must be null or a valid boolean")
            }
        }
    var layout_centerInParent: String? = null
        set(value) {
            field = if (value == null || value.equals("true", ignoreCase = true) || value.equals("false", ignoreCase = true)){
                value?.lowercase()
            }else{
                throw IllegalArgumentException("layout_centerInParent must be null or a valid boolean")
            }
        }
    var layout_centerHorizontal: String? = null
        set(value) {
            field = if (value == null || value.equals("true", ignoreCase = true) || value.equals("false", ignoreCase = true)){
                value?.lowercase()
            }else{
                throw IllegalArgumentException("layout_centerHorizontal must be null or a valid boolean")
            }
        }
    var layout_centerVertical: String? = null
        set(value) {
            field = if (value == null || value.equals("true", ignoreCase = true) || value.equals("false", ignoreCase = true)){
                value?.lowercase()
            }else{
                throw IllegalArgumentException("layout_centerVertical must be null or a valid boolean")
            }
        }
}