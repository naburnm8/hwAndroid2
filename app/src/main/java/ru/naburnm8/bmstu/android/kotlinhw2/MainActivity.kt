package ru.naburnm8.bmstu.android.kotlinhw2

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.*
import ru.naburnm8.bmstu.android.kotlinhw2.net.GiphyRepository
import ru.naburnm8.bmstu.android.kotlinhw2.net.models.GifData
import ru.naburnm8.bmstu.android.kotlinhw2.net.models.GiphyListResponse
import ru.naburnm8.bmstu.android.kotlinhw2.net.models.GiphySingletResponse

import ru.naburnm8.bmstu.android.kotlinhw2.ui.theme.KotlinHw2Theme

class MainActivity : ComponentActivity() {
    private val giphyRepository by lazy { GiphyRepository(BuildConfig.GIPHY_URL) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                TrendingGifsScreen(giphyRepository)
            }
        }

    @Composable
    fun TrendingGifsScreen(giphyRepository: GiphyRepository) {
        var gifList by rememberSaveable { mutableStateOf<List<GifData>?>(null) }
        var dialogueShown by rememberSaveable {mutableStateOf(false)}

        val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
        val coroutineScope = rememberCoroutineScope()
        val handler = CoroutineExceptionHandler { _, exception ->
            run {
                Log.println(
                    Log.ERROR,
                    "MainActivity: Request",
                    "Exception:" + (exception.message ?: "")
                )
                //Toast.makeText(baseContext, exception.message ?: "", Toast.LENGTH_LONG).show()
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            if (isLandscape) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(10.dp)
                )
                {
                    items(gifList ?: emptyList()) { gifData ->
                        GifItem(gifData)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                )
                {
                    items(gifList ?: emptyList()) { gifData ->
                        GifItem(gifData)
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    coroutineScope.launch(handler) {
                        try {
                            val response = giphyRepository.requestNTrendingGifs(BuildConfig.API_KEY, 20)
                            gifList = response?.data
                        } catch (e: Exception){
                            Toast.makeText(baseContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }, modifier = Modifier.weight(1f)) {
                    Text(text = getString(R.string.updateGifsTrending))
                }
                Button(onClick = {
                    dialogueShown = true;
                }) {Text(getString(R.string.gifSearch))}
                if(dialogueShown){
                    SearchDialogue(onDismiss = {dialogueShown = false}, onConfirm = {
                        if (it == ""){
                            Toast.makeText(baseContext, R.string.mustBeNonNull, Toast.LENGTH_LONG).show()
                        }
                        else{
                                coroutineScope.launch(handler) {
                                    try {
                                        val response = giphyRepository.requestNQueryGifs(BuildConfig.API_KEY, 20, it)
                                        gifList = response?.data
                                    } catch (e: Exception){
                                        Toast.makeText(baseContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }

                    })
                }

            }
        }
    }

    @Composable
    fun GifItem(gifData: GifData, modifier : Modifier = Modifier) {
        Column(modifier = modifier.fillMaxWidth().padding(8.dp)) {
            AndroidView(factory = { context -> ImageView(context).apply {
                scaleType = ImageView.ScaleType.FIT_CENTER
            } }, modifier = Modifier.fillMaxSize(),
                update = {imageView -> Glide.with(imageView.context).load(gifData.images.original.url).diskCacheStrategy(
                    DiskCacheStrategy.ALL)
                    .into(imageView) }
            )
            Text(text = gifData.title, modifier = Modifier.fillMaxWidth().padding(top = 8.dp), textAlign = TextAlign.Center)
        }
    }


    @Composable
    fun SearchDialogue(modifier: Modifier = Modifier, onDismiss: () -> Unit, onConfirm: (String) -> Unit){
        var inputText by remember {mutableStateOf("")}
        AlertDialog(
            text = {
                Column{
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text(getString(R.string.typeAQuery)) }
                    )
                }
            },
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm(inputText)
                        onDismiss()
                    }
                ) {
                    Text(getString(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(getString(R.string.cancel))
                }
            }
        )
    }
}




