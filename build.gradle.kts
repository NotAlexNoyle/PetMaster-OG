plugins {
    id("java") // Tell gradle this is a java project.
    id("io.github.goooler.shadow") version "8.1.8"
    eclipse // Import eclipse plugin for IDE integration.
    kotlin("jvm") version "1.9.23" // Import kotlin jvm plugin for kotlin/java integration.
}

java {
    // Declare java version.
    sourceCompatibility = JavaVersion.VERSION_17
}

group = "com.hm.petmaster" // Declare bundle identifier.
version = "1.0" // Declare plugin version (will be in .jar).
val apiVersion = "1.19" // Declare minecraft server target version.

tasks.named<ProcessResources>("processResources") {
    val props = mapOf(
        "version" to version,
        "apiVersion" to apiVersion
    )

    inputs.properties(props) // Indicates to rerun if version changes.

    filesMatching("plugin.yml") {
        expand(props)
    }
}

repositories {
    mavenCentral()
    maven { // Consider using JitPack's official Maven repository instead

        url = uri("https://jitpack.io")
	}

    maven {
        url = uri("https://repo.purpurmc.org/snapshots")
    }
    
    maven {
        url = uri("https://repo.codemc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("org.purpurmc.purpur:purpur-api:1.19.4-R0.1-SNAPSHOT") // Declare purpur API version to be packaged.
    compileOnly("io.github.miniplaceholders:miniplaceholders-api:2.2.3") // Import MiniPlaceholders API.
	compileOnly("com.github.decentsoftware-eu:decentholograms:2.8.9") // Import DecentHolograms API.
	implementation(project(":libs:MCShared-OG")) // Import source-based MCShared-OG API.
	implementation(project(":libs:Utilities-OG")) // Import source-based Utilities-OG API.
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.shadowJar {
    exclude("io.github.miniplaceholders.*") // Exclude the MiniPlaceholders package from being shadowed.
    minimize()
}

tasks.jar {
    dependsOn(tasks.shadowJar)
    archiveClassifier.set("part")
}

tasks.shadowJar {
    archiveClassifier.set("") // Use empty string instead of null
    from("LICENSE") {
        into("/")
    }
}

tasks.jar {
    dependsOn("shadowJar")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
    options.encoding = "UTF-8"
    options.isFork = true
}

kotlin {
    jvmToolchain(17)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
        vendor = JvmVendorSpec.GRAAL_VM
    }
}
