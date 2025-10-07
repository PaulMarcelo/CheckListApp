package ec.com.pmyb.checklistapp.ui.screen

import android.content.Context
import android.content.pm.PackageManager

object UtilApp {
    fun getVersionName(
        context: Context,
    ): String {
        val manager = context.packageManager
        val info = manager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
        return info.versionName
    }
}
