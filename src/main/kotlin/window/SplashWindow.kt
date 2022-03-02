package window

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import util.AsyncImage
import util.getIconsFolderPath
import util.loadXmlImageVector
import java.io.File

@Composable
fun SplashContent(onClick: () -> Unit) {
    val density = LocalDensity.current
    Surface(color = Color(0xFF4A90E2)) {
        Box(modifier = Modifier.fillMaxSize().clickable { onClick() }) {
            AsyncImage(
                load = { loadXmlImageVector(File("${getIconsFolderPath()}icon_splash.xml"), density) },
                painterFor = { rememberVectorPainter(it) },
                contentDescription = "Splash",
                tintColor = Color.White,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(200.dp).align(Alignment.Center)
            )
        }
    }
}