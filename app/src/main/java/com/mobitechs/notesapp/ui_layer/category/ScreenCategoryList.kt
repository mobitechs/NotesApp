package com.mobitechs.notesapp.ui_layer.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobitechs.notesapp.Screen
import com.mobitechs.notesapp.data_layer.Category
import com.mobitechs.notesapp.data_layer.ExpenseDetails
import com.mobitechs.notesapp.ui_layer.expense.EventExpense
import com.mobitechs.notesapp.util.ConfirmationDialog

//@Composable
//fun ScreenCategoryList(
////    modifier: Modifier,
//    navController: NavController,
//    categoryState: CategoryState,
//    onEvent: (CategoryEvent) -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFF121212))
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        PurchaseCard(
//            datePurchased = "10th November 2023",
//            orderId = "12345678",
//            price = "₹899",
//            quantity = "10",
//            giftCardsRedeemed = "3 / 10",
//            imageBackgroundColor = Color(0xFF2A3B5A)
//        )
//
//        PurchaseCard(
//            datePurchased = "21st July 2023",
//            orderId = "987654321",
//            price = "₹899",
//            quantity = "5",
//            giftCardsRedeemed = "4 / 5",
//            imageBackgroundColor = Color(0xFF424242)
//        )
//    }
//}
//
//@Composable
//fun PurchaseCard(
//    datePurchased: String,
//    orderId: String,
//    price: String,
//    quantity: String,
//    giftCardsRedeemed: String,
//    imageBackgroundColor: Color
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight(),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
//        shape = RoundedCornerShape(8.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(16.dp)
//        ) {
//            Text(
//                text = "Date Purchased: $datePurchased",
//                color = Color.White,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Medium
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column {
//                    Text(
//                        text = "Order ID: $orderId",
//                        color = Color.White,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Normal
//                    )
//
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    Text(
//                        text = "Qty Purchased: $quantity",
//                        color = Color.White,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Normal
//                    )
//
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    Text(
//                        text = "Gift Cards Redeemed: $giftCardsRedeemed",
//                        color = Color.White,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Normal
//                    )
//                }
//
//                Box(
//                    modifier = Modifier
//                        .size(80.dp)
//                        .background(imageBackgroundColor, RoundedCornerShape(8.dp))
//                        .clickable { /* Handle Click */ },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "View Details",
//                        color = Color.White,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//        }
//    }
//}
//


@Composable
fun ScreenCategoryList(
//    modifier: Modifier,
    navController: NavController,
    stateCategory: StateCategory,
    onEvent: (EventCategory) -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val filteredExpenses = remember(searchQuery, selectedCategory, stateCategory.categoryList) {
        stateCategory.categoryList.filter {
            (searchQuery.isEmpty() || it.categoryName.contains(searchQuery, ignoreCase = true))
                    && (selectedCategory == null || it.categoryName == selectedCategory)
        }
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Category List",
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),
                    fontSize = 17.sp
                )
                IconButton(onClick = { onEvent(EventCategory.SortCategory) }) {
                    Icon(imageVector = Icons.AutoMirrored.Rounded.Sort, contentDescription = null)
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
//                navController.navigate(Screen.ScreenCategoryAdd)
                navController.navigate(Screen.ScreenCategoryAdd.createRouteCategory())
            }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = it
        ) {
//            items(stateCategory.categoryList) {
            items(filteredExpenses) { category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = category.categoryName)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = {
                            navController.navigate(
                                Screen.ScreenCategoryAdd.createRouteCategory(
                                    category
                                )
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = {
                            //onEvent(EventCategory.DeleteCategory(category))
                            categoryToDelete = category
                            showDeleteDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }


                }
            }
        }
        if (showDeleteDialog) {
            ConfirmationDialog(
                title = "Delete Category",
                message = "Are you sure you want to delete this category?",
                confirmButtonText = "Delete",
                cancelButtonText = "Cancel",
                onConfirm = {
                    onEvent?.let {
                        onEvent(EventCategory.DeleteCategory(categoryToDelete!!))
                    }
                    showDeleteDialog = false
                },
                onCancel = { showDeleteDialog = false },
                isDialogVisible = true,
                onDismiss = { showDeleteDialog = false }
            )
        }
    }

}