plugins {
    java
    maven
}

group = "io.github.protonmc"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.puppycrawl.tools", "checkstyle", "8.37")
}
