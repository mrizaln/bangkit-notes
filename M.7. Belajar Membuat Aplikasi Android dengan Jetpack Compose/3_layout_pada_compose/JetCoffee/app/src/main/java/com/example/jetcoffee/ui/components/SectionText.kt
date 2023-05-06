package com.example.jetcoffee.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetcoffee.R
import com.example.jetcoffee.ui.theme.JetCoffeeTheme

@Composable
fun SectionText(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.ExtraBold
        ),
        modifier = modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp
        )
    )
}

@Preview
@Composable
fun SectionTextPreview() {
    JetCoffeeTheme {
        SectionText(title = stringResource(id = R.string.section_category))
    }
}
