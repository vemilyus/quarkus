package io.quarkus.extension.gradle.kapt;

import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;
import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin;

public class KaptUtils {
    public static void addAnnotationProcessorToKapt(Project project, String annotationProcessorDep) {
        if (!isKaptAvailable(project)) {
            return;
        }

        project.getConfigurations()
                .getByName(Kapt3GradleSubplugin.Companion.getKaptConfigurationName(SourceSet.MAIN_SOURCE_SET_NAME))
                .withDependencies(dependencies -> {
                    dependencies.add(project.getDependencies().create(annotationProcessorDep));
                });
    }

    private static boolean isKaptAvailable(Project project) {
        return project.getPlugins().hasPlugin("org.jetbrains.kotlin.kapt");
    }
}
