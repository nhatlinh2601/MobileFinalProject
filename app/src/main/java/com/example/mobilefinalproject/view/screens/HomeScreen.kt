package com.example.mobilefinalproject.view.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobilefinalproject.viewmodel.HomeViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.mobilefinalproject.config.CONSTANT
import com.example.mobilefinalproject.model.Course
import com.example.mobilefinalproject.model.User
import com.example.mobilefinalproject.navigation.NavigationItem
import com.example.mobilefinalproject.view.components.CategoryCard
import com.example.mobilefinalproject.view.components.CourseCard
import com.example.mobilefinalproject.view.components.InfoDialog
import com.example.mobilefinalproject.viewmodel.AuthViewModel
import com.example.mobilefinalproject.viewmodel.CourseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    courseViewModel: CourseViewModel,
    navController: NavController,
    userID: Int
) {

    val currentUser by authViewModel.infoUser.collectAsState()
    val countOfCart by courseViewModel.countOfCart.collectAsState()
    LaunchedEffect(key1 = Unit) {
        authViewModel.getUser(userID)
        courseViewModel.getCountCart(userID)
    }
    Log.e("currentUser-home-screen", currentUser.toString())


    // search
    val context = LocalContext.current
    val searchText by homeViewModel.searchText.collectAsState()
    val isSearching by homeViewModel.isSearching.collectAsState()

    //
    val courses by homeViewModel.courses.collectAsState(emptyList())
    val popularCourses by homeViewModel.popularCourses.collectAsState(emptyList())
    val recommendCourses by homeViewModel.recommendCourses.collectAsState(emptyList())
    val categories by homeViewModel.categories.collectAsState(emptyList())
    val isOpenDialog by courseViewModel.isOpenDialog.collectAsState()



    LaunchedEffect(Unit) {
        homeViewModel.fetchCourses()
        homeViewModel.fetchCategories()
        homeViewModel.fetchPopularCourses()
        homeViewModel.fetchRecommendCourses()
    }

    val randomCategories = categories.shuffled().take(2)


    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    Log.e("courses", courses.toString())
    Log.e("categories", categories.toString())

    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 14.dp, vertical = 0.dp)
    ) {
        item {
            if (currentUser != null) {

                WelcomeBack(
                    name = currentUser!!.name,
                    urlImage = currentUser!!.img,
                    navController,
                    currentUser = currentUser!!,
                    countOfCart
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        item {
            Column {
                SearchBar(
                    query = searchText,//text showed on SearchBar
                    onQueryChange = homeViewModel::onSearchTextChange, //update the value of searchText
                    onSearch = homeViewModel::onSearchTextChange, //the callback to be invoked when the input service triggers the ImeAction.Search action
                    active = isSearching, //whether the user is searching or not
                    onActiveChange = { homeViewModel::onToggleSearch }, //the callback to be invoked when this search bar's active state is changed
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            if (searchText.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Please do not leave the search box blank",
                                    Toast.LENGTH_LONG
                                ).show()

                            } else {
                                homeViewModel.fetchCoursesBySearchKey(searchText)
                                navController.navigate("${NavigationItem.CourseResult.route}/${searchText}")
                            }

                        }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    shape = SearchBarDefaults.dockedShape,
                ) {}

                Spacer(modifier = Modifier.height(16.dp))

                FlowRow(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 3
                ) {}
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories) { category ->
                    CategoryCard(category = category, onItemClick = {
                        homeViewModel.fetchCoursesByCate(category.id)
                        navController.navigate("${NavigationItem.CourseByCategory.route}/${category.id}/${category.name}")
                    }, width = 140)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            CoursesByList(
                courses = popularCourses,
                title = "Popular",
                width = 280,
                navController,
                courseViewModel,
                userID
            )
        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            CoursesByList(
                courses = recommendCourses,
                title = "Recommended",
                width = 280,
                navController,
                courseViewModel,
                userID
            )
        }

//        for (category in randomCategories){
//            val listCourses = courseViewModel.fe
//            val list : List<Course> = courses
//            item {
//                CoursesByList(
//                    courses = list,
//                    title = category.name,
//                    width = 260,
//                    navController = navController,
//                    courseViewModel = courseViewModel,
//                    userID = userID
//                )
//            }
//        }


    }

    if (isOpenDialog) {
        InfoDialog(
            title = "Successfully",
            desc = "Add to cart successfully",
            onDismiss = {
                courseViewModel.deleteDialog()
            },
            urlIcon = CONSTANT.APP.SUCCESS_ICON_URL
        )

    }


}


@Composable
fun CoursesByList(
    courses: List<Course>,
    title: String,
    width: Int,
    navController: NavController,
    courseViewModel: CourseViewModel,
    userID: Int
) {

    val isMyCart by courseViewModel.isMyCart.collectAsState()

    var openExistedCart by remember {
        mutableStateOf(false)
    }
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(20.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(courses) { course ->
                CourseCard(course = course, width = width, onItemClick = {
                    navController.navigate("${NavigationItem.CourseOverview.route}/${course.id}")
                }, onAddToCartClick = {
                    courseViewModel.addToCart(userID, course.id)
                })
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun WelcomeBack(
    name: String, urlImage: String? = null, navController: NavController, currentUser: User,
    countOfCart: Int
) {

    val painter = rememberAsyncImagePainter(
        model = urlImage
    )
    val painterError = rememberAsyncImagePainter(
        model = "https://cdn.sforum.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg"
    )
    Row(
        modifier = Modifier
            .padding(top = 20.dp, bottom = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (painter.state is AsyncImagePainter.State.Error) {
                Image(
                    painter = painterError,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)

                )
            } else {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)

                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Hi, $name",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxHeight()
            )

        }


        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .size(60.dp)
                .padding(8.dp)
                .clickable {
                    navController.navigate("${NavigationItem.Cart.route}/${currentUser.id}")
                }
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Shopping Cart",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center).size(36.dp)
            )

            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer
                    )
                    .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = countOfCart.toString(),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,

                )
            }

        }
//
//        Box(modifier = Modifier){
//            Icon(
//                imageVector = Icons.Default.ShoppingCart,
//                contentDescription = "Shopping Cart",
//                modifier = Modifier
//                    .size(48.dp)
//                    .padding(8.dp)
//                    .clickable {
//                        navController.navigate("${NavigationItem.Cart.route}/${currentUser.id}")
//                    },
//                tint = MaterialTheme.colorScheme.primary
//            )
//        }

    }
}


@Composable
fun CategoryButton(
    categoryName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp)
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .padding(horizontal = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant, shape = shape
            )
            .clickable(onClick = onClick), contentAlignment = Alignment.Center
    ) {
        Text(
            text = categoryName,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}







