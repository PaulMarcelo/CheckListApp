package ec.com.pmyb.checklistapp.ui.screen.splashscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import ec.com.pmyb.checklistapp.R
import ec.com.pmyb.checklistapp.ui.navigator.Screen
import ec.com.pmyb.checklistapp.ui.screen.UtilColorApp

@Composable
fun SplashScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)

        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie))
            val logoAnimationState =
                animateLottieCompositionAsState(composition = composition)
            LottieAnimation(
                composition = composition,
                progress = { logoAnimationState.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(
                text = stringResource(R.string.title_app),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 24.sp, fontFamily = FontFamily.Cursive),
                color = UtilColorApp.getTextColor(isSystemInDarkTheme())
            )
            if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        }
    }
}


