val hytaleServerJar: String by project

dependencies {
    api(project(":common"))
    compileOnly(files(hytaleServerJar))
}
