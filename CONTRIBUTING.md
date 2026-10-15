# Contributing to Tinta 🖋️

Thank you for your interest in contributing to **Tinta**! We are building an artisanal, vintage editorial reading experience that respects user privacy and celebrates print typography in the digital era.

Whether you are fixing a typo, resolving an Android rendering bug, improving accessibility, or proposing a new editorial theme, your contribution is greatly appreciated.

---

## 🏛️ Guiding Principles

1. **Zero-Trust Privacy (Non-Negotiable)**: Tinta will **never** request network permissions (`android.permission.INTERNET`). Any pull request adding telemetry, analytics, or external network calls will be rejected.
2. **Editorial Aesthetic First**: Every UI component should feel like real paper, ink, or printing press material. Avoid generic flat UI or jarring high-contrast elements.
3. **Storage Access Framework (SAF) Compliance**: We do not request broad filesystem permissions (`MANAGE_EXTERNAL_STORAGE`). Only work with user-granted URIs.

---

## 🚀 Getting Started

### 1. Prerequisites
- **Android Studio** Ladybug (2024.2.1+) or newer.
- **JDK 17** (Zulu or Temurin recommended).
- Android device or emulator running **API 26 (Android 8.0)** or higher.

### 2. Fork & Clone
```bash
git clone https://github.com/<your-username>/Tinta-md-reader.git
cd Tinta-md-reader
git remote add upstream https://github.com/mzhafraan/Tinta-md-reader.git
```

### 3. Branching Workflow
- Create a dedicated branch for your work:
  ```bash
  git checkout -b feature/vintage-toc-drawer
  # or
  git checkout -b fix/code-block-overflow
  ```
- Keep commits focused and provide clear, descriptive commit messages:
  - `feat: add table of contents bottom sheet`
  - `fix: resolve crash on revoked persistable uri`
  - `docs: update roadmap in README`

---

## 💻 Development & Code Style

- **Kotlin Best Practices**: Follow the official [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).
- **Jetpack Compose**:
  - Prefer immutable state holders and **State Hoisting** (pass events up, state down).
  - Use `Modifier` as the first optional parameter in Composable functions.
  - Colors and typography should always refer to `TintaTheme` tokens (`com.zeeonworks.tinta.ui.theme`).
- **Clean Architecture**:
  - Keep domain logic and parsing independent of UI components.
  - ViewModels communicate with Repositories and expose state via Kotlin `StateFlow`.

---

## 🧪 Testing Before Submitting

Always ensure that unit tests and linting pass locally before opening a pull request:

```bash
# Run unit tests
./gradlew test

# Run Android lint checks
./gradlew lintDebug
```

---

## 📬 Submitting a Pull Request (PR)

1. Push your branch to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```
2. Open a Pull Request against `mzhafraan/Tinta-md-reader` targeting the `master` branch.
3. Fill out the PR template thoroughly, explaining what changed, why, and attaching before/after screenshots if UI changes were made.
4. Ensure all automated GitHub Actions CI checks pass.

Thank you for helping keep the printing press running! 📜
