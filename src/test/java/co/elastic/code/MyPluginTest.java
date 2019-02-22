package co.elastic.code;

import junit.framework.TestCase;
import org.gradle.api.Project;

import org.gradle.testfixtures.ProjectBuilder;

/**
 * @author poytr1
 */
public class MyPluginTest extends TestCase {

     public void testGenManifest() {
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("gradle-manifest-plugin");

        assertNotNull(project.getTasksByName("genManifest", false));
    }

    public void testManifestGeneratorTask() {
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("gradle-manifest-plugin");

        assertTrue(project.getTasksByName("genManifest", false).iterator().next() instanceof ManifestGeneratorTask);
    }
}
