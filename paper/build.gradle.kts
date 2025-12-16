import io.papermc.paperweight.util.constants.paperweightDebug

plugins {
    `multiloader-loader`
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
    kotlin("jvm") version "2.2.0"
}

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven("https://repo.essentialsx.net/releases/")
    maven("https://repo.william278.net/releases")
}

dependencies {
    paperweight.paperDevBundle("${commonMod.prop("minecraft_version")}-R0.1-SNAPSHOT")
    compileOnly("net.essentialsx:EssentialsX:${commonMod.dep("essentials")}")
    compileOnly("net.william278.huskhomes:huskhomes-bukkit:${commonMod.dep("huskhomes")}")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}