package com.sb.equalizer.presentation.ui.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sb.core.composable.ClickableIcon
import com.sb.core.resources.AppRes
import com.sb.domain.entity.Profile


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilesDropDownMenu(
    modifier: Modifier = Modifier,
    selectedProfile: Profile,
    list: List<Profile>,
    onSelected: (Profile) -> Unit,
    onDelete: (Profile) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            value = selectedProfile.name,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = {
                Text(
                    text = AppRes.strings.profile,
                    color = AppRes.colors.secondary,
                    fontSize = 10.sp,
                    style = AppRes.type.gilroy
                )
            },
            trailingIcon = {
                val rotation by animateFloatAsState(if (expanded) 180f else 0f)
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation)
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = ExposedDropdownMenuDefaults.textFieldColors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = AppRes.colors.secondary.copy(alpha = 0.1f),
                unfocusedContainerColor = AppRes.colors.secondary.copy(alpha = 0.1f),
            )
        )
        if (list.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = RoundedCornerShape(12.dp),
            ) {
                list.forEach { profile ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = profile.name,
                                color = AppRes.colors.primary,
                                fontSize = 14.sp,
                                style = AppRes.type.gilroyMedium
                            )
                        },
                        trailingIcon = {
                            ClickableIcon(
                                modifier = Modifier.size(24.dp),
                                imageVector = AppRes.icons.trash,
                                tint = AppRes.colors.gray,
                                rippleRadius = 20.dp,
                                onClick = { onDelete(profile) }
                            )
                        },
                        onClick = {
                            expanded = false
                            onSelected(profile)
                        },
                    )
                }
            }
        }
    }
}
