import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sefford.artdrian.R
import com.sefford.artdrian.model.Wallpaper
import com.sefford.artdrian.ui.theme.ArtdrianTheme
import com.sefford.artdrian.wallpaperlist.ui.WallpaperCard
import com.sefford.artdrian.wallpaperlist.ui.WallpaperListViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun WallpaperListScreen(response: WallpaperListViewModel.ViewState, onItemClicked: (String, String) -> Unit = {_,_ ->}) {
    ArtdrianTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name)) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Black,
                        titleContentColor = Color.White
                    )
                )
            },
            content = { innerPadding ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    when (response) {
                        WallpaperListViewModel.ViewState.Loading -> ShowLoading()
                        is WallpaperListViewModel.ViewState.Content -> ShowWallpapers(response.wallpapers, onItemClicked)
                        is WallpaperListViewModel.ViewState.Error -> ShowError(response.error)
                    }
                }
            })
    }
}

@Composable
private fun ShowLoading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center), color = Color.Black
        )
    }
}

@Composable
private fun ShowWallpapers(wallpapers: List<Wallpaper>, onItemClick: (String, String) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(wallpapers) { wallpaper ->
            WallpaperCard(wallpaper = wallpaper, onItemClicked = { onItemClick(wallpaper.metadataDto.id, wallpaper.name) })
        }
    }
}

@Composable
private fun ShowError(errors: WallpaperListViewModel.Errors) {
    val icon = when (errors) {
        is WallpaperListViewModel.Errors.NetworkError -> Icons.Rounded.WifiOff
        is WallpaperListViewModel.Errors.NotFoundError -> Icons.Rounded.QuestionMark
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(icon, modifier = Modifier.size(120.dp), contentDescription = "", tint = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.network_error),
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}

@Preview
@Composable
private fun showLoading() {
    WallpaperListScreen(WallpaperListViewModel.ViewState.Loading)
}

@Preview
@Composable
private fun showContent() {
    WallpaperListScreen(WallpaperListViewModel.ViewState.Content(listOf()))
}

@Preview
@Composable
private fun showError() {
    WallpaperListScreen(WallpaperListViewModel.ViewState.Error(WallpaperListViewModel.Errors.NetworkError(0)))
}
