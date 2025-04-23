package com.dis.glowbordercard.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun GlowingBorderCard(
    modifier: Modifier = Modifier,
    borderColor: Color = Color(0xFF00FF00),
    backgroundColor: Color = Color(0xFF121212),
    borderWidth: Float = 2f,
    glowRadius: Float = 10f,
    cornerRadius: Float = 16f,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "border-animation")

    // 애니메이션 진행률
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = -0.5f, // tween 애니메이션 보다 먼저 끝나면 안되므로 initialValue와 targetValue를 잘 조절해야 함
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    Box(modifier = modifier) {
        // 빛나는 테두리를 그리는 캔버스
        Canvas(
            modifier = Modifier
                .matchParentSize()
        ) {
            val width = size.width
            val height = size.height

            // 내부 카드 배경을 그리기
            drawRoundRect(
                color = backgroundColor,
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                size = size
            )

            // 테두리를 생성
            val shader = Brush.linearGradient(
                colors = listOf(
                    borderColor.copy(alpha = 0.2f), // 알파를 다르게 줘서 처리하기
                    borderColor.copy(alpha = 0.8f),
                    borderColor,
                    borderColor.copy(alpha = 0.8f),
                    borderColor.copy(alpha = 0.2f)
                ),
                start = Offset(width * (animationProgress - 0.3f), 0f),
                end = Offset(width * (animationProgress + 0.3f), height)
            )

            // 테두리를 그리기
            drawRoundRect(
                brush = shader,
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                style = Stroke(width = borderWidth),
                size = size
            )

            // 글로우 효과 추가
            drawRoundRect(
                brush = shader,
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                style = Stroke(
                    width = glowRadius,
                    pathEffect = PathEffect.cornerPathEffect(cornerRadius)
                ),
                alpha = 0.5f,
                size = size
            )
        }

        // 실제 콘텐츠
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(borderWidth.dp + glowRadius.dp / 2)
                .clip(RoundedCornerShape(cornerRadius.dp))
        ) {
            content()
        }
    }
}

@Composable
fun DemoGlowingBorders() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GlowingBorderCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            borderColor = Color(0xFF00FF00),
            glowRadius = 8f
        ) {
            Box(modifier = Modifier.fillMaxSize().background(Color(0xFF121212)))
        }

        GlowingBorderCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            borderColor = Color(0xFF00AAFF),
            glowRadius = 12f
        ) {
            Box(modifier = Modifier.fillMaxSize().background(Color(0xFF121212)))
        }

        GlowingBorderCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            borderColor = Color(0xFFA020F0),
            glowRadius = 10f
        ) {
            Box(modifier = Modifier.fillMaxSize().background(Color(0xFF121212)))
        }
    }
}