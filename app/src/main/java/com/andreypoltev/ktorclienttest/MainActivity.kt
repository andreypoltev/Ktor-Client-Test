package com.andreypoltev.ktorclienttest

import android.content.Context
import android.net.http.HttpResponseCache.install
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.andreypoltev.ktorclienttest.models.ResponseModelPhotos
import com.andreypoltev.ktorclienttest.ui.theme.KtorClientTestTheme
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val list = produceState<List<ResponseModelPhotos>>(initialValue = emptyList(),
                producer = {
                    value = getData(this@MainActivity)
                })

            KtorClientTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CreateList(list.value)

                }
            }
        }
    }
}

@Composable
fun CreateList(list: List<ResponseModelPhotos>) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        itemsIndexed(list) { _, item ->
            CreateCard(item)
            Spacer(Modifier.size(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCard(item: ResponseModelPhotos) {

    Card(
        onClick = { /*TODO*/ },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.fillMaxWidth()) {

            AsyncImage(
                model = item.thumbnailUrl,
                contentDescription = "Thumbnail",
                Modifier.size(150.dp)
            )
            Text(text = item.title, Modifier.padding(8.dp))
        }
    }
}

suspend fun getData(
    context: Context,
): List<ResponseModelPhotos> {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    val response = client.get(Links.PHOTOS)
//    val response = client.request(Links.PHOTOS)
    client.close()

    if (response.status.value == 200)
        Toast.makeText(
            context,
            "Connected successfully.",
            Toast.LENGTH_SHORT
        ).show()
    else
        Toast.makeText(
            context,
            "Something went wrong.\n${response.status.description}\nResponse code: ${response.status.value}.",
            Toast.LENGTH_SHORT
        ).show()

    return response.body()
}