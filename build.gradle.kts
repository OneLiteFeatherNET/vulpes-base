group = "net.theevilreaper.vulpes"

subprojects {

    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    tasks {
        getByName<JavaCompile>("compileJava") {
            options.release.set(21)
            options.encoding = "UTF-8"
        }
        getByName<JacocoReport>("jacocoTestReport") {
            dependsOn(project.tasks.findByPath("test"))
            reports {
                xml.required.set(true)
            }
        }
        getByName<Test>("test") {
            finalizedBy(project.tasks.findByPath("jacocoTestReport"))
            useJUnitPlatform()
            testLogging {
                events("passed", "skipped", "failed")
            }
        }
    }
    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}