plugins {
    alias(libs.plugins.forge.gradle)
//    alias(libs.plugins.librarian)
}

repositories {
//    maven {
//        name = "parchmentmc"
//        url = uri("https://maven.parchmentmc.org")
//    }
    mavenCentral()
}

minecraft {
    mappings("official", libs.versions.minecraft.get())
}

dependencies {
    minecraft(libs.forge)
}

tasks {
    jar {
        from(rootProject.sourceSets.main.get().output)
        from("LICENSE") {
            rename { "${it}_MixinExtras" }
        }
        finalizedBy("reobfJar")
        manifest.attributes(
            "FMLModType" to "GAMELIBRARY",
        )
    }

    sourcesJar {
        from(rootProject.sourceSets.main.get().allSource)
    }
}
