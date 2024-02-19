// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    extra.apply {
        set("room_version", "2.6.1")
        set("lifecycle_version", "2.7.0")
        set("retrofit_version", "2.9.0")
        set("hilt_version", "2.48")
        set("paging_version", "3.2.1")
    }
}

plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}
