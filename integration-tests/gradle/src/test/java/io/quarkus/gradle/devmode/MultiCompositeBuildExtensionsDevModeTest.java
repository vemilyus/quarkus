package io.quarkus.gradle.devmode;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.google.common.collect.ImmutableMap;

public class MultiCompositeBuildExtensionsDevModeTest extends QuarkusDevGradleTestBase {
    @Override
    protected String projectDirectoryName() {
        return "multi-composite-build-extensions-project";
    }

    @Override
    protected String[] buildArguments() {
        return new String[] { ":application:clean", ":application:quarkusDev" };
    }

    protected void testDevMode() throws Exception {

        assertThat(getHttpResponse())
                .contains("ready")
                .contains("my-quarkus-project")
                .contains("org.acme.quarkus.sample")
                .contains("1.0-SNAPSHOT");

        assertThat(getHttpResponse("/hello")).contains("hello from LibB and LibA extension enabled: false");

        replace("libraries/libraryA/src/main/java/org/acme/liba/LibA.java",
                ImmutableMap.of("return \"LibA\";", "return \"modifiedA\";"));
        replace("libraries/libraryB/src/main/java/org/acme/libb/LibB.java",
                ImmutableMap.of("return \"LibB\";", "return \"modifiedB\";"));
        replace("application/src/main/resources/application.properties",
                ImmutableMap.of("false", "true"));

        assertThat(getHttpResponse("/hello")).contains("hello from LibB and LibA extension enabled: true");
    }

    @Override
    protected File getProjectDir() {
        File projectDir = super.getProjectDir();
        final File appProperties = new File(projectDir, "application/gradle.properties");
        final File libsProperties = new File(projectDir, "libraries/gradle.properties");
        final File extensionProperties = new File(projectDir, "extensions/example-extension/gradle.properties");
        final File anotherExtensionProperties = new File(projectDir, "extensions/another-example-extension/gradle.properties");
        final Path projectProperties = projectDir.toPath().resolve("gradle.properties");

        try {
            Files.copy(projectProperties, appProperties.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(projectProperties, libsProperties.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(projectProperties, extensionProperties.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(projectProperties, anotherExtensionProperties.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to copy gradle.properties file", e);
        }
        return projectDir;
    }

    /*
     * @Test
     * public void testBasicMultiModuleBuild() throws Exception {
     *
     * final File projectDir = getProjectDir("multi-composite-build-extensions-project");
     *
     * final File appProperties = new File(projectDir, "application/gradle.properties");
     * final File libsProperties = new File(projectDir, "libraries/gradle.properties");
     * final File extensionProperties = new File(projectDir, "extensions/example-extension/gradle.properties");
     * final File anotherExtensionProperties = new File(projectDir, "extensions/another-example-extension/gradle.properties");
     *
     * final Path projectProperties = projectDir.toPath().resolve("gradle.properties");
     *
     * try {
     * Files.copy(projectProperties, appProperties.toPath(), StandardCopyOption.REPLACE_EXISTING);
     * Files.copy(projectProperties, libsProperties.toPath(), StandardCopyOption.REPLACE_EXISTING);
     * Files.copy(projectProperties, extensionProperties.toPath(), StandardCopyOption.REPLACE_EXISTING);
     * Files.copy(projectProperties, anotherExtensionProperties.toPath(), StandardCopyOption.REPLACE_EXISTING);
     * } catch (IOException e) {
     * throw new IllegalStateException("Unable to copy gradle.properties file", e);
     * }
     *
     * runGradleWrapper(projectDir, ":application:quarkusBuild");
     *
     * final Path extension = projectDir.toPath().resolve("extensions").resolve("example-extension").resolve("runtime")
     * .resolve("build")
     * .resolve("libs");
     * assertThat(extension).exists();
     * assertThat(extension.resolve("example-extension-1.0-SNAPSHOT.jar")).exists();
     *
     * final Path anotherExtension =
     * projectDir.toPath().resolve("extensions").resolve("another-example-extension").resolve("runtime")
     * .resolve("build");
     *
     * assertThat(anotherExtension).exists();
     * assertThat(anotherExtension.resolve("resources/main/META-INF/quarkus-extension.yaml")).exists();
     *
     * final Path libA = projectDir.toPath().resolve("libraries").resolve("libraryA").resolve("build").resolve("libs");
     * assertThat(libA).exists();
     * assertThat(libA.resolve("libraryA-1.0-SNAPSHOT.jar")).exists();
     *
     * final Path libB = projectDir.toPath().resolve("libraries").resolve("libraryB").resolve("build").resolve("libs");
     * assertThat(libB).exists();
     * assertThat(libB.resolve("libraryB-1.0-SNAPSHOT.jar")).exists();
     *
     * final Path applicationLib = projectDir.toPath().resolve("application").resolve("build").resolve("quarkus-app");
     * assertThat(applicationLib.resolve("lib").resolve("main").resolve("org.acme.libs.libraryA-1.0-SNAPSHOT.jar")).exists();
     * assertThat(applicationLib.resolve("lib").resolve("main").resolve("org.acme.libs.libraryB-1.0-SNAPSHOT.jar")).exists();
     * assertThat(applicationLib.resolve("lib").resolve("main")
     * .resolve("org.acme.extensions.example-extension-1.0-SNAPSHOT.jar")).exists();
     * assertThat(applicationLib.resolve("lib").resolve("main")
     * .resolve("org.acme.extensions.another-example-extension-1.0-SNAPSHOT.jar")).exists();
     *
     * assertThat(applicationLib.resolve("app").resolve("application-1.0-SNAPSHOT.jar")).exists();
     * }
     */
}
