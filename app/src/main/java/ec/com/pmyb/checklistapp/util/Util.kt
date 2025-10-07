package ec.com.pmyb.checklistapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Util {
    fun toSimpleString(date: Date) : String {
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        return format.format(date)
    }
}