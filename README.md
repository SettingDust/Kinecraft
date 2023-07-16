# Kinecraft Serialization
https://github.com/SettingDust/kinecraft-serialization  
It's a lib for (de)serializing minecraft tag(NBT), chat component, ~~packet~~ classes with any serializable objects.   
Writing in kotlin and works with kotlinx.serialization  
And with the 
  - [tag serializers](https://github.com/SettingDust/kinecraft-serialization/blob/main/src/main/kotlin/settingdust/kinecraft/serialization/TagSerializer.kt) you can [describe tags](https://github.com/SettingDust/kinecraft-serialization/blob/main/src/settingdust/kinecraft/serialization/TagSerializer.kt) with correct type instead of string. So that it will encode/decode with the format you are using as "real" type of the tag.
  - [component serializer](https://github.com/SettingDust/kinecraft-serialization/blob/main/src/main/kotlin/settingdust/kinecraft/serialization/ComponentSerializer.kt)

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
    implementation 'maven.modrinth:kinecraft-serialization:1.1.0'
}
```

[`MinecraftTag`](https://github.com/SettingDust/kinecraft-serialization/blob/main/src/main/kotlin/settingdust/kinecraft/serialization/MinecraftTagFormat.kt) is using for serialization between data class and Minecraft tag classes.
Reading [test](https://github.com/SettingDust/kinecraft-serialization/blob/main/src/test/kotlin/settingdust/kinecraft/serialization/MinecraftTagTest.kt) for more usage
