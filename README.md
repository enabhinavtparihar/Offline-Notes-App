# Offline-First Notes App

## Project Overview
An Android notes application built using Kotlin and Jetpack Compose. The application allows users to create, view, edit, delete, search, and organize notes without requiring an internet connection.

## Features
- Create and edit notes
- Delete notes with confirmation
- Search notes by title and content
- Sort notes by newest first and title A–Z
- Automatic local data persistence
- Offline functionality
- Empty and no-results states

## Technology Stack
- Kotlin
- Jetpack Compose
- MVVM Architecture
- Room Database
- Kotlin Coroutines
- Flow and StateFlow
- Navigation Compose

## Architecture
The application follows MVVM architecture.

- Data Layer: Room Entity, DAO, Database and Repository.
- ViewModel Layer: Manages application state and business logic.
- UI Layer: Jetpack Compose screens.
- Navigation Layer: Handles navigation between screens.

## Database
Room Database is used for local storage.

Note fields:
- id
- title
- content
- createdAt
- updatedAt

## How to Run
1. Clone the GitHub repository.
2. Open the project in Android Studio.
3. Allow Gradle Sync to complete.
4. Connect an Android device or start an emulator.
5. Click Run.

## Testing
The following features have been tested:
- Create notes
- Edit notes
- Delete notes
- Search by title and content
- Sorting
- Persistence after app restart
- Offline functionality

## Libraries
- AndroidX Room
- Jetpack Compose Material 3
- Navigation Compose
- AndroidX Lifecycle
- Kotlin Coroutines

## Known Limitations
- Notes are stored locally on the device.
- Cloud synchronization and account-based access are not implemented.
- No backend is required.

## Project Type
Android Developer Coding Assignment – Round 2.
