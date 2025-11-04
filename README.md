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
There are two ways to use GroovinPermX depending on your context:

### 1. Usage in Activity
You can directly call `requestPermission()` extension function from any `ComponentActivity` within a coroutine scope.
> Note : `requestPermission()` extension is only available for `ComponentActivity`.

```kotlin
class YourActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Call requestPermission() in any coroutine scope
    lifecycleScope.launch {
      val permList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
      )
      val permResult = requestPermission(permList)

      if (permResult.isAllGranted()) {
        // All of permissions are granted.
      } else if (permResult.shouldShowRequestPermissionRationale()) {
        // Some(or all) permissions are denied, and You should show the request permission rationale to user.
      } else {
        // Some(or all) permissions are denied.
      }
    }
  }
}
```

### 2. Usage in Composable
In Compose, use `rememberPermX()` to get a PermX instance and call `requestPermission()`.

```kotlin
@Composable
fun YourComposable() {
  val permX = rememberPermX()
  val scope = rememberCoroutineScope()

  Button(onClick = {
    scope.launch {
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
  }) {
    Text("Request Permissions")
  }
}
```

### Understanding PermissionResult
`requestPermission()` method will return `PermissionResult`. This class has 3 member variables and 3 methods.

```kotlin
val permResult = requestPermission(permList)

// Granted permission list.
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
