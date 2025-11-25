package com.msomu.androidkt.presentation.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private object SkeletonDefaults {
    const val ALPHA_HIGH = 0.6f
    const val ALPHA_LOW = 0.3f
    const val SHIMMER_DISTANCE = 1000f
    const val SHIMMER_DURATION_MS = 1200

    const val TITLE_WIDTH_FRACTION = 0.7f
    const val SUBTITLE_WIDTH_FRACTION = 0.4f

    const val DETAIL_TITLE_WIDTH_FRACTION = 0.8f
    const val DETAIL_SUBTITLE_WIDTH_FRACTION = 0.5f

    const val DESCRIPTION_LINES = 3
    const val LAST_LINE_INDEX = 2
    const val LAST_LINE_WIDTH_FRACTION = 0.6f
    const val FULL_WIDTH_FRACTION = 1f
}

/**
 * Skeleton loading component for todo list items
 */
@Composable
fun SkeletonTodoItem(modifier: Modifier = Modifier) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = SkeletonDefaults.ALPHA_HIGH),
        Color.LightGray.copy(alpha = SkeletonDefaults.ALPHA_LOW),
        Color.LightGray.copy(alpha = SkeletonDefaults.ALPHA_HIGH)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = SkeletonDefaults.SHIMMER_DISTANCE,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = SkeletonDefaults.SHIMMER_DURATION_MS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(
            translateAnim - SkeletonDefaults.SHIMMER_DISTANCE,
            translateAnim - SkeletonDefaults.SHIMMER_DISTANCE
        ),
        end = Offset(translateAnim, translateAnim)
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox skeleton
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Title skeleton
                Box(
                    modifier = Modifier
                        .fillMaxWidth(SkeletonDefaults.TITLE_WIDTH_FRACTION)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )

                // Subtitle skeleton
                Box(
                    modifier = Modifier
                        .fillMaxWidth(SkeletonDefaults.SUBTITLE_WIDTH_FRACTION)
                        .height(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Avatar skeleton
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(brush)
            )
        }
    }
}

/**
 * Skeleton loading component for detail page cards
 */
@Composable
fun SkeletonDetailCard(modifier: Modifier = Modifier) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = SkeletonDefaults.ALPHA_HIGH),
        Color.LightGray.copy(alpha = SkeletonDefaults.ALPHA_LOW),
        Color.LightGray.copy(alpha = SkeletonDefaults.ALPHA_HIGH)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = SkeletonDefaults.SHIMMER_DISTANCE,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = SkeletonDefaults.SHIMMER_DURATION_MS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(
            translateAnim - SkeletonDefaults.SHIMMER_DISTANCE,
            translateAnim - SkeletonDefaults.SHIMMER_DISTANCE
        ),
        end = Offset(translateAnim, translateAnim)
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar skeleton
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(brush)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Title skeleton
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(SkeletonDefaults.DETAIL_TITLE_WIDTH_FRACTION)
                            .height(20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(brush)
                    )

                    // Subtitle skeleton
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(SkeletonDefaults.DETAIL_SUBTITLE_WIDTH_FRACTION)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(brush)
                    )
                }
            }

            // Description lines
            repeat(SkeletonDefaults.DESCRIPTION_LINES) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(
                            if (it == SkeletonDefaults.LAST_LINE_INDEX)
                                SkeletonDefaults.LAST_LINE_WIDTH_FRACTION
                            else SkeletonDefaults.FULL_WIDTH_FRACTION
                        )
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
            }
        }
    }
}
