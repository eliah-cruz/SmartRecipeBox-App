# 🍳 SmartBox - Smart Recipe Box App

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/IDE-Android%20Studio%20Hedgehog-43853D?style=for-the-badge&logo=android-studio&logoColor=white" />
  <img src="https://img.shields.io/badge/Status-Frontend%20Complete-orange?style=for-the-badge" />
</p>

SmartBox is a modern, feature-rich Android recipe manager built with Kotlin. It allows users to organize, browse, search, and add recipes through a smooth single-activity fragment architecture.

---

## 📱 Frontend Implementation Status (✅ COMPLETED)

The frontend UI, fragment navigation, user interactions, and layouts have been fully developed and tested:

- **Fragment-Based Navigation:** Single-activity architecture (`MainActivity`) managing dynamic transitions between `IntroFragment`, `RecipeListFragment`, and `RecipeDetailFragment`.
- **Custom Animations:** Smooth custom slide and fade transitions for fragment switching and UI navigation.
- **Search & Filter System:** Real-time search with smooth debouncing and responsive category chips (All, Quick, Breakfast, Dinner, Dessert).
- **Theme Persistence:** Dynamic Light and Dark mode toggling that saves user preferences locally via `SharedPreferences`.
- **Android Intents:**
  - *Explicit Intent:* Launches `AddRecipeActivity` from the main recipe list.
  - *Implicit Intent:* Triggers the device share sheet to share recipes with external apps (WhatsApp, Gmail, etc.).
- **Robust UI/UX:** Styled using `LinearLayout`, `CardView`, and custom text gradient shaders.

---

## 🗂️ Project Structure Overview
- `app/src/main/java/com/example/smartrecipebox/`
  - 📂 **fragments/** (`IntroFragment`, `RecipeListFragment`, `RecipeDetailFragment`)
  - 📂 **models/** (`Recipe.kt`)
  - 📂 **data/** (`RecipeRepository.kt`)
  - 📄 `MainActivity.kt` (Hosts fragment navigation container)
  - 📄 `AddRecipeActivity.kt` (Explicit intent form for adding new recipes)
  - 📄 `ThemeHelper.kt` (Manages light/dark mode persistence)
  - 📄 `extensions.kt` (Custom UI text gradient utilities)

---

## 🔥 Backend Handoff Guide (Firebase Integration)

> **Note for Backend Developer:** The frontend UI, input forms, and data models are fully wired up. You are ready to connect **Firebase Auth & Firestore**!

### Where to integrate Firebase:
1. **Adding Recipes (`AddRecipeActivity.kt`):**
   - Currently, user-submitted recipes are appended to a local mock repository (`RecipeRepository.addRecipe`).
   - *Task:* Replace or extend the "Save Recipe" click listener to write the `Recipe` object directly to **Firebase Firestore**.

2. **Fetching Recipes (`RecipeListFragment.kt` & `RecipeDetailFragment.kt`):**
   - Currently, recipes are pulled from local mock data.
   - *Task:* Replace local repository calls with real-time Firestore listeners (`addSnapshotListener`) to load and display cloud recipes dynamically.

3. **Authentication (Optional/Recommended):**
   - Implement **Firebase Auth** if user-specific recipe boxes or a login/signup screen is required.

---

## 🚀 How to Run
1. Clone this repository.
 ``` bash
git clone https://github.com/eliah-cruz/SmartRecipeBox-App
```
3. Open the project in Android Studio.
4. Sync project with Gradle files and run on an emulator or physical device. (**Physical device RECOMMENDED**).
