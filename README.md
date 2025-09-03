# Duck Cards – README

Eine moderne Karteikarten-App für Android. Du kannst Ordner erstellen, Fragen und Antworten verwalten und einen Quiz-Modus starten. Die App ist in **Kotlin** mit **AndroidX** und **Material Components** gebaut.

---

## Tech-Stack & Versionen

* **Programmiersprache:** Kotlin
* **Min. Android Version (minSdk):** 24 (Android 7.0)
* **Target SDK (targetSdk):** 33
* **Compile SDK (compileSdk):** 34
* **Build-Tools:** Android Gradle Plugin (AGP) 8.x
* **Kotlin JVM:** 17

---

## Features

* **Ordner erstellen** und Karteikarten organisieren
* **Fragen & Antworten** verwalten
* **Quiz-Modus** mit Bewertung über 3 Enten (grün = korrekt, gelb = unsicher, rot = falsch)
* **Responsive UI**: Karten passen sich automatisch der Bildschirmbreite an
* **Moderner Look** mit Material Design
* **Resultat-Ansicht** mit Auswertung deiner Antworten

---

## Installation

1. Projekt in **Android Studio** (Giraffe/Koala oder neuer) öffnen
2. Gradle Sync laufen lassen
3. Emulator (API 24+) oder echtes Gerät starten

Build & Run mit:

```bash
./gradlew installDebug
```

---

## Projektstruktur

```
app/
 ├─ src/main/
 │   ├─ java/com/example/duckcards/
 │   │   ├─ MainActivity.kt
 │   │   ├─ FolderView.kt
 │   │   ├─ QuestionListActivity.kt
 │   │   ├─ QuizActivity.kt
 │   │   ├─ Models.kt
 │   │   ├─ DatabaseHandler.kt
 │   │   ├─ Adapters/
 │   │   │   ├─ FolderAdapter.kt
 │   │   │   └─ QuestionAdapter.kt
 │   ├─ res/
 │   │   ├─ layout/
 │   │   ├─ drawable/
 │   │   └─ values/
 │   └─ AndroidManifest.xml
 └─ build.gradle
```

---

## Build-Konfiguration (`app/build.gradle`)

```gradle
android {
    namespace 'com.example.duckcards'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.duckcards"
        minSdk 24
        targetSdk 33
        versionCode 1
        versionName "1.0"
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        viewBinding true
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = '17'
    }
}
```

---

## Strings (`res/values/strings.xml`)

```xml
<resources>
    <string name="app_name">Duck Cards</string>
    <string name="bearbeiten">Bearbeiten</string>
    <string name="delete">Löschen</string>
    <string name="befragung_starten">Befragung starten</string>
    <string name="ordner_titel">Ordner Titel</string>
    <string name="name">Name</string>
    <string name="name_of_your_new_folder">Name of your new Folder :</string>
    <string name="name_of_new_folder">Name of new folder</string>
    <string name="new_folder">Neuer Ordner</string>
    <string name="ordner_anzeigen">Ordner anzeigen</string>
    <string name="items_count">%1$d Einträge</string>
    <string name="add_question">Frage hinzufügen</string>
    <string name="question_name">Fragetitel</string>
    <string name="question_text">Fragetext</string>
    <string name="answer_text">Antwort</string>
</resources>
```

---

## Troubleshooting

* **`Error inflating class MaterialButton`** → Stelle sicher, dass dein App-Theme von `Theme.MaterialComponents.*` erbt.
* **`resource … not found`** → Fehlende Strings oder Drawables in `res/values/strings.xml` oder `res/drawable/` ergänzen.
* **`Unresolved reference: Intent`** → Import ergänzen: `import android.content.Intent`

---

## Lizenz

MIT License

Copyright (c) 2025 Duck Cards Contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
