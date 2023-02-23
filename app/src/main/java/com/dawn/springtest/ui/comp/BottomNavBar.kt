package com.dawn.springtest.ui.comp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.baec23.ludwig.component.toggleable.ToggleableIcon
import com.dawn.springtest.ui.screen.finishtodolist.finishTodoListScreenRoute
import com.dawn.springtest.ui.screen.todolist.todoListScreenRoute

val bottomNavBarItem = listOf(
    BottomNavBarItem(
        route = todoListScreenRoute,
        iconImageVector = Icons.Default.Home,
        label = "Home"
    ),
    BottomNavBarItem(
        route = finishTodoListScreenRoute,
        iconImageVector = Icons.Default.Menu,
        label = "Home"
    ),
)

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    items: List<BottomNavBarItem>,
    currNavScreenRoute: String?,
    onBottomNavBarButtonPressed: (BottomNavBarItem) -> Unit,
) {
    NavigationBar(modifier = modifier) {
        items.forEach { item ->
            ToggleableIcon(
                modifier = Modifier.weight(1f),
                isToggled = item.route == currNavScreenRoute,
                imageVector = item.iconImageVector,
            ) {
                onBottomNavBarButtonPressed(item)
            }
        }
    }
}

data class BottomNavBarItem(
    val iconImageVector: ImageVector,
    val route: String,
    val label: String? = null,
)