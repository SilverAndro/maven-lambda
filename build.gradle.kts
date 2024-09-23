plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.2"
}

group = "dev.silverandro"
version = "1.0.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.sleeping.town")
        mavenContent {
            includeGroup("com.unascribed")
        }
    }
}

dependencies {
    implementation("com.amazonaws:aws-lambda-java-core:1.2.3")
    implementation("com.amazonaws:aws-lambda-java-events:3.14.0")
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.772")
    implementation("com.unascribed:flexver-java:1.1.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}