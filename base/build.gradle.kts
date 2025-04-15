plugins {
    alias(libs.plugins.publishdata)
    alias(libs.plugins.shadow)
}

group = "${rootProject.group}.base"
version = "1.0.0"

dependencies {
    implementation(platform(libs.mycelium.bom))
    implementation(project(":api"))
    implementation(libs.worldSeed) {
        exclude(group = "com.github.Minestom", module = "Minestom")
    }
    compileOnly(libs.minestom)
    testImplementation(platform(libs.mycelium.bom))
    testImplementation(project(":api"))
    testImplementation(libs.minestom)
    testImplementation(libs.minestom.test)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}

publishData {
    addBuildData()
    useGitlabReposForProject("327", "https://gitlab.onelitefeather.dev/")
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