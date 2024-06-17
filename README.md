# Kinecraft Serialization
https://github.com/SettingDust/kinecraft-serialization  
It's a lib for (de)serializing minecraft tag(NBT), chat component, ByteBuf with any serializable objects.   
Writing in kotlin and works with `kotlinx.serialization`  
And with 
  - [tag serializers](https://github.com/SettingDust/kinecraft-serialization/blob/main/src/main/kotlin/settingdust/kinecraft/serialization/TagSerializer.kt) you can [describe tags](https://github.com/SettingDust/kinecraft-serialization/blob/main/src/settingdust/kinecraft/serialization/TagSerializer.kt) with the correct type instead of string. So that it will encode/decode with the format you are using as "real" type of the tag.

## Usage

```kts
repositories {
    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven") {
                name = "Modrinth"
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    val kinecraftSerializationVersion = "1.6.7"
    
    compileOnly("maven.modrinth:kinecraft-serialization:$kinecraftSerializationVersion:common")
    // Fabric Loom
    runtimeOnly("maven.modrinth:kinecraft-serialization:$kinecraftSerializationVersion:fabric")
    include("maven.modrinth:kinecraft-serialization:$kinecraftSerializationVersion")
    // NeoGradle
    runtimeOnly("maven.modrinth:kinecraft-serialization:$kinecraftSerializationVersion:neoforge")
    jarInJar("maven.modrinth:kinecraft-serialization:$kinecraftSerializationVersion")
}
```

[`MinecraftTag`](https://github.com/SettingDust/kinecraft-serialization/blob/main/src/main/kotlin/settingdust/kinecraft/serialization/MinecraftTagFormat.kt) is for serialization between data class and Minecraft tag classes.
Reading [tag test](https://github.com/SettingDust/kinecraft-serialization/blob/main/src/test/kotlin/settingdust/kinecraft/serialization/MinecraftTagTest.kt) and [bytebuf test](https://github.com/SettingDust/kinecraft-serialization/blob/main/src/test/kotlin/settingdust/kinecraft/serialization/ByteBufTest.kt) for more usage
