## GroovinPermX
[![Release](https://jitpack.io/v/io.groovin/GroovinPermX.svg)](https://jitpack.io/#io.groovin/GroovinPermX)  
Android Permissions with Kotlin Coroutines.
Get Permission Request Result asynchronously with one function call.

## Sample App
Sample app code : [Sample App Code](https://github.com/gaiuszzang/GroovinPermX/tree/main/sampleapp)

There are 2 Activities that one is AppCompatActivity, and other is ComponentActivity for Compose.
You can see how to use this library on each cases. 


## Including in your project
### Gradle
Add below codes to `settings.gradle`.
```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

And add a dependency code to your **module**'s `build.gradle` file.
```gradle
dependencies {
    implementation 'io.groovin:GroovinPermX:x.x.x'
}
```


## Usage
### Declare PermX
First of all, Declare the `PermX` as member fields in Activity.
`PermX` needs a Activity parameter, but you can also use `by permX()` delegation for easily.
> Note : `PermX` only supports Activity that based on AppCompatActivity or ComponentActivity.
```kotlin
class YourActivity : AppCompatActivity() {

  private val permX by permX()  
  // or private val permX = PermX(this)

}
```

In Compose, You can use the `LocalPermX` with CompositionLocalProvider.
```kotlin
class YourActivity : ComponentActivity() {

  private val permX by permX()  
  // or private val permX = PermX(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      CompositionLocalProvider(LocalPermX provides permX) {
        //Composable
      }
    }
  }
}
```

### Check Permissions asynchronously
You can check the permissions as asynchronously with using `requestPermission()` method.
This method is suspend function, so please call this in coroutine.

```kotlin
private suspend fun checkPermission() {
  val permList = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
  )
  val permResult = permX.requestPermission(permList)
  if (permResult.isAllGranted()) {
    // All of permissions are granted. 
  } else if (permResult.shouldShowRequestPermissionRationale()) {
    // Some(or all) permissions are denied, and You should show the request permission rationale to user.
  } else {
    // Some(or all) permissions are denied.
  }
}
```

`requestPermission()` method will return `PermissionResult`. This Class have 3 member variables and 3 methods.
```kotlin
 
val permResult = permX.requestPermission(permList)

//Granted permission list.
permResult.grantList

// Denied permission list that excludes should show request permission rationale.
permResult.deniedList

// Denied permission list that should show request permission rationale.
permResult.shouldShowRequestPermissionRationaleList

// You can check that all permissions are granted or not simply with following method.
permResult.isAllGranted()

// You can check that you should show the Request permissions rationale or not.
permResult.shouldShowRequestPermissionRationale()

// This method returns deniedList + shouldShowRequestPermissionRationaleList.
permResult.getAllDeniedPermissionList()
```


## License
```xml
Copyright 2022 gaiuszzang (Mincheol Shin)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
