plugins {
    java
    `maven-publish`

    alias(libs.plugins.shadow)
}

base { archivesName.set(rootProject.base.archivesName.get()) }

dependencies {
    subprojects.forEach {
        shadow(project(it.path)) {
            isTransitive = false
        }
    }
}

tasks {
    shadowJar {
        configurations = listOf(project.configurations.shadow.get())

        mergeServiceFiles()
        archiveClassifier.set("")

        doFirst {
            manifest {
                from(
                    configurations
                        .flatMap { it.files }
                        .map { zipTree(it) }
                        .map { zip -> zip.find { it.name.equals("MANIFEST.MF") } }
                )
            }
        }
    }
}

rootProject.publishing {
    publications {
        named<MavenPublication>("maven") {
            artifact(tasks.shadowJar)
        }
    }
}
