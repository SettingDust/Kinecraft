plugins {
    java
    `maven-publish`

    alias(catalog.plugins.shadow)
}

base { archivesName.set(rootProject.base.archivesName.get()) }

dependencies {
    shadow(project("nested-fabric")) {
        isTransitive = false
    }
    shadow(project("nested-neoforge")) {
        isTransitive = false

        attributes {
            attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, JavaVersion.VERSION_21.majorVersion.toInt())
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
