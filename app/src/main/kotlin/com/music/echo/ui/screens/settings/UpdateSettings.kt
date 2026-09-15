package iad1tya.echo.music.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.MaterialTheme
import iad1tya.echo.music.LocalPlayerAwareWindowInsets
import iad1tya.echo.music.R
import iad1tya.echo.music.ui.component.IconButton
import iad1tya.echo.music.ui.component.Material3SettingsGroup
import iad1tya.echo.music.ui.component.Material3SettingsItem
import iad1tya.echo.music.echomusic.updater.getAutoUpdateCheckSetting
import iad1tya.echo.music.echomusic.updater.saveAutoUpdateCheckSetting
import iad1tya.echo.music.echomusic.updater.getUpdateAvailableState
import iad1tya.echo.music.echomusic.updater.saveUpdateAvailableState
import iad1tya.echo.music.echomusic.updater.getUpdateNotificationsSetting
import iad1tya.echo.music.echomusic.updater.saveUpdateNotificationsSetting
import iad1tya.echo.music.echomusic.updater.getBetaUpdatesSetting
import iad1tya.echo.music.echomusic.updater.saveBetaUpdatesSetting
import iad1tya.echo.music.BuildConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateSettings(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val context = LocalContext.current
    var autoUpdateEnabled by remember { mutableStateOf(getAutoUpdateCheckSetting(context)) }
    var updateNotificationsEnabled by remember { mutableStateOf(getUpdateNotificationsSetting(context)) }
    var betaUpdatesEnabled by remember { mutableStateOf(getBetaUpdatesSetting(context)) }
    val isUpdateAvailable = getUpdateAvailableState(context) && autoUpdateEnabled

    Column(
        Modifier
            .windowInsetsPadding(LocalPlayerAwareWindowInsets.current)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        Material3SettingsGroup(
            items = listOf(
                Material3SettingsItem(
                    icon = painterResource(R.drawable.update),
                    title = { Text(stringResource(R.string.system_update)) },
                    description = {
                        if (isUpdateAvailable) {
                            Text(
                                text = stringResource(R.string.update_available),
                                color = MaterialTheme.colorScheme.error
                            )
                        } else {
                            Text(stringResource(R.string.app_update_uptodate))
                        }
                    },
                    onClick = { navController.navigate("update") }
                ),
                Material3SettingsItem(
                    icon = painterResource(R.drawable.info),
                    title = {
                        Text(stringResource(R.string.version, BuildConfig.VERSION_NAME))
                    },
                    description = {
                        val arch = BuildConfig.ARCHITECTURE
                        val variant = if (BuildConfig.CAST_AVAILABLE) "GMS" else "FOSS"
                        Text("$arch - $variant · Ver cambios de esta versión")
                    },
                    onClick = { navController.navigate("settings/changelog") }
                ),
                Material3SettingsItem(
                    icon = painterResource(R.drawable.update),
                    title = { Text(stringResource(R.string.auto_update_check)) },
                    description = { Text(stringResource(R.string.auto_update_check_subtitle)) },
                    trailingContent = {
                        Switch(
                            checked = autoUpdateEnabled,
                            onCheckedChange = { enabled ->
                                autoUpdateEnabled = enabled
                                saveAutoUpdateCheckSetting(context, enabled)
                                if (!enabled) {
                                    saveUpdateAvailableState(context, false)
                                }
                            },
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (autoUpdateEnabled) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = {
                        autoUpdateEnabled = !autoUpdateEnabled
                        saveAutoUpdateCheckSetting(context, autoUpdateEnabled)
                        if (!autoUpdateEnabled) {
                            saveUpdateAvailableState(context, false)
                        }
                    }
                ),
                Material3SettingsItem(
                    icon = painterResource(R.drawable.notification),
                    title = { Text(stringResource(R.string.update_notifications)) },
                    description = { Text(stringResource(R.string.update_notifications_subtitle)) },
                    trailingContent = {
                        Switch(
                            checked = updateNotificationsEnabled,
                            onCheckedChange = { enabled ->
                                updateNotificationsEnabled = enabled
                                saveUpdateNotificationsSetting(context, enabled)
                            },
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (updateNotificationsEnabled) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = {
                        updateNotificationsEnabled = !updateNotificationsEnabled
                        saveUpdateNotificationsSetting(context, updateNotificationsEnabled)
                    }
                )
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
    }

    TopAppBar(
        title = { Text(stringResource(R.string.update_settings_title)) },
        navigationIcon = {
            IconButton(
                onClick = navController::navigateUp,
                onLongClick = null
            ) {
                Icon(
                    painterResource(R.drawable.arrow_back),
                    contentDescription = null
                )
            }
        }
    )
}