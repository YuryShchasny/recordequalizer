package com.sb.equalizer.presentation.ui.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sb.core.R
import com.sb.core.resources.AppRes
import com.sb.core.resources.theme.EqualizerTheme
import com.sb.domain.entity.Profile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveDialog(
    modifier: Modifier = Modifier,
    profiles: List<Profile>,
    show: Boolean,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (show) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                modifier = modifier,
                color = AppRes.colors.backgroundContrast,
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 8.dp,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                ) {
                    Text(
                        text = stringResource(R.string.save_new_profile),
                        color = AppRes.colors.primary,
                        style = AppRes.type.gilroySemibold,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(16.dp))
                    var profileName by remember { mutableStateOf("") }
                    var hasError by remember { mutableStateOf(false) }
                    LaunchedEffect(profileName) {
                        hasError = profiles.any { it.name == profileName }
                    }
                    BasicTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .border(
                                width = 1.dp,
                                color = if (hasError) AppRes.colors.red else AppRes.colors.primary,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp),
                        value = profileName,
                        onValueChange = { profileName = it },
                        textStyle = AppRes.type.gilroy.copy(AppRes.colors.primary),
                        singleLine = true,
                        cursorBrush = SolidColor(AppRes.colors.primary),
                        decorationBox = { innerTextField ->
                            TextFieldDefaults.DecorationBox(
                                value = profileName,
                                innerTextField = innerTextField,
                                enabled = true,
                                singleLine = true,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = remember { MutableInteractionSource() },
                                placeholder = {
                                    Text(
                                        text = stringResource(R.string.new_profile_name),
                                        color = AppRes.colors.primary.copy(0.8f),
                                        style = AppRes.type.gilroy,
                                        fontSize = 14.sp
                                    )
                                },
                                contentPadding = PaddingValues(0.dp),
                                colors = TextFieldDefaults.colors().copy(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    errorContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    errorIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),
                            )
                        }
                    )
                    if (hasError) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.profile_exists),
                            style = AppRes.type.gilroy,
                            fontSize = 10.sp,
                            color = AppRes.colors.red,
                        )
                    }
                    Spacer(Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.align(Alignment.End),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.clickable(
                                interactionSource = null,
                                indication = ripple(radius = 16.dp, bounded = false),
                                onClick = { onDismiss() }
                            ),
                            text = stringResource(R.string.cancel),
                            fontSize = 16.sp,
                            style = AppRes.type.gilroy,
                            color = AppRes.colors.primary.copy(0.8f)
                        )
                        Text(
                            modifier = Modifier.clickable(
                                interactionSource = null,
                                indication = ripple(radius = 16.dp, bounded = false),
                                onClick = {
                                    if (!hasError) {
                                        onSave(profileName)
                                        onDismiss()
                                    }
                                }
                            ),
                            text = stringResource(R.string.save),
                            fontSize = 18.sp,
                            style = AppRes.type.gilroyMedium,
                            color = AppRes.colors.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SaveDialogPreview() {
    EqualizerTheme {
        SaveDialog(
            profiles = listOf(),
            show = true,
            onSave = {},
            onDismiss = {}
        )
    }
}