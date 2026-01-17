val hytaleServerJar: String by project

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
