# Minecraft Tag Serialization
It's a lib for developer

## Usage

```groovy
repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = "https://api.modrinth.com/maven"
            }
        }
        filter {
            includeGroup "maven.modrinth"
        }
    }
}

dependencies {
    implementation 'maven.modrinth:nbt-tag-serialization:0.1.0'
}
```

[`MinecraftTag`](src/main/kotlin/settingdust/tag/serialization/MinecraftTagFormat.kt) is using for serialization between data class and Minecraft tag classes.
Reading [test](src/test/kotlin/settingdust/tag/serialization/MinecraftTagTest.kt) for more info
