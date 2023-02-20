# GitHub Update Checker

[![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/heroslender/github-update-checker/build.yml?label=Build&logo=GitHub)](https://github.com/heroslender/github-update-checker/actions/workflows/build.yml)
![Maven metadata URL](https://img.shields.io/maven-metadata/v?label=Version&metadataUrl=https%3A%2F%2Fnexus.heroslender.com%2Frepository%2Fmaven-snapshots%2Fcom%2Fheroslender%2Fgithub-update-checker%2Fmaven-metadata.xml)
[![GitHub stars](https://img.shields.io/github/stars/heroslender/github-update-checker.svg?label=Stars)](https://github.com/heroslender/menu-framework/stargazers)
[![GitHub issues](https://img.shields.io/github/issues-raw/heroslender/github-update-checker.svg?label=Issues)](https://github.com/heroslender/github-update-checker/issues)
[![GitHub last commit](https://img.shields.io/github/last-commit/heroslender/github-update-checker.svg?label=Last%20Commit)](https://github.com/heroslender/github-update-checker/commit)
[![Open Source Love](https://badges.frapsoft.com/os/v2/open-source.png?v=103)](https://github.com/ellerbrock/open-source-badges/)

An api to check for plugin updates on GitHub Releases based on the SemVer specification.

## Dependency

### Gradle kts

```kotlin
repositories {
    maven("https://nexus.heroslender.com/repository/maven-public/")
}

dependencies {
    implementation("com.heroslender:github-update-checker:1.0.0")
}
```

### Maven

```xml
<repository>
    <id>heroslender-repo</id>
    <url>https://nexus.heroslender.com/repository/maven-public/</url>
</repository>
```

```xml
<dependency>
    <groupId>com.heroslender</groupId>
    <artifactId>github-update-checker</artifactId>
    <version>1.0.0</version>
</dependency>
```
