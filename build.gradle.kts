plugins {
    java
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

repositories {
    mavenCentral()
    // Vault
    maven("https://nexus.heroslender.com/repository/maven-public/")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    compileOnly("org.jetbrains:annotations:23.0.0")

    testImplementation(platform("org.junit:junit-bom:5.7.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

group = "com.heroslender"
version = "1.4.0"

java {
    withJavadocJar()
    withSourcesJar()

    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    test {
        useJUnitPlatform()
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
}

publishing {
    repositories {
        maven {
            val target = if (project.version.toString().endsWith("-SNAPSHOT"))
                "https://nexus.heroslender.com/repository/maven-snapshots/"
            else
                "https://nexus.heroslender.com/repository/maven-releases/"

            name = "heroslender-nexus"
            url = uri(target)

            credentials {
                username = project.findProperty("nexus.user") as? String
                    ?: System.getenv("NEXUS_USERNAME")
                password = project.findProperty("nexus.key") as? String
                    ?: System.getenv("NEXUS_PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            artifactId = "github-update-checker"
            groupId = project.group.toString()
            version = project.version.toString()

            from(components["java"])

            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/heroslender/github-update-checker")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/heroslender/github-update-checker/blob/main/LICENSE")
                    }
                }

                developers {
                    developer {
                        id.set("heroslender")
                        name.set("Bruno Martins")
                        email.set("admin@heroslender.com")
                    }
                }

                scm {
                    connection.set("https://github.com/heroslender/github-update-checker.git")
                    developerConnection.set("git@github.com:heroslender/github-update-checker.git")
                    url.set("https://github.com/heroslender/github-update-checker")
                }
            }
        }
    }
}