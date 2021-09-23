import org.panteleyev.jpackage.ImageType

plugins {
    application
    id("org.panteleyev.jpackageplugin") version "1.3.1"
}

tasks.withType<Wrapper>().configureEach {
    gradleVersion = "7.2"
}

group = "mars"
version = "4.5.2"

application {
    mainClass.set("Mars")
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.weisj:darklaf-core:2.7.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

val jarsDirectory = File(buildDir, "jpackage-jars")
val distDirectory = File(buildDir, "jpackage-dist")

val cleanJarsDirectory = tasks.register<Delete>("cleanJarsDirectory") {
    delete(jarsDirectory)
}

val copyDependencies = tasks.register<Copy>("copyDependencies") {
    from(configurations.runtimeClasspath).into(jarsDirectory)
    dependsOn(cleanJarsDirectory)
    mustRunAfter(cleanJarsDirectory)
}

val copyJar = task("copyJar", Copy::class) {
    from(tasks.jar).into(jarsDirectory)
    dependsOn(cleanJarsDirectory)
    mustRunAfter(cleanJarsDirectory)
}

tasks.jpackage {
    dependsOn(copyDependencies, copyJar)

    input = jarsDirectory.absolutePath
    destination = distDirectory.absolutePath

    appName = "MARS"
    appDescription = "MIPS Assembler and Runtime Simulator"
    vendor = "app.org"
    copyright = "Copyright 2003-2014 Pete Sanderson and Kenneth Vollmar"
    licenseFile = project.file("LICENSE.txt").absolutePath

    mainJar = tasks.jar.get().archiveFileName.get()
    mainClass = application.mainClass.get()

    windows {
        type = ImageType.MSI
        icon = project.file("src/main/resources/images/RedMars.ico").absolutePath
        winUpgradeUuid = "920a4cfb-f2a9-41eb-81d6-fc8ef228331c"
        winDirChooser = true
        winMenu = true
        winShortcut = true
    }
}
