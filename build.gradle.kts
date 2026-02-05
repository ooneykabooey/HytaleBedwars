plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven {
        setUrl("https://www.cursemaven.com")
    }
}

dependencies {
    compileOnly(files("libs/HytaleServer.jar"))
    testCompileOnly(files("libs/HytaleServer.jar"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("curse.maven:hyui-1431415:7548594") // 0.5.10
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")
}

tasks.shadowJar {
    exclude("libs/HytaleServer.jar")
}

tasks.test {
    useJUnitPlatform()
}

/*
tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from("src/main/resources")

    finalizedBy("copyPluginJar")
}
 */

/// Do unchecked.
tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:unchecked")
}