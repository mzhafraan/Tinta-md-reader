---
description: Enforce Apple HIG aesthetic in Kotlin Jetpack Compose files
globs: "**/*.kt"
alwaysApply: true
---

# Kotlin Apple HIG Override
Whenever creating or modifying Jetpack Compose `@Composable` UI functions:
1. Always invoke and adhere to the installed `apple-design-skill` and `taste-skill`.
2. Reject default Material 3 ripples, harsh drop shadows, and standard `RoundedCornerShape`.
3. Use `SquircleShape` for continuous concentric corners (`InnerRadius = OuterRadius - Padding`).
4. Use `Haze` (`HazeMaterials.ultraThin()`) for translucent iOS-style navigation bars, tab bars, and sheets.
5. In Dark Mode, use Apple's elevated surface luminance (`#000000` base, `#1C1C1E` primary card, `#2C2C2E` modal) with a `0.5.dp` top inner border (`Color.White.copy(alpha = 0.12f)`).
6. Use spring physics (`spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)`) and scale-down press states (`0.97f`) instead of linear/tween animations.
