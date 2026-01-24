import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val minecraftServerDir: String? by project

dependencies {
    api(project(":common"))
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
    compileOnly("com.google.code.gson:gson:2.11.0")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("net.luckperms:api:5.4")
    implementation("com.jeff-media:custom-block-data:2.2.2")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    archiveFileName.set("PlayStats-${project.version}.jar")
    minimize()
}

tasks.processResources {
    val props = mapOf("version" to project.version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

if (minecraftServerDir != null) {
    val pluginsDir = file(minecraftServerDir!!).resolve("plugins")

    tasks.register<Copy>("copyToServer") {
        group = "build"
        description = "Copies the plugin JAR to the Paper plugins directory."

        from(layout.buildDirectory.dir("libs"))
        include("PlayStats-*.jar")
        into(pluginsDir)
    }

    tasks.named("shadowJar") {
        finalizedBy("copyToServer")
    }
}
