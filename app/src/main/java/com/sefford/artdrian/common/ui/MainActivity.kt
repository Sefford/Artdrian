package com.sefford.artdrian.common.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sefford.artdrian.common.ui.navigation.Artdrian
import com.sefford.artdrian.common.ui.theme.ArtdrianTheme
import com.sefford.artdrian.common.utils.graph

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtdrianTheme {
                Artdrian(graph)
            }
        }
    }
}
