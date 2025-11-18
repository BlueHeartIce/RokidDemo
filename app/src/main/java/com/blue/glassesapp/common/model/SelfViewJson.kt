package com.blue.glassesapp.common.model

class SelfViewJson {
    var type: String = ""
    var props: String = ""
    var children: List<SelfViewJson>? = null

    fun toJson(): String {
        return if (type.isEmpty()){
            throw Exception("type is empty")
        }else{
            val rb = StringBuilder("{").append("\"type\":\"$type\"")
                .append(",\"props\":$props")
            children?.let {
                rb.append(",\"children\":[")
                for (child in children!!){
                    rb.append(child.toJson())
                    rb.append(",")
                }
                rb.deleteCharAt(rb.length - 1)
                rb.append("]")
            }
            rb.append("}")
            rb.toString()
        }
    }
}