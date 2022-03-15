package com.bilalov.task

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.bilalov.task.data.request.ListItemResponse
import com.bilalov.task.data.request.UrlItem
import com.bilalov.task.ui.theme.WalletTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private var arrayItems: List<String>? = null
private var connectionStatus: Boolean = false

class MainActivity : ComponentActivity() {
    private val getData = GetData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("TTT", "generate json")
        val service = UrlItem.retrofitInstance?.create(ListItemResponse::class.java)
        val call: Call<List<String>>? = service?.getAllItems()
        call?.enqueue(object: Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                val body: List<String>? = response.body()
                arrayItems = body
                Log.e("BODY", body.toString())
                Log.e("ELEMENT_FIRST", body?.get(0).toString())
                Log.e("ELEMENT_FIRST", body?.size.toString())
                connectionStatus = true
                setContent {
                    WalletTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.background,
                        ) {
                            DefaultPreview()
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Toast.makeText(applicationContext ,"Error, connection is lost", Toast.LENGTH_LONG).show()
            }
        })
        lifecycle.addObserver(getData)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Row(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    )
    {
        SwipeRefreshCompose()
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun SwipeRefreshCompose() {
    val context: Context = LocalContext.current

    val windowInfo = rememberWindowInfo()

    val isFullScreenOpenFirst = remember {
        mutableStateOf(false)
    }
    val isFullScreenOpenSecond = remember {
        mutableStateOf(false)
    }
    val isFullScreenOpenThird = remember {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        var refreshing by rememberSaveable { mutableStateOf(false) }
        val countImage = rememberSaveable { mutableStateOf(0) }
        val countItem = arrayItems?.size
        LaunchedEffect(refreshing) {
            if (refreshing) {
                delay(1000)
                refreshing = false
                checkApiStatus()
                if (!checkApiStatus()){
                    Toast.makeText(context ,"Error, connection is lost", Toast.LENGTH_LONG).show()
                }
            }
        }
        if (windowInfo.screenInfoWidth is WindowInfo.WindowType.Compact) {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = refreshing),
                onRefresh = {
                    refreshing = true
                    Log.e("TTT_START_REFRESH", "${countImage.value}")
                    if (countImage.value < countItem!! - 2) {
                        Log.e("TTT_COUNT", "windowInfo_Compact")
                        countImage.value += 2
                        Log.e("TTT_COUNT", "${countImage.value}")
                    } else {
                        countImage.value = 0
                        Log.e("TTT_COUNT", "${countImage.value}")
                    }
                },
            ) {
                LazyColumn(
                    modifier = Modifier
                        .background(Color.Black)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(count = 1) {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(
                                modifier = Modifier
                                    .height(200.dp)
                                    .width(200.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceAround
                            ) {
                                val painterFirst = rememberImagePainter(
                                    data = arrayItems?.get(countImage.value).toString(),
                                    builder = {
                                        placeholder(R.drawable.ic_baseline_find_replace_24)
                                        error(R.drawable.error_img)
                                        crossfade(1000)
                                    })
                                ShowFullScreen(
                                    isFullScreenOpen = isFullScreenOpenFirst,
                                    painter = painterFirst
                                )
                                Image(
                                    painter = painterFirst,
                                    contentDescription = "cardBackground",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize(100f)
                                        .clickable {
                                            isFullScreenOpenFirst.value = true
                                        },
                                )
                            }

                            Spacer(modifier = Modifier.padding(5.dp))
                            Column(
                                modifier = Modifier
                                    .height(200.dp)
                                    .width(200.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                val painterSecond = rememberImagePainter(
                                    data = arrayItems?.get(countImage.value + 1).toString(),
                                    builder = {
                                        placeholder(R.drawable.ic_baseline_find_replace_24)
                                        error(R.drawable.error_img)
                                        crossfade(1000)
                                    })
                                ShowFullScreen(
                                    isFullScreenOpen = isFullScreenOpenSecond,
                                    painter = painterSecond
                                )
                                Image(
                                    painter = painterSecond,
                                    contentDescription = "cardBackground",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize(100f)
                                        .clickable {
                                            isFullScreenOpenSecond.value = true
                                        },
                                )
                            }
                        }
                    }
                }
            }
        }
            if (windowInfo.screenInfoWidth is WindowInfo.WindowType.Expanded){
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = refreshing),
                    onRefresh = { refreshing = true
                        if (countImage.value < countItem!!-3) {
                            countImage.value += 3
                            Log.e("TTT_COUNT","${countImage.value}")
                        } else{
                            countImage.value = 0
                            Log.e("TTT_COUNT","${countImage.value}")
                        }},
                ) {
                LazyColumn(modifier = Modifier
                    .background(Color.Black)
                    .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(count = 1) {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(
                                modifier = Modifier
                                    .height(200.dp)
                                    .width(200.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceAround
                            ) {
                                val painterFirst = rememberImagePainter(
                                    data = arrayItems?.get(countImage.value).toString(),
                                    builder = {
                                        placeholder(R.drawable.ic_baseline_find_replace_24)
                                        error(R.drawable.error_img)
                                        crossfade(1000)
                                    })
                                ShowFullScreen(isFullScreenOpen = isFullScreenOpenFirst, painter =painterFirst)
                                Image(
                                    painter = painterFirst,
                                    contentDescription = "cardBackground",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize(100f)
                                        .clickable {
                                            isFullScreenOpenFirst.value = true
                                        },
                                )
                            }
                            Spacer(modifier = Modifier.padding(5.dp))

                            Column(modifier = Modifier
                                .height(200.dp)
                                .width(200.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                val painterSecond = rememberImagePainter(
                                    data = arrayItems?.get(countImage.value+1).toString(),
                                    builder = {
                                        placeholder(R.drawable.ic_baseline_find_replace_24)
                                        error(R.drawable.error_img)
                                        crossfade(1000)
                                    })
                                ShowFullScreen(isFullScreenOpen = isFullScreenOpenSecond, painter =painterSecond)
                                Image(
                                    painter = painterSecond,
                                    contentDescription = "cardBackground",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize(100f)
                                        .clickable {
                                            isFullScreenOpenSecond.value = true
                                        },
                                )
                            }
                            Spacer(modifier = Modifier.padding(5.dp))

                            Column(modifier = Modifier
                                .height(200.dp)
                                .width(200.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                val painterThird = rememberImagePainter(
                                    data = arrayItems?.get(countImage.value+2).toString(),
                                    builder = {
                                        placeholder(R.drawable.ic_baseline_find_replace_24)
                                        error(R.drawable.error_img)
                                        crossfade(1000)
                                    })
                                ShowFullScreen(isFullScreenOpen = isFullScreenOpenThird, painter = painterThird)
                                Image(
                                    painter = painterThird,
                                    contentDescription = "cardBackground",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize(100f)
                                        .clickable {
                                            isFullScreenOpenThird.value = true
                                                   },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun checkApiStatus(): Boolean {
    Log.e("TTT", "checkApiStatus json")
    val service = UrlItem.retrofitInstance?.create(ListItemResponse::class.java)
    val call: Call<List<String>>? = service?.getAllItems()
    call?.enqueue(object: Callback<List<String>> {
        override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
            val body: List<String>? = response.body()
            arrayItems = body
            connectionStatus = true
        }
        override fun onFailure(call: Call<List<String>>, t: Throwable) {
            connectionStatus = false
        }
    })
    return connectionStatus
}





