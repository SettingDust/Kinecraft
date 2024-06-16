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

artifacts {
    archives(tasks.shadowJar.flatMap { it.archiveFile }) {
        builtBy(tasks.shadowJar)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }

    repositories {
        maven("file://${rootProject.projectDir}/publish")
    }
}
