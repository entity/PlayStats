java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.compileJava {
    options.release.set(25)
}

repositories {
    maven("https://maven.wardle.systems/public/")
}

dependencies {
    api(project(":common"))
    compileOnly("com.hypixel.hytale:HytaleServer:1.0-SNAPSHOT")
}

val hytaleServerDir: String? by project

tasks.shadowJar {
    archiveClassifier.set("")
    archiveFileName.set("PlayStats-Hytale-${project.version}.jar")

    if (hytaleServerDir != null) {
        doLast {
            copy {
                from(archiveFile)
                into(file(hytaleServerDir!!).resolve("mods"))
            }
        }
    }
}
