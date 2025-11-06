package com.msomu.androidkt.presentation.ui.animation

import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.unit.dp

/**
 * Centralized animation specifications for consistent animations throughout the app
 */
object AnimationSpecs {

    // Spring animations for natural, physics-based motion
    val SpringDefault = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )

    val SpringLowStiffness = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )

    val SpringHighStiffness = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessHigh
    )

    // Tween animations for controlled, time-based motion
    val TweenFast = tween<Float>(
        durationMillis = 200,
        easing = FastOutSlowInEasing
    )

    val TweenMedium = tween<Float>(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )

    val TweenSlow = tween<Float>(
        durationMillis = 500,
        easing = FastOutSlowInEasing
    )

    // Enter/Exit transitions for composables
    val EnterTransition = fadeIn(
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    ) + slideInVertically(
        initialOffsetY = { it / 4 },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val ExitTransition = fadeOut(
        animationSpec = tween(200, easing = FastOutLinearInEasing)
    ) + slideOutVertically(
        targetOffsetY = { -it / 4 },
        animationSpec = tween(200, easing = FastOutLinearInEasing)
    )

    // Staggered animation for list items
    fun getStaggeredDelay(index: Int, baseDelay: Int = 50): Int {
        return index * baseDelay
    }

    // Scale animation for press feedback
    object Scale {
        const val PRESSED = 0.98f
        const val DEFAULT = 1f
        const val BOUNCE = 1.05f
    }

    // Duration constants
    object Duration {
        const val FAST = 200
        const val MEDIUM = 300
        const val SLOW = 500
        const val EXTRA_SLOW = 800
    }

    // Easing curves
    object Easing {
        val FastOutSlowIn = FastOutSlowInEasing
        val LinearOutSlowIn = LinearOutSlowInEasing
        val FastOutLinearIn = FastOutLinearInEasing
        val Linear = LinearEasing
    }
}

/**
 * Animation configuration for list items
 */
object ListItemAnimation {
    val enterAnimation = fadeIn(
        animationSpec = tween(
            durationMillis = AnimationSpecs.Duration.MEDIUM,
            easing = AnimationSpecs.Easing.FastOutSlowIn
        )
    ) + slideInVertically(
        initialOffsetY = { it / 8 },
        animationSpec = tween(
            durationMillis = AnimationSpecs.Duration.MEDIUM,
            easing = AnimationSpecs.Easing.FastOutSlowIn
        )
    )

    val exitAnimation = fadeOut(
        animationSpec = tween(
            durationMillis = AnimationSpecs.Duration.FAST,
            easing = AnimationSpecs.Easing.FastOutLinearIn
        )
    )
}

/**
 * Animation configuration for card components
 */
object CardAnimation {
    val enterAnimation = fadeIn(
        animationSpec = tween(
            durationMillis = AnimationSpecs.Duration.SLOW,
            easing = AnimationSpecs.Easing.FastOutSlowIn
        )
    ) + slideInVertically(
        initialOffsetY = { it / 3 },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )

    fun getStaggeredDelay(cardIndex: Int): Int {
        return cardIndex * 100
    }
}
