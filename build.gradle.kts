plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "MIROSHKA"
version = "1.2.0"

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven("https://repo.lanink.cn/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("cn.nukkit:Nukkit:MOT-SNAPSHOT")
    implementation("com.github.MEFRREEX:FormConstructor:3.1.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    
    filesMatching("plugin.yml") {
        expand(
            "name" to project.name,
            "version" to project.version
        )
    }
}

sourceSets {
    main {
        resources {
            srcDirs("src/main/resources")
            include("plugin.yml", "config.yml")
        }
    }
}

tasks {
    "build" {
        dependsOn(shadowJar)
    }
} 