# gradle-code-manifest-plugin
[![Build Status](https://travis-ci.org/elastic/gradle-code-manifest-plugin.svg?branch=master)](https://travis-ci.org/elastic/gradle-code-manifest-plugin)

Gradle plugin to generate a manifest file consumed by https://github.com/elastic/java-langserver.

If you want to file a issue, file it in: https://github.com/elastic/code/issues.


## Upload to local maven repository
Use `gradle uploadArchives`

## Usage
Add following script to **build.grdle**:
```
buildscript {
    repositories {
         mavenLocal()
    }
    dependencies {
        classpath 'co.elastic.code:gradle-manifest-plugin:1.0-SNAPSHOT'
    }
}
apply plugin: 'gradle-manifest-plugin'
```
Then run `gradle genManifest` will generate a file called **manifest.json** in the project folder.
