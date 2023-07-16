plugins {
    alias(libs.plugins.fabric.loom)
}

repositories {
    maven {
        name = "parchmentmc"
        url = uri("https://maven.parchmentmc.org")
    }
    mavenCentral()
}

dependencies {
    minecraft(libs.minecraft)

    mappings(
        loom.layered {
            officialMojangMappings()
            parchment(
                variantOf(libs.parchment) {
                    artifactType("zip")
                },
            )
        },
    )
}

tasks {
    jar {
        from(rootProject.sourceSets.main.get().output)
        from("LICENSE") {
            rename { "${it}_MixinExtras" }
        }
    }

    sourcesJar {
        from(rootProject.sourceSets.main.get().allSource)
    }
}
