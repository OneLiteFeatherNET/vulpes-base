plugins {
    java
    `java-library`
    jacoco
    `maven-publish`
    alias(libs.plugins.shadow)
}

group = "net.thevilreaper.vulpes.base"
version = "1.0.0"

dependencies {
    implementation(platform(libs.mycelium.bom))
    implementation(libs.worldSeed) {
        exclude(group = "com.github.Minestom", module = "Minestom")
    }
    compileOnly(libs.minestom)
    testImplementation(platform(libs.mycelium.bom))
    testImplementation(libs.minestom)
    testImplementation(libs.minestom.test)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    compileJava {
        options.release.set(21)
        options.encoding = "UTF-8"
    }

    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required.set(true)
        }
    }

    test {
        finalizedBy(jacocoTestReport)
        useJUnitPlatform()
        jvmArgs("-Dminestom.inside-test=true")
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            authentication {
                credentials(PasswordCredentials::class) {
                    username = System.getenv("ONELITEFEATHER_MAVEN_USERNAME")
                    password = System.getenv("ONELITEFEATHER_MAVEN_PASSWORD")
                }
            }
            name = "OneLiteFeatherRepository"
            url = if (project.version.toString().contains("SNAPSHOT")) {
                uri("https://repo.onelitefeather.dev/onelitefeather-snapshots")
            } else {
                uri("https://repo.onelitefeather.dev/onelitefeather-releases")
            }
        }
    }
}