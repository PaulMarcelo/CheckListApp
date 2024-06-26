package ec.com.pmyb.checklistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import ec.com.pmyb.checklistapp.ui.screen.TasksScreen
import ec.com.pmyb.checklistapp.ui.theme.CheckListAppTheme
import ec.com.pmyb.checklistapp.ui.viewmodel.TasksViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val taskViewModel: TasksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckListAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.LightGray
                ) {
                    TasksScreen(taskViewModel)
//                    TasksScreen()
                }
            }
        }
    }
}


