# Kinecraft Serialization
https://github.com/SettingDust/kinecraft-serialization  
It's a lib for (de)serializing minecraft tag(NBT), chat component, ByteBuf with any serializable objects.   
Writing in kotlin and works with `kotlinx.serialization`  
And with 
  - [tag serializers](https://github.com/SettingDust/kinecraft-serialization/blob/main/src/main/kotlin/settingdust/kinecraft/serialization/TagSerializer.kt) you can [describe tags](https://github.com/SettingDust/kinecraft-serialization/blob/main/src/settingdust/kinecraft/serialization/TagSerializer.kt) with the correct type instead of string. So that it will encode/decode with the format you are using as "real" type of the tag.
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
    implementation 'maven.modrinth:kinecraft-serialization:1.3.0'
}1
```

[`MinecraftTag`](https://github.com/SettingDust/kinecraft-serialization/blob/main/src/main/kotlin/settingdust/kinecraft/serialization/MinecraftTagFormat.kt) is for serialization between data class and Minecraft tag classes.
Reading [tag test](https://github.com/SettingDust/kinecraft-serialization/blob/main/src/test/kotlin/settingdust/kinecraft/serialization/MinecraftTagTest.kt) and [bytebuf test](https://github.com/SettingDust/kinecraft-serialization/blob/main/src/test/kotlin/settingdust/kinecraft/serialization/ByteBufTest.kt) for more usage
