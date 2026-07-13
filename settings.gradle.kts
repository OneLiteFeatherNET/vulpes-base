rootProject.name = "vulpes"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://reposilite.atlasengine.ca/public")
        maven {
            name = "OneLiteFeatherRepository"
            url = uri("https://repo.onelitefeather.dev/onelitefeather")
            if (System.getenv("CI") != null) {
                credentials {
                    username = System.getenv("ONELITEFEATHER_MAVEN_USERNAME")
                    password = System.getenv("ONELITEFEATHER_MAVEN_PASSWORD")
                }
            } else {
                credentials(PasswordCredentials::class)
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
        }
    }

    versionCatalogs {
        create("libs") {
            version("shadow", "9.5.1")
            version("bom", "1.7.2")
            version("worldSeed", "11.6.1")
            version("cyclonedx", "3.2.4")
            version("slf4j", "2.0.18")

            library("mycelium.bom", "net.onelitefeather", "mycelium-bom").versionRef("bom")
            library("minestom", "net.minestom", "minestom").withoutVersion()
            library("cyano", "net.onelitefeather", "cyano").withoutVersion()
            library("slf4j.api", "org.slf4j", "slf4j-api").versionRef("slf4j")
            library("junit.api", "org.junit.jupiter", "junit-jupiter-api").withoutVersion()
            library("junit.params", "org.junit.jupiter", "junit-jupiter-params").withoutVersion()
            library("junit.engine", "org.junit.jupiter", "junit-jupiter-engine").withoutVersion()
            library("junit.platform.launcher", "org.junit.platform", "junit-platform-launcher").withoutVersion()

            library("worldSeed", "net.worldseed.multipart", "WorldSeedEntityEngine").versionRef("worldSeed")

            plugin("shadow", "com.gradleup.shadow").versionRef("shadow")
            plugin("cyclonedx", "org.cyclonedx.bom").versionRef("cyclonedx")
        }
    }
}
