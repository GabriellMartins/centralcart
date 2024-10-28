plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

group = "br.com.centralcart"
version = "1.0.0-61fb4c"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    implementation ("io.socket:socket.io-client:2.1.0")
    implementation("com.squareup.okhttp3:okhttp:3.12.0")
    implementation("org.bstats:bstats-bungeecord:3.0.2")
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
    compileOnly(files("../libraries/Travertine.jar"))
    implementation("org.bstats:bstats-bukkit:3.0.2")
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly(files("../libraries/carbonspigot.jar"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.processResources {
    from(sourceSets.main.get().allSource.srcDirs) {
        expand(mapOf("pluginVersion" to version))
        include("plugin.yml")
        include("bungee.yml")
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

tasks.build.get().dependsOn("shadowJar")

tasks.shadowJar {
    archiveFileName.set("CentralCart.jar")
    destinationDirectory.set(file("C:\\Users\\GabrielMartins\\IdeaProjects\\minecraft-server"))
    relocate("org.bstats", "${project.group}.metrics")
}