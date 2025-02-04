package com.mobitechs.notesapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mobitechs.notesapp.data_layer.Category
import com.mobitechs.notesapp.data_layer.ExpenseDetails
import com.mobitechs.notesapp.data_layer.Income
import com.mobitechs.notesapp.ui.theme.NotesAppTheme
import com.mobitechs.notesapp.ui_layer.Note.ScreenNoteAdd
import com.mobitechs.notesapp.ui_layer.Note.ScreenNoteList
import com.mobitechs.notesapp.ui_layer.Note.ViewModelNote
import com.mobitechs.notesapp.ui_layer.category.ScreenCategoryAdd
import com.mobitechs.notesapp.ui_layer.category.ScreenCategoryList
import com.mobitechs.notesapp.ui_layer.category.ViewModelCategory
import com.mobitechs.notesapp.ui_layer.expense.ScreenExpenseAdd
import com.mobitechs.notesapp.ui_layer.expense.ScreenExpenseList
import com.mobitechs.notesapp.ui_layer.expense.ViewModelExpense
import com.mobitechs.notesapp.ui_layer.income.ScreenIncomeAdd
import com.mobitechs.notesapp.ui_layer.income.ScreenIncomeList
import com.mobitechs.notesapp.ui_layer.income.ViewModelIncome
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesAppTheme {


                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val viewModel = hiltViewModel<ViewModelNote>()
                    val state by viewModel.state.collectAsState()

                    val viewModelCategory = hiltViewModel<ViewModelCategory>()
                    val categoryState by viewModelCategory.stateCategory.collectAsState()

                    val viewModelIncome = hiltViewModel<ViewModelIncome>()
                    val stateIncome by viewModelIncome.stateIncome.collectAsState()

                    val viewModelExpense = hiltViewModel<ViewModelExpense>()
                    val stateExpense by viewModelExpense.stateExpenseDetails.collectAsState()


                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        val navController = rememberNavController()
                        NavHost(
                             navController = navController,
                            startDestination = Screen.ScreenExpenseList.route
                        ) {
//                            composable(Screen.ScreenExpenseList.route) { // Define the route for ExpenseList
//                                ScreenExpenseList(
//                                    navController = navController,
//                                    stateExpense = stateExpense,
//                                    onEventExpense = viewModelExpense::onEvent
//                                )
//                            }

                            composable(Screen.ScreenNoteList.route) {
                                ScreenNoteList(
                                    navController = navController,
                                    state = state,
                                    onEvent = viewModel::onEvent,
                                    //  modifier = TODO()
                                )
                            }
                            composable(Screen.ScreenNoteAdd.route) {
                                ScreenNoteAdd(
                                    navController = navController,
                                    state = state,
                                    onEvent = viewModel::onEvent,
                                    //  modifier = TODO()
                                )
                            }
                            composable(Screen.ScreenCategoryList.route) {
                                ScreenCategoryList(
                                    navController = navController,
                                    stateCategory = categoryState,
                                    onEvent = viewModelCategory::onEvent,
                                    //  modifier = TODO()
                                )
                            }
//                            composable(Screen.ScreenCategoryAdd.route) {
//                                ScreenCategoryAdd(
//                                    navController = navController,
//                                    stateCategory = categoryState,
//                                    onEvent = viewModelCategory::onEvent,
//                                    //  modifier = TODO()
//                                )
//                            }

                            composable(
                                route = Screen.ScreenCategoryAdd.route,
                                arguments = listOf(
                                    navArgument("categoryDetails") {
                                        type = NavType.StringType
                                        nullable = true
                                    }
                                )
                            ) { backStackEntry ->
                                val categoryDetailsJson = backStackEntry.arguments?.getString("categoryDetails")
                                val categoryDetails = categoryDetailsJson?.let {
                                    Json.decodeFromString<Category>(it)
                                }

                                ScreenCategoryAdd(
                                    navController = navController,
                                    stateCategory = categoryState,
                                    onEvent = viewModelCategory::onEvent,
                                    categoryDetails = categoryDetails
                                )
                            }

                            composable(Screen.ScreenIncomeList.route) {
                                ScreenIncomeList(
                                    navController = navController,
                                    stateIncome = stateIncome,
                                    onEventIncome = viewModelIncome::onEvent,
//                                    modifier = TODO()
                                )
                            }
                            composable(Screen.ScreenExpenseList.route) {
                                ScreenExpenseList(
                                    navController = navController,
                                    stateExpense=stateExpense,
                                    onEventExpense = viewModelExpense::onEvent,
//                                    modifier = TODO()
                                )
                            }

//                            composable(Screen.ScreenIncomeAdd.route) {
//                                ScreenIncomeAdd(
//                                    navController = navController,
//                                    stateIncome = stateIncome,
//                                    onEventIncome = viewModelIncome::onEvent,
//                                    //  modifier = TODO()
//                                )
//                            }

                            composable(
                                route = Screen.ScreenIncomeAdd.route,
                                arguments = listOf(
                                    navArgument("incomeDetails") {
                                        type = NavType.StringType
                                        nullable = true
                                    }
                                )
                            ) { backStackEntry ->
                                val incomeDetailsJson = backStackEntry.arguments?.getString("incomeDetails")
                                val incomeDetails = incomeDetailsJson?.let {
                                    Json.decodeFromString<Income>(it)
                                }

                                ScreenIncomeAdd(
                                    navController = navController,
                                    stateIncome = stateIncome,
                                    onEventIncome = viewModelIncome::onEvent,
                                    incomeDetails = incomeDetails
                                )
                            }

//                            composable(
//                                route = Screen.ScreenExpenseAdd.route,
//                                arguments = listOf(navArgument("expenseId") {
//                                    type = NavType.IntType
//                                    defaultValue = -1 // Default for Add
//                                })
//                            ) { backStackEntry ->
//                                val expenseId = backStackEntry.arguments?.getInt("expenseId") ?: -1
//                                ScreenExpenseAdd(
//                                    navController = navController,
//                                    stateExpense = stateExpense,
//                                    onEventExpense = viewModelExpense::onEvent,
//                                    expenseId = expenseId
//                                )
//                            }


                            composable(
                                route = Screen.ScreenExpenseAdd.route,
                                arguments = listOf(
                                    navArgument("expenseDetails") {
                                        type = NavType.StringType
                                        nullable = true
                                    }
                                )
                            ) { backStackEntry ->
                                val expenseDetailsJson = backStackEntry.arguments?.getString("expenseDetails")
                                val expenseDetails = expenseDetailsJson?.let {
                                    Json.decodeFromString<ExpenseDetails>(it)
                                }

                                ScreenExpenseAdd(
                                    navController = navController,
                                    stateExpense = stateExpense,
                                    onEventExpense = viewModelExpense::onEvent,
                                    expenseDetails = expenseDetails // Can be null
                                )
                            }



                        }
                    }
                }
            }
        }
    }
}


sealed class Screen(val route: String) {
    object ScreenNoteList : Screen("note_list")
    object ScreenNoteAdd : Screen("note_add")
    object ScreenCategoryList : Screen("category_list")
    //object ScreenCategoryAdd : Screen("category_add")
    object ScreenIncomeList : Screen("income_list")
//    object ScreenIncomeAdd : Screen("income_add")
    object ScreenExpenseList : Screen("expense_list")

    object ScreenCategoryAdd : Screen("category_add?categoryDetails={categoryDetails}") {
        fun createRouteCategory(categoryDetails: Category? = null): String {
            return if (categoryDetails != null) {
                "category_add?categoryDetails=${Uri.encode(Json.encodeToString(categoryDetails))}"
            } else {
                "category_add"
            }
        }
    }

    object ScreenIncomeAdd : Screen("income_add?incomeDetails={incomeDetails}") {
        fun createRouteIncome(incomeDetails: Income? = null): String {
            return if (incomeDetails != null) {
                "income_add?incomeDetails=${Uri.encode(Json.encodeToString(incomeDetails))}"
            } else {
                "income_add"
            }
        }
    }

    object ScreenExpenseAdd : Screen("expense_add?expenseDetails={expenseDetails}") {
        fun createRoute(expenseDetails: ExpenseDetails? = null): String {
            return if (expenseDetails != null) {
                "expense_add?expenseDetails=${Uri.encode(Json.encodeToString(expenseDetails))}"
            } else {
                "expense_add"
            }
        }
    }
}



//
//sealed class Screen {
//    @Serializable
//    object ScreenNoteList
//
//    @Serializable
//    object ScreenNoteAdd
//
//
//    @Serializable
//    object ScreenCategoryList
//
//    @Serializable
//    object ScreenCategoryAdd
//
//
//    @Serializable
//    object ScreenIncomeList
//
//    @Serializable
//    object ScreenIncomeAdd
//
//    @Serializable
//    object ScreenExpenseAdd
//
//    @Serializable
//    object ScreenExpenseList
//
//
////    @Serializable
////    object
////
//}
