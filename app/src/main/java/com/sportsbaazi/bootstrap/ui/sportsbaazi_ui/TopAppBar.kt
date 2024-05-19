package com.sportsbaazi.bootstrap.ui.sportsbaazi_ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sportsbaazi.bootstrap.R
import com.sportsbaazi.bootstrap.ui.style.categoryTitleStyle

/**
 * Created by Akash on 28/08/20
 */

@Composable
fun TopAppBars(@StringRes titleResource: Int, onThemeSwitch: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            text = stringResource(id = titleResource),
            style = categoryTitleStyle
        )
        Box(modifier = Modifier.padding(top = 24.dp, end = 8.dp)) {
            ThemeSwitcher(onThemeSwitch = {
                onThemeSwitch()
            })
        }
    }
}

@Composable
fun ThemeSwitcher(onThemeSwitch: () -> Unit) {
    val isDark = remember { mutableStateOf(false) }
    @DrawableRes val light = R.drawable.ic_light
    @DrawableRes val dark = R.drawable.ic_dark
    IconButton(onClick = {
        onThemeSwitch()
        isDark.value = !isDark.value
    }) {
        Icon(
            painter = if (isDark.value) painterResource(light) else painterResource(dark),
            contentDescription = "Theme Switcher",
            tint = Color.White
        )
    }
}
