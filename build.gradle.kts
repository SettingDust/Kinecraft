import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.text.SimpleDateFormat
import java.util.*


plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.quilt.loom)
    `maven-publish`
}

group = "${project.property("group")}"
version = "${project.property("version")}"

repositories {
    mavenCentral()
    maven {
        name = "parchmentmc"
        url = uri("https://maven.parchmentmc.org")
    }
}

dependencies {
    minecraft(libs.minecraft)

    mappings(loom.officialMojangMappings())

    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.reflect)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

java {
    // Still required by IDEs such as Eclipse and Visual Studio Code
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    // If this mod is going to be a library, then it should also generate Javadocs in order to aid with development.
    // Uncomment this line to generate them.
    // withJavadocJar()
}

tasks {
    processResources {
        inputs.property("version", project.version)
        inputs.property("group", project.group)

        filesMatching("fabric.mod.json") {
            expand(
                "id" to "minecraft_tag_serialization",
                "version" to project.version,
                "group" to project.group,
                "name" to "Minecraft Tag Serialization",
                "description" to "Kotlin serialization for Minecraft NBT tag classes",
                "author" to "SettingDust",
                "source" to "https://github.com/SettingDust/minecraft-tag-serialization"
            )
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("minecraft-tag-serialization") {
            groupId = "${rootProject.group}"
            artifactId = "minecraft-tag-serialization"
            version = rootProject.version.toString() + "SNAPSHOT.${SimpleDateFormat("YYYY.MMdd.HHmmss").format(Date())}"
            from(components.getByName("java"))
        }
    }
}
