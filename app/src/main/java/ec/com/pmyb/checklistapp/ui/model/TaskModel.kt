package ec.com.pmyb.checklistapp.ui.model

import java.util.Date

data class TaskModel(
    val id: Int = System.currentTimeMillis().hashCode(),
    val task: String,
    var selected: Boolean=false,
    var createDate: String,
    var showAnimate:Boolean=false
)