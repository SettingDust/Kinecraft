plugins {
    alias(catalog.plugins.neoforge.moddev)
    alias(catalog.plugins.kotlin.jvm)
    alias(catalog.plugins.kotlin.plugin.serialization)
    `maven-publish`
}

base { archivesName.set("${rootProject.base.archivesName.get()}-${project.name}") }

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    withSourcesJar()

    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    jvmToolchain(21)
}

neoForge {
    version = catalog.neoforge.get().version!!
}

dependencies {
    implementation(catalog.kotlinx.serialization.core)
    implementation(catalog.kotlinx.serialization.json)
    implementation(catalog.kotlin.reflect)
    jarJar(project(":neoforge"))
    jarJar(project(":lexforge"))
}

tasks {
    jar {
        from("LICENSE") { rename { "${it}_KinecraftSerialization" } }
        manifest.attributes(
            "FMLModType" to "GAMELIBRARY"
        )
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    sourcesJar { from(rootProject.sourceSets.main.get().allSource) }
}

publishing {
    publications {
        create<MavenPublication>(rootProject.name) {
            groupId = "${rootProject.group}"
            artifactId = base.archivesName.get()
            version = "${rootProject.version}"
            from(components.getByName("java"))
        }
    }
}
