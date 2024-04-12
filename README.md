# HTTP GET POST REQUEST

In this project, http GET and POST request are implemented by using HttpURLConnection from JAVA SDK. So no 3rd party libraries such as Retrofit or HttpClient are used.

## Features


### Getting User Profile
This method send GET method to backend server to get user profile.\
\
params\
userID: String = It is a user id of requested user profile.\
\
Returns: It returns requested user profile ALL attributes as a string. For test case "ae41278c-8128-40c3-b6d8-7d23c6b21218" can be used.
```kotlin 
fun getUserProfile(
        userID: String,
 ): String
```

### Login
This method send POST method to backend server to perform login operations.\
\
params\
userLoginDto: UserLoginDto = It is a defined user login dto from backend side.\
\
Returns: If login is successful it returns user id corresponding login, if not, it returns empty string.
```kotlin 
 fun login(
        userLoginDto: UserLoginDto
): String
```

### Model class
#### User Login DTO
It is a dto for using login operations. It is required for POST implementation in login for backend side so it is very crucial.
```kotlin 
 data class UserLoginDto(
    val email: String,
    val password: String
)
```
