package co.elastic.code;

import junit.framework.TestCase;
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.*;

import java.io.*;
import java.util.Arrays;

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;

/**
 * @author poytr1
 */
@Ignore
public class BaseProjectTest extends TestCase {

    private File baseProjectDir = new File(System.getProperty("user.dir") + "/testProjects");
    private File projectDir = new File(this.baseProjectDir, getProjectName());
    private File manifestFile = new File(this.projectDir, "manifest.json");
    private File expectedManifestFile = new File(this.projectDir, "expected_manifest.json");

    private void cleanUp() {
        if (manifestFile.exists()) {
            assertTrue(manifestFile.delete());
        }
    }

    // this method should be override by subclass
    protected String getProjectName() {
        return "";
    }

    @Test
    public void testGenManifest() throws IOException {
        cleanUp();
        BuildResult result =  GradleRunner.create()
                .withProjectDir(this.projectDir)
                .withPluginClasspath()
                .withArguments(Arrays.asList("-Pmanifest.resolve=true", "genManifest"))
                .build();
        assertEquals(SUCCESS, result.task(":genManifest").getOutcome());
        FileUtils.moveFileToDirectory(
                FileUtils.getFile(System.getProperty("user.dir") + "/manifest.json"),
                FileUtils.getFile(this.projectDir), false);
    }

    @Test
    public void testProjectConfig() throws IOException {
        if (!expectedManifestFile.exists()) {
            System.out.println("generate a new expected config file");
            FileUtils.moveFile(this.manifestFile, this.expectedManifestFile);
        } else {
            junitx.framework.FileAssert.assertEquals(this.expectedManifestFile, this.manifestFile);
        }
        cleanUp();
    }

}
