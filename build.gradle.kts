import java.util.Properties
import java.io.FileInputStream
import java.io.FileOutputStream 

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta12"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
}

group = "org.clockworx.hyperconomy"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    // ServiceIO repository (official Maven repository)
    maven("https://repo.thenextlvl.net/releases")
    // Vault repository (for API interface - ServiceIO implements this interface)
    maven("https://nexus.hc.to/content/repositories/pub_releases")
    // Local Maven repository for SimpleDataLib
    mavenLocal()
}

dependencies {
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")
    
    // Database - Core
    implementation("org.hibernate:hibernate-core:6.6.40.Final") 
    implementation("org.hibernate:hibernate-community-dialects:6.6.40.Final")
    implementation("org.flywaydb:flyway-core:11.20.0")
    implementation("org.flywaydb:flyway-mysql:11.20.0")
    implementation("com.mysql:mysql-connector-j:9.1.0")
    implementation("org.xerial:sqlite-jdbc:3.49.1.0")
    implementation("org.postgresql:postgresql:42.7.5")
    
    // Database - Connection Pools (Shade this)
    implementation("com.zaxxer:HikariCP:6.3.0")
    implementation("org.hibernate.orm:hibernate-hikaricp:6.6.40.Final")

    // Jakarta Persistence API
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    
    // Logging - Make sure we use compatible versions
    implementation("org.jboss.logging:jboss-logging:3.6.1.Final")
    implementation("org.jboss.logging:jboss-logging-annotations:2.2.1.Final")
    // Use Logback for SLF4J implementation compatible with Paper
    implementation("ch.qos.logback:logback-classic:1.5.23")
    // implementation("org.slf4j:slf4j-api:2.0.9") // Provided by Paper/Logback
    
    // Lombok (for boilerplate code reduction)
    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
    
    // ServiceIO API - Modern drop-in replacement for Vault
    // ServiceIO implements the Vault Economy interface for compatibility
    // Using ServiceIO's official repository: https://repo.thenextlvl.net/releases
    // Version 2.3.1 (latest available in Maven; 2.3.2/2.3.3 exist on GitHub but not yet published)
    compileOnly("net.thenextlvl.services:service-io:2.3.1")
    
    // Vault API - Used as fallback for compilation (ServiceIO implements this interface)
    // ServiceIO is a drop-in replacement that implements net.milkbowl.vault.economy.Economy
    // Exclude Bukkit dependency since we're using Paper API
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit", module = "bukkit")
    }
    
    // SimpleDataLib - Using local fork as composite build
    // Note: This library is external and wasn't renamed, so imports use regalowl.simpledatalib
    // Using includeBuild in settings.gradle.kts to include the local SimpleDataLib project
    implementation("regalowl.simpledatalib:simpledatalib:0.1.088-SNAPSHOT")
    
    // Servlet API - Required for web interface (provided at runtime by Jetty/container)
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
    
    // JAXB API - Required for XML binding (provided at runtime in Java 9+)
    compileOnly("javax.xml.bind:jaxb-api:2.3.1")
    
    // Jetty - Required for web interface (provided at runtime)
    compileOnly("org.eclipse.jetty:jetty-server:8.1.9.v20130131")
    compileOnly("org.eclipse.jetty:jetty-servlet:8.1.9.v20130131")
    
    // Note: ServiceIO should be installed as a plugin at runtime
    // It implements the Vault Economy interface, so plugins can use either API
    
    // Add any additional dependencies here
    // testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

// Configure paperweight for Mojang-mapped production output
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

// Store version at configuration time
val projectVersion = version.toString()

// Version is automatically loaded from gradle.properties by default in recent Gradle versions
// You can access it via project.version or just 'version'
println("Initial project version from properties: $version")

// Set the project version explicitly if needed elsewhere
project.version = version.toString() 

/**
 * Task to increment the patch version in gradle.properties.
 * Refactored to declare inputs/outputs for configuration cache compatibility.
 */
abstract class IncrementPatchVersionTask : DefaultTask() {

    /**
     * The gradle.properties file to modify.
     */
    @get:OutputFile
    abstract val propertiesFile: RegularFileProperty

    /**
     * Executes the version increment logic.
     * Reads the current version from gradle.properties, increments the patch number,
     * and writes the updated version back to the file.
     */
    @TaskAction
    fun execute() {
        val propsFile = propertiesFile.get().asFile
        if (!propsFile.exists()) {
            throw GradleException("gradle.properties file not found at: ${propsFile.path}")
        }

        val props = Properties()
        propsFile.reader(Charsets.UTF_8).use { reader ->
            props.load(reader)
        }

        val currentVersion = props.getProperty("version")
        if (currentVersion == null) {
            throw GradleException("Could not find 'version' property in ${propsFile.path}")
        }
        logger.quiet("Current version from file: $currentVersion")

        val versionRegex = """^(\d+)\.(\d+)\.(\d+)(.*)$""".toRegex()
        val matchResult = versionRegex.find(currentVersion)
            ?: throw GradleException("Version '$currentVersion' does not match expected Major.Minor.Patch format.")

        val (majorStr, minorStr, patchStr, suffix) = matchResult.destructured
        var patch = patchStr.toInt()
        patch++

        val newVersion = "${majorStr}.${minorStr}.$patch$suffix"
        logger.quiet("Incremented version to: $newVersion")

        props.setProperty("version", newVersion)
        propsFile.writer(Charsets.UTF_8).use { writer ->
            props.store(writer, null)
        }
    }
}

// Register the task using the custom task class
val incrementPatchVersion = tasks.register<IncrementPatchVersionTask>("incrementPatchVersion") {
    propertiesFile.set(project.layout.projectDirectory.file("gradle.properties"))
    // Ensure this task only runs if the properties file exists
    onlyIf { propertiesFile.get().asFile.exists() }
    // Ensure this task always runs, as it modifies its output in place
    outputs.upToDateWhen { false }
}

tasks {
    // Configure shadowJar - critical for proper relocation
    shadowJar {
        enableRelocation = false
        archiveClassifier.set("all")
        
        // Add mappings namespace to shadowJar manifest as well
        manifest {
            attributes("paperweight-mappings-namespace" to "mojang")
        }

        // Relocate packages - include all required dependencies
        relocate("com.zaxxer.hikari", "org.clockworx.vampire.lib.hikari")
        relocate("org.hibernate", "org.clockworx.vampire.lib.hibernate")
        relocate("org.jboss.logging", "org.clockworx.vampire.lib.jboss.logging")
        relocate("jakarta.persistence", "org.clockworx.vampire.lib.jakarta.persistence")
        // SLF4J API is usually provided by the server environment (like Paper), avoid relocating unless necessary
        // relocate("org.slf4j", "org.clockworx.vampire.lib.slf4j")
        relocate("org.flywaydb", "org.clockworx.vampire.lib.flywaydb")
        relocate("ch.qos.logback", "org.clockworx.vampire.lib.logback") // Relocate Logback
        // Relocate the Xerial part of SQLite driver, but NOT the core org.sqlite part
        relocate("org.xerial.sqlite", "org.clockworx.vampire.lib.xerial.sqlite")
        
        // IMPORTANT: Specifically exclude the core SQLite package from relocation
        // to prevent breaking native library loading (JNI).
        exclude("org/sqlite/**")

        // Merge service files - critical for service provider loading
        mergeServiceFiles()
    }

    // Configure jar task
    jar {
        manifest {
            attributes(
                "Name" to project.name,
                "Version" to provider { project.version.toString() },
                "Description" to "A modern economy and shop plugin for Minecraft",
                "Authors" to "RegalOwl, ClockWorX",
                "Main" to "org.clockworx.hyperconomy.bukkit.BukkitConnector",
                // Tell Paper that this plugin uses Mojang mappings (required for 1.20.5+)
                "paperweight-mappings-namespace" to "mojang"
            )
        }
        from("LICENSE") {
            rename { "${it}_${project.name}" }
        }
        dependsOn("shadowJar")
    }

    clean {
        delete(layout.buildDirectory)
    }
    
    // Configure test task
    test {
        useJUnitPlatform()
    }
    
    // Process resources
    processResources {
        // Process all resource files for version expansion
        // Note: ${project.version} will be replaced with the version value
        filesMatching(listOf("config.yml", "plugin.yml", "levels.yml")) {
            // Replace ${project.version} with actual version
            filter { line -> line.replace("\${project.version}", project.version.toString()) }
            expand(
                "version" to project.version
            )
        }
    }
}

// Configure reobfJar to use shadowJar as input and rename output to -paper
// This ensures all resources (plugin.yml, etc.) and dependencies are included
tasks.named("reobfJar").configure {
    val shadowJar = tasks.named("shadowJar")
    val remapJar = this as io.papermc.paperweight.tasks.RemapJar
    // Use the shadowJar output file as input for reobfuscation
    remapJar.inputJar.set(
        shadowJar.flatMap { task -> 
            task.outputs.files.singleFile.let { file ->
                layout.file(providers.provider { file })
            }
        }
    )
    // Rename output to -paper by configuring the output file
    doLast {
        val outputFile = remapJar.outputJar.get().asFile
        val newFile = File(outputFile.parent, outputFile.name.replace("-reobf.jar", "-paper.jar"))
        if (outputFile.exists() && outputFile != newFile) {
            outputFile.renameTo(newFile)
        }
    }
}

// Ensure the production JAR is built with the assemble task
tasks.assemble {
    dependsOn(tasks.reobfJar)
}

// Ensure the 'build' task runs the increment task AFTER finishing
afterEvaluate {
    tasks.named("build").get().finalizedBy(tasks.named("incrementPatchVersion"))
}
