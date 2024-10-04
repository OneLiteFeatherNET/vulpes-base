plugins {
    alias(libs.plugins.publishdata)
}

group = "${rootProject.group}.api"
version = "1.0.0"

dependencies {
    implementation(platform(libs.dungeon.base.bom))
    compileOnly(libs.minestom)
    testImplementation(platform(libs.dungeon.base.bom))
    testImplementation(libs.minestom)
    testImplementation(libs.minestom.test)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}

publishData {
    addBuildData()
    useGitlabReposForProject("89", "https://gitlab.onelitefeather.dev/")
    publishTask("jar")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            // configure the publication as defined previously.
            publishData.configurePublication(this)
            version = publishData.getVersion(false)
        }
    }
    repositories {
        maven {
            credentials(HttpHeaderCredentials::class) {
                name = "Job-Token"
                value = System.getenv("CI_JOB_TOKEN")
            }
            authentication {
                create("header", HttpHeaderAuthentication::class)
            }

            name = "Gitlab"
            // Get the detected repository from the publish data
            url = uri(publishData.getRepository())
        }
    }
}
