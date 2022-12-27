package io.groovin.permx

data class PermissionResult(
    val grantList: List<String>,
    val deniedList: List<String>,
    val shouldShowRequestPermissionRationaleList: List<String>
) {
    fun isAllGranted(): Boolean = deniedList.isEmpty() && shouldShowRequestPermissionRationaleList.isEmpty()
    fun shouldShowRequestPermissionRationale(): Boolean = shouldShowRequestPermissionRationaleList.isNotEmpty()
    fun getAllDeniedPermissionList(): List<String> = deniedList + shouldShowRequestPermissionRationaleList
}