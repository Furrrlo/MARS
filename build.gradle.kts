plugins {
    application
}

tasks.withType<Wrapper>().configureEach {
    gradleVersion = "7.2"
}

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

