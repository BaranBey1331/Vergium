# Vergium Initial Setup Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Initialize the Forge 1.20.1 project structure and set up GitHub Actions for automated .jar builds.

**Architecture:** Standard Forge Gradle project with a focus on Xclipse 940 (ANGLE/Vulkan) optimizations.

**Tech Stack:** Java 17, Forge 1.20.1, Gradle, GitHub Actions.

---

### Task 1: Initialize Forge Project Structure

**Files:**
- Create: `build.gradle`
- Create: `gradle.properties`
- Create: `settings.gradle`
- Create: `src/main/resources/META-INF/mods.toml`

- [ ] **Step 1: Create build.gradle**
```groovy
plugins {
    id 'eclipse'
    id 'idea'
    id 'net.minecraftforge.gradle' version '[6.0,6.2)'
}

group = 'com.vergium'
version = '0.1.0'

base {
    archivesName = 'vergium'
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

minecraft {
    mappings channel: 'official', version: '1.20.1'
    copyIdeResources = true
    runs {
        client {
            workingDirectory project.file('run')
            property 'forge.logging.markers', 'REGISTRIES'
            property 'forge.logging.console.level', 'debug'
            property 'forge.enabledGameTestNamespaces', 'vergium'
            mods {
                vergium {
                    source sourceSets.main
                }
            }
        }
    }
}

sourceSets.main.resources { srcDir 'src/generated/resources' }

repositories {
    maven {
        name = "CurseMaven"
        url = "https://www.cursemaven.com"
        content {
            includeGroup "curse.maven"
        }
    }
}

dependencies {
    minecraft 'net.minecraftforge:forge:1.20.1-47.2.0'
}

tasks.named('processResources', ProcessResources).configure {
    var replaceProperties = [
            minecraft_version: '1.20.1',
            minecraft_version_range: '[1.20.1,1.21)',
            forge_version: '47.2.0',
            forge_version_range: '[47,)',
            loader_version_range: '[47,)',
            mod_id: 'vergium',
            mod_name: 'Vergium',
            mod_license: 'MIT',
            mod_version: '0.1.0',
            mod_authors: 'BaranBey1331',
            mod_description: 'High-performance optimization mod for Xclipse 940 GPU.'
    ]
    inputs.properties replaceProperties
    replaceProperties.put 'project', project

    filesMatching(['META-INF/mods.toml', 'pack.mcmeta']) {
        expand replaceProperties
    }
}

tasks.withType(JavaCompile).configureEach {
    options.encoding = 'UTF-8'
}
```

- [ ] **Step 2: Create gradle.properties**
```properties
org.gradle.jvmargs=-Xmx3G
org.gradle.parallel=true
minecraft_version=1.20.1
forge_version=47.2.0
```

- [ ] **Step 3: Create settings.gradle**
```groovy
rootProject.name = 'vergium'
```

- [ ] **Step 4: Create mods.toml**
```toml
modLoader="javafml"
loaderVersion="${loader_version_range}"
license="${mod_license}"

[[mods]]
modId="${mod_id}"
version="${mod_version}"
displayName="${mod_name}"
authors="${mod_authors}"
description='''
${mod_description}
'''

[[dependencies.vergium]]
    modId="forge"
    mandatory=true
    versionRange="${forge_version_range}"
    ordering="NONE"
    side="BOTH"

[[dependencies.vergium]]
    modId="minecraft"
    mandatory=true
    versionRange="${minecraft_version_range}"
    ordering="NONE"
    side="BOTH"
```

- [ ] **Step 5: Commit initial structure**
```bash
git add build.gradle gradle.properties settings.gradle src/main/resources/META-INF/mods.toml
git commit -m "chore: Initialize Forge 1.20.1 project structure"
```

### Task 2: Setup GitHub Actions for .jar Build

**Files:**
- Create: `.github/workflows/build.yml`

- [ ] **Step 1: Create build.yml**
```yaml
name: Java CI with Gradle

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

permissions:
  contents: read

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
    - name: Setup Gradle
      uses: gradle/actions/setup-gradle@v3
    - name: Build with Gradle
      run: ./gradlew build
    - name: Upload Artifact
      uses: actions/upload-artifact@v4
      with:
        name: Vergium-jar
        path: build/libs/*.jar
```

- [ ] **Step 2: Commit GitHub Workflow**
```bash
git add .github/workflows/build.yml
git commit -m "chore: Add GitHub Actions workflow for building .jar"
```
