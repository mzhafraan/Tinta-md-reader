---
description: Android Jetpack Compose best practices, architecture, and performance guidelines
globs: "**/*.kt"
alwaysApply: true
---

# Jetpack Compose Development Guidelines

## 1. Architecture & State Management
- **Unidirectional Data Flow (UDF)**: Events flow up, State flows down.
- **State Hoisting**: Keep Composables stateless whenever possible. Pass events up to the ViewModel or parent caller.
- **Immutability**: Ensure data models passed to composables are immutable (`data class` with `val` properties).
- **Coroutines & Flow**: Use Kotlin Coroutines and `StateFlow` for state observation in UI layers (`collectAsState()`).
- **Clean Separation**: UI layer observes ViewModel; ViewModel delegates to Repository; Repository interacts with DataStore and Storage Access Framework (SAF).

## 2. Compose Performance & Recomposition
- Use `remember` and `derivedStateOf` for calculations and expensive operations inside composables.
- Provide stable keys in `items(items, key = { ... })` for `LazyColumn`, `LazyRow`, and `LazyVerticalGrid`.
- Avoid calculating values or allocating objects directly inside `@Composable` drawing loops.

## 3. Modifiers & Layout
- Always chain modifiers in the correct sequence (size and layout modifiers first, click/event modifiers, then drawing/graphics modifiers like background or border).
- Pass `modifier: Modifier = Modifier` as the first optional parameter in all reusable Composables.
