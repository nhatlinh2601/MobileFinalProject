package com.example.mobilefinalproject.navigation

enum class Screen{
    SPLASH,
    HOME,
    PROFILE,
    AUTHOR_PROFILE,
    SEARCH_COURSE,
    COURSE_RESULT,
    COURSE_DETAILS,
    COURSE_OVERVIEW,
    COURSE_PLAYLIST,
    MY_COURSES,
    LOGIN,
    REGISTER,
    COURSE_BY_CATEGORY,
    LESSON,
    EDIT_PROFILE,
    CART,
    CHAT,
    TEST

}

sealed class NavigationItem(val route:String) {
    data object Splash : NavigationItem(Screen.SPLASH.name)
    data object Home : NavigationItem(Screen.HOME.name)
    data object Profile : NavigationItem(Screen.PROFILE.name)
    data object EditProfile : NavigationItem(Screen.EDIT_PROFILE.name)
    data object AuthorProfile : NavigationItem(Screen.AUTHOR_PROFILE.name)
    data object SearchCourse : NavigationItem(Screen.SEARCH_COURSE.name)
    data object CourseResult : NavigationItem(Screen.COURSE_RESULT.name)
    data object CourseDetails : NavigationItem(Screen.COURSE_DETAILS.name)
    data object CourseOverview : NavigationItem(Screen.COURSE_OVERVIEW.name)
    data object Lesson : NavigationItem(Screen.LESSON.name)
    data object CoursePlaylist : NavigationItem(Screen.COURSE_PLAYLIST.name)
    data object MyCourses : NavigationItem(Screen.MY_COURSES.name)
    data object Login : NavigationItem(Screen.LOGIN.name)
    data object Register : NavigationItem(Screen.REGISTER.name)
    data object CourseByCategory : NavigationItem(Screen.COURSE_BY_CATEGORY.name)
    data object Cart : NavigationItem(Screen.CART.name)
    data object Chat : NavigationItem(Screen.CHAT.name)
    data object Test : NavigationItem(Screen.TEST.name)
}