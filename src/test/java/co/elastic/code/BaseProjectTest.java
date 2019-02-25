package co.elastic.code;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    private File manifestDir = new File(this.projectDir, "manifest.json");

    private void cleanUp() {
        if (manifestDir.exists()) {
            assertTrue(manifestDir.delete());
        }
    }

    // this method should be override by subclass
    protected String getProjectName() {
        return "";
    }

    // this method should be override by subclass
    protected String getExpectedConfig() {
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
    public void testProjectConfig() throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(this.manifestDir));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Object json = gson.fromJson(bufferedReader, Object.class);
        assertEquals(this.getExpectedConfig(), gson.toJson(json));
        cleanUp();
    }

}
