rootProject.name = "vulpes"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://reposilite.worldseed.online/public")
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
            version("microtus","1.5.1")
            version("bom", "1.1.2")
            version("shadow", "8.3.6")

            library("microtus.bom", "net.onelitefeather.microtus", "bom").versionRef("microtus")
            library("mycelium.bom", "net.theevilreaper.mycelium.bom", "mycelium-bom").versionRef("bom")

            library("minestom", "net.onelitefeather.microtus", "Microtus").withoutVersion()
            library("minestom-test", "net.onelitefeather.microtus.testing", "testing").withoutVersion()
            library("junit.api", "org.junit.jupiter", "junit-jupiter-api").withoutVersion()
            library("junit.engine", "org.junit.jupiter", "junit-jupiter-engine").withoutVersion()
            library("worldSeed", "net.worldseed.multipart", "WorldSeedEntityEngine").version("11.3.1")

            plugin("shadow", "com.gradleup.shadow").versionRef("shadow")
        }
    }
}
