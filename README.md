# ငွေစာရင်းတွက်ချက်မှု (Burmese Accounting & Ledger Calculator)

A native Android application built with Kotlin, Jetpack Compose, and Material 3 for real-time ledger accounting, commission calculations, and payout reconciliations in Myanmar Unicode.

---

## Features

- **Burmese Unicode & Noto Sans Myanmar**: Bundled local `Noto Sans Myanmar` font with generous ~1.9 line height to ensure all diacritics and stacked consonants render without clipping.
- **Biometric Security (BiometricPrompt)**: Secure fingerprint or face unlock before displaying sensitive ledger amounts and calculations, with instant re-lock capability from the top bar.
- **Local Room Database & History (ယခင်မှတ်တမ်းများ)**:
  - Save current calculation slips to a local SQLite/Room database with the "သိမ်းမည်" button.
  - History screen listing previous entries with detailed financial breakdowns.
  - Actions to reload any previous entry back into the calculator, copy formatted slips to clipboard, or delete individual/all entries.
  - History count badge on top app bar.
- **Header & Branding**: Title on the left, luxury gold "ShweKhinMyay" logo with a gold diamond and gradient serif text on the right (with an easy plug-in slot to swap in custom brand drawables).
- **Persistent State**: Full state retention across screen rotation and backgrounding via `ViewModel` and `SavedStateHandle`.
- **Card 1: ခေါင်းစဉ်နှင့် အကြိမ်**:
  - Material 3 DatePicker formatted as `dd.MM.yy`.
  - Toggleable AM / PM chips (unselected by default).
  - Dropdown "အကြိမ် ရွေးရန်" with options from ၁ ကြိမ် to ၂၄ ကြိမ် (Burmese digits), followed by "2D".
  - Text input for "အမည်" (Name).
- **Card 2: ရောင်းငွေ နှင့် ကော်၊ အပ်ငွေ**:
  - Numeric input "ရောင်းငွေ (ကျပ်)" with live thousands separators.
  - Commission rate selector: 15%, 17%, 25% (default), 30%.
  - Live calculations for ကော် (commission = sales × rate) and အပ်ငွေ (net = sales − commission), each with a dedicated Copy button.
- **Card 3: ပေါင်းထည့်ငွေများ**:
  - Number fields for "အကြွေးကျန်" and "အလုံး".
  - Live total "စုစုပေါင်း အပ်ငွေ" (all = net + debt + lump) with a Copy button.
- **Card 4: အလျော်**:
  - ဒဲ့ amount × multiplier dropdown (550, 600, 80, 75).
  - သွဒ် amount × 10.
  - အပါ amount × multiplier dropdown (3, 8, 15).
  - Live sub-results, read-only total အလျော် (pay = dae + thwad + apa) with a Copy button, and large final total difference.
- **Sticky Bottom Bar**:
  - Sticky at bottom with final difference `diff = |all − pay|` and sign (`"-"` if all > pay, `"+"` if all < pay).
  - "အားလုံးကူးရန်" (Copy All) button that formats a complete Burmese receipt slip directly to the clipboard.
- **Light & Dark Themes**: Follows system theme with Teal accent (`#0F6B5C` light, `#3FC1A8` dark), gold accents, and accessible touch targets (≥ 48dp).

---

## Automatic APK Building & GitHub Releases

The project is pre-configured with **GitHub Actions** (`.github/workflows/build-apk.yml`) to automatically test, build, and publish the debug APK for direct download.

### 1. Direct Download from GitHub Releases
Whenever you create or push a Git tag (e.g., `v1.0.0`), GitHub Actions automatically:
1. Runs the test suite and builds the debug APK.
2. Creates a new GitHub Release with the tag name.
3. Attaches **`NgweSaryin-Twetchet-debug.apk`** for direct public download.

To trigger a release manually via Git:
```bash
git tag v1.0.0
git push origin v1.0.0
```

### 2. Direct Download from GitHub Actions Artifacts
On every push or pull request to `main` or `master`:
1. Navigate to the **Actions** tab in your GitHub repository.
2. Click on the latest workflow run: **Build & Release Debug APK**.
3. Scroll to the **Artifacts** section at the bottom of the summary page.
4. Click **`NgweSaryin-Twetchet-debug-apk`** to download the ZIP containing the compiled APK.

### 3. Manual Workflow Trigger
1. Go to **Actions** → **Build & Release Debug APK**.
2. Click **Run workflow**.
3. Check **Publish as a GitHub Release** and specify a tag version (e.g. `v1.0.0`).
4. Click **Run workflow**.

---

## Local Development & Build Steps

### 1. Requirements
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17 or JDK 21
- Android SDK (minSdk: 24, targetSdk: 34 / compileSdk: 36)

### 2. Building the Debug APK Locally
Open a terminal in the root directory and run:

```bash
./gradlew assembleDebug
```

The compiled APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### 3. Running Unit Tests
```bash
./gradlew testDebugUnitTest
```

### 4. Installing onto a Device / Emulator
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## Customizing the Logo

To replace the header logo with your own brand asset:
1. Copy your logo image (PNG, WebP, or XML vector) to `app/src/main/res/drawable/` (e.g. `ic_shwe_khin_myay_logo.xml` or `my_logo.png`).
2. In `app/src/main/java/com/example/ui/components/ShweKhinMyayLogo.kt`, set `useCustomDrawable = true` or reference your drawable id.
