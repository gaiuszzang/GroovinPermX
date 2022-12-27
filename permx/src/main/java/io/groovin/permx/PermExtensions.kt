package io.groovin.permx

import android.app.Activity
import androidx.compose.runtime.compositionLocalOf
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

val LocalPermX = compositionLocalOf<PermX> { throw NotLocalizedCompositionException() }

class PermXFactory(activity: Activity): ReadOnlyProperty<Any, PermX> {
    private val permX = PermX(activity)
    override fun getValue(thisRef: Any, property: KProperty<*>): PermX {
        return permX
    }
}

fun Activity.permX(): PermXFactory {
    return PermXFactory(this)
}
