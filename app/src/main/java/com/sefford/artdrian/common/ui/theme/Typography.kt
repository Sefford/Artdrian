package com.sefford.artdrian.common.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sefford.artdrian.R

val jetbrainsMono = FontFamily(
    Font(R.font.jetbrains_mono_regular),
)

val artdrianTypography = Typography().let { typography ->
    typography.copy(
        displayLarge = typography.displayLarge.setJetbrainsMono(),
        displayMedium = typography.displayMedium.setJetbrainsMono(),
        displaySmall = typography.displaySmall.setJetbrainsMono(),
        headlineLarge = typography.headlineLarge.setJetbrainsMono(),
        headlineMedium = typography.headlineMedium.setJetbrainsMono(),
        headlineSmall = typography.headlineSmall.setJetbrainsMono(),
        titleLarge = typography.titleLarge.setJetbrainsMono(),
        titleMedium = typography.titleMedium.setJetbrainsMono(),
        titleSmall = typography.titleSmall.setJetbrainsMono(),
        bodyLarge = typography.bodyLarge.setJetbrainsMono(),
        bodyMedium = typography.bodyMedium.setJetbrainsMono(),
        bodySmall = typography.bodySmall.setJetbrainsMono(),
        labelLarge = typography.labelLarge.setJetbrainsMono(),
        labelMedium = typography.labelMedium.setJetbrainsMono(),
        labelSmall = typography.labelSmall.setJetbrainsMono()
    )
}

fun TextStyle.bold() = copy(fontWeight = FontWeight.Bold)

private fun TextStyle.setJetbrainsMono() = copy(fontFamily = jetbrainsMono)

@Composable
fun TypographyPreview() {
    val typography = MaterialTheme.typography
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Typography Preview", style = typography.headlineMedium.bold())
        Spacer(modifier = Modifier.height(16.dp))
        listOf(
            "Display Large" to typography.displayLarge,
            "Display Medium" to typography.displayMedium,
            "Display Small" to typography.displaySmall,
            "Headline Large" to typography.headlineLarge,
            "Headline Medium" to typography.headlineMedium,
            "Headline Small" to typography.headlineSmall,
            "Title Large" to typography.titleLarge,
            "Title Medium" to typography.titleMedium,
            "Title Small" to typography.titleSmall,
            "Body Large" to typography.bodyLarge,
            "Body Medium" to typography.bodyMedium,
            "Body Small" to typography.bodySmall,
            "Label Large" to typography.labelLarge,
            "Label Medium" to typography.labelMedium,
            "Label Small" to typography.labelSmall,
        ).forEach { (label, textStyle) ->
            Column {
                Text(text = label, style = typography.labelMedium.bold(), color = MaterialTheme.colorScheme.primary)
                Text(text = "Sample text", style = textStyle)
            }
        }
    }
}

@Composable
@Preview(widthDp = 420, heightDp = 1000)
fun PreviewTypography() {
    ArtdrianTheme {
        TypographyPreview()
    }
}
