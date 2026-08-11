// Top-level build file. The Android plugin version is kept in libs.versions.toml,
// which is the same version-catalog style used in the class example project.
plugins {
    alias(libs.plugins.android.application) apply false

    // Allows Android to read the Firebase google-services.json file.
    id("com.google.gms.google-services") version "4.5.0" apply false
}
