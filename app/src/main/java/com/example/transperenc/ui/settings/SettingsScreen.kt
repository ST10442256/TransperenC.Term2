package com.example.transperenc.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.example.transperenc.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    val languages = mapOf(
        "English" to "en",
        "isiZulu" to "zu",
        "Afrikaans" to "af",
        "Sesotho" to "st",
        "TshiVenda" to "ve"
    )
    
    val currentLocale = AppCompatDelegate.getApplicationLocales().get(0)?.language ?: "en"
    var selectedLanguage by remember { 
        mutableStateOf(languages.entries.find { it.value == currentLocale }?.key ?: "English") 
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource(R.string.language_selection), style = MaterialTheme.typography.titleMedium)
            languages.keys.forEach { language ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (language == selectedLanguage),
                        onClick = { 
                            selectedLanguage = language
                            val localeCode = languages[language] ?: "en"
                            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(localeCode)
                            AppCompatDelegate.setApplicationLocales(appLocale)
                        }
                    )
                    Text(text = language, modifier = Modifier.padding(start = 8.dp))
                }
            }
            HorizontalDivider()
            Text("App Version: 1.0.0", style = MaterialTheme.typography.bodySmall)
        }
    }
}
