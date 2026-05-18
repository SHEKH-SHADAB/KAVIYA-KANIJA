# Project Plan

Build the Kavya Kanija app with all features and screens mentioned in the project brief. Ensure it follows all UI/UX guidelines and includes a robust data layer for poetry storage.

## Project Brief

# Kavya Kanija - Project Brief

**Kavya Kanija** (Storehouse of Poetry) is a vibrant, modern Android application dedicated to the rich heritage of Kannada literature. The app provides a curated space for users to discover, read, and celebrate Kannada poetry through an energetic Material Design 3 interface.

### Features
*   **Poetry Discovery:** Browse and search through an extensive collection of Kannada poems categorized by poet, era (modern/classical), and genre.
*   **Immersive Reader Experience:** A beautifully crafted reading interface optimized for Kannada typography, featuring adjustable text sizes and themes for comfortable reading.
*   **Share & Celebrate:** Easily share favorite stanzas or entire poems as styled text or image snippets with friends and on social media.
*   **Personal Collection:** Save and organize favorite poems into a personal "Kanija" (storehouse) for quick access and offline reading.

### High-Level Tech Stack
*   **Language:** Kotlin
*   **UI Framework:** Jetpack Compose with **Material Design 3** (M3)
*   **Navigation:** **Jetpack Navigation 3** (state-driven architecture)
*   **Adaptive Strategy:** **Compose Material Adaptive** library for seamless transitions across foldable, tablet, and mobile screens.
*   **Concurrency:** Kotlin Coroutines and Flow for reactive data handling.
*   **Persistence:** Room Database for storing favorites and offline poem metadata.
*   **Image Loading:** Coil for rendering poet portraits and literary cover art.

### Additional Requirements:
- Strictly follow Material Design 3 (M3).
- Vibrant, energetic color scheme.
- Adaptive app icon.
- Full Edge-to-Edge Display.

## Implementation Steps

### Task_1_DataLayer: Set up the data layer including Room database, entities (Poem, Poet), and Repository. Implement initial data population.
- **Status:** COMPLETED
- **Updates:** Set up Poet and Poem entities. Implemented Room database with PoetryDao. Created PoetryRepository. Added initial mock data for Kannada poetry. Updated SDK versions and enabled BuildConfig.
- **Acceptance Criteria:**
  - Room database and DAOs are defined
  - Poem and Poet models are implemented
  - Repository provides Flow of data
  - Initial mock data or asset-based data is loaded
  - Build passes

### Task_2_NavigationAndDiscovery: Implement the main navigation using Navigation 3 and the Discovery Screen (List/Search) with adaptive layouts.
- **Status:** COMPLETED
- **Updates:** Implemented Navigation 3 structure with Screen keys. Created DiscoveryScreen with ListDetailSceneStrategy for adaptive layout. Implemented DiscoveryViewModel with search functionality. Updated theme to be vibrant and energetic. Enabled edge-to-edge display. Verified build stability.
- **Acceptance Criteria:**
  - Navigation 3 structure is set up
  - Discovery screen displays poems and poets
  - Adaptive ListDetailPaneScaffold is used for tablet/foldable support
  - Edge-to-Edge display is enabled
  - App runs without crashing

### Task_3_ReaderAndSharing: Create the Immersive Reader screen and the sharing functionality.
- **Status:** COMPLETED
- **Updates:** Implemented ReaderScreen with support for Kannada typography and text resizing (12sp to 36sp). Added theme switching (Default, Sepia, Parchment, Dark). Implemented sharing as styled text and image snippets using FileProvider and bitmap capture. Integrated ReaderScreen into Navigation 3. Followed Material 3 guidelines.
- **Acceptance Criteria:**
  - Reader screen supports Kannada typography and text resizing
  - Theme switching within the reader works
  - Share as styled text/image snippet is implemented
  - UI follows Material 3 guidelines

### Task_4_KanijaAndTheming: Implement the Personal Collection (Kanija) feature and refine the app's visual identity (Vibrant M3 theme and Adaptive Icon).
- **Status:** COMPLETED
- **Updates:** Fixed favorites button hit box by ensuring independent click handling. Updated mock data to native Kannada script (e.g., Kuvempu's 'O Nanna Chetana'). Removed debug database deletion to allow persistence. Optimized typography for Kannada script (line heights and spacing). Localized main navigation labels.
- **Acceptance Criteria:**
  - Favorites can be saved and viewed in Kanija screen
  - Vibrant and energetic Material 3 color scheme is applied
  - Adaptive app icon is created and integrated
  - Full Edge-to-Edge display is verified across all screens

### Task_5_RunAndVerify: Final run and verification of the application.
- **Status:** COMPLETED
- **Updates:** Fixed the database initialization bug by ensuring population occurs after the instance is built. Incremented database version to 2 and enabled destructive migration to refresh mock data. Verified native Kannada script in the data population method. App builds successfully.
- **Acceptance Criteria:**
  - Application is stable with no crashes
  - All features from the brief are functional
  - UI matches Material 3 and vibrant aesthetic
  - Navigation is smooth on all device types
- **Duration:** N/A

