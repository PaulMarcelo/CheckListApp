package ec.com.pmyb.checklistapp.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import ec.com.pmyb.checklistapp.ui.theme.AppColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    content: @Composable (T) -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    // Creates the CoroutineScope
    val coroutineScope = rememberCoroutineScope()

    // Creates the dismiss state
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            if (state == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }, positionalThreshold = with(LocalDensity.current) {
            { 150.dp.toPx() }
        }
    )
    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            coroutineScope.launch {
                onDelete(item)
            }
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            DeleteBackground(dismissState)
        },
        enableDismissFromStartToEnd = false
    ) {

        content(item)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteBackground(
    swipeDismissState: SwipeToDismissBoxState
) {
    val color =
        if (swipeDismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
            AppColors.Red2
        } else
            Color.Transparent
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(size = 9.dp))
            .background(color)
            .padding(16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}