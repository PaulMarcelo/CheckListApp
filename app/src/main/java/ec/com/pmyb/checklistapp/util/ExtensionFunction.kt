package ec.com.pmyb.checklistapp.util

import java.text.SimpleDateFormat
import java.util.Date

fun Date.toSimpleString() : String {
    val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    return format.format(this)
}