package com.blue.glassesapp.common.model

import kotlin.collections.iterator

class UpdateViewJson {
    val updateList = mutableListOf<UpdateJson>()
    class UpdateJson(val id: String){
        val action = "update"
        var props: HashMap<String, String> = HashMap()
    }

    fun toJson(): String {
        val sb: StringBuilder = StringBuilder("[")

        for (update in updateList) {
            sb.append("{")
            sb.append("\"action\":\"${update.action}\",")
            sb.append("\"id\":\"${update.id}\",")
            sb.append("\"props\":{")

            for ((key, value) in update.props) {
                sb.append("\"$key\":\"$value\",")
            }

            sb.deleteCharAt(sb.length - 1)
            sb.append("}")
            sb.append("},")
        }
        sb.deleteCharAt(sb.length - 1)

        sb.append("]")
        return sb.toString()
    }
}