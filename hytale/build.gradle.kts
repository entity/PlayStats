val hytaleServerJar: String by project

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.compileJava {
    options.release.set(25)
}

dependencies {
    api(project(":common"))
    compileOnly(files(hytaleServerJar))
}

tasks.shadowJar {
    doLast {
        copy {
            from(archiveFile)
            into("/Users/charlie/Code/HytaleServer/mods")
        }
    }
}
