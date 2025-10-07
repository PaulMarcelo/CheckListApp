package ec.com.pmyb.checklistapp.ui.theme

import androidx.compose.ui.graphics.Color
object AppColors {
    val IndigoBlueDark = Color(0xFF303F9F)
    val IndigoBlueLight = Color(0xFFAAB1E2)
    val White = Color(0xFFFCFDFD)
    val White01 = Color(0xFFC4C7C7)
    val Black = Color(0xFF020202)
    val Black01 = Color(0xFF1F1E1E)
    val Black02 = Color(0xFF2B2222)
    val Black03 = Color(0xFF3C4463)
    val PinkLight = Color(0xFFE7DFE1)
    val PinkLight2 = Color(0xFFACA6B1)
    val Red2 = Color(0xFFCC6B4B)
    val Gray1 = Color(0xFFDFD7CB)
    val Gray2 = Color(0xFF504C4B)
    val Gray3 = Color(0xFF979592)
    val Gray4 = Color(0xFF504C4B)

}

sealed class ThemeColors(
    val bacground: Color,
    val surafce: Color,
    val primary: Color,
    val text: Color
)  {
    object Night: ThemeColors(
        surafce = AppColors.IndigoBlueDark,
        bacground = AppColors.Black01,
        primary = AppColors.IndigoBlueDark,
        text = Color.White
    )
    object Day: ThemeColors(
        surafce = AppColors.IndigoBlueLight,
        bacground = AppColors.White01,
        primary = AppColors.IndigoBlueLight,
        text = Color.Black
    )
}