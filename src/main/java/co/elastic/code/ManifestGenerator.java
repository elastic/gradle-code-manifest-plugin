package co.elastic.code;

import co.elastic.code.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.artifacts.repositories.FlatDirectoryArtifactRepository;
import org.gradle.api.artifacts.repositories.IvyArtifactRepository;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author poytr1
 */
public class ManifestGenerator extends DefaultTask {

    @TaskAction
    void generateConfigFile() {
        ArrayList<ProjectInfo> projectInfos = new ArrayList<>();
        getProject().allprojects(project -> {
            ProjectInfo thisProjectInfo = new ProjectInfo();
            if (isAndroidApp(project)) {
                thisProjectInfo.setAndroid(true);
                if (project.getTasksByName("android", false) != null) {
                    try {
                        String androidVersion = (String) MethodUtils.invokeMethod(project.findProperty("android"), true, "getCompileSdkVersion");
                        if (androidVersion != null) {
                            thisProjectInfo.setAndroidSdkVersion(androidVersion);
                        }
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            ArrayList<Repo> repos = new ArrayList<>();
            for (ArtifactRepository r : project.getRepositories()) {
                Repo repo = new Repo();
                String url = "";
                Credentials creds;
                if (r instanceof MavenArtifactRepository) {
                    repo.setRepoType(Repo.RepoTypes.MAVEN);
                    url = ((MavenArtifactRepository) r).getUrl().toString();

                } else if (r instanceof IvyArtifactRepository) {
                    repo.setRepoType(Repo.RepoTypes.IVY);
                    url = ((IvyArtifactRepository) r).getUrl().toString();

                } else if (r instanceof FlatDirectoryArtifactRepository) {
                    repo.setRepoType(Repo.RepoTypes.LOCAL);
                    url = ((FlatDirectoryArtifactRepository) r).getDirs().toString();
                } else {
                    getLogger().error("Unrecognized repo type of " + r.getName());
                }
                repo.setUrl(url);
                repos.add(repo);
            }
            thisProjectInfo.setRepos(repos.stream().distinct().collect(Collectors.toList()));
            ArrayList<Dependency> deps = new ArrayList<>();
            for (Configuration conf : project.getConfigurations()) {
                conf.getAllDependencies().forEach(dep -> {
                    deps.add(new Dependency(dep.getGroup(), dep.getName(), dep.getVersion()));
                });
            }
            thisProjectInfo.setDependencies(deps.stream().distinct().collect(Collectors.toList()));
            SourceSetContainer sourceSetContainer = (SourceSetContainer)project.getProperties().get("sourceSets");
            if (sourceSetContainer != null) {
                File projectDir = project.getProjectDir();
                try {
                    Set<File> srcDirs = sourceSetContainer.getByName("main").getJava().getSrcDirs();
                    thisProjectInfo.setSrcDirs(srcDirs.stream().map(srcDir -> projectDir.toPath().relativize(srcDir.toPath()).toString()).collect(Collectors.toList()));
                } catch (Exception e) {
                    getLogger().error("Get src dirs error", e);
                }
                try {
                    Set<File> testSrcDirs = sourceSetContainer.getByName("test").getJava().getSrcDirs();
                    thisProjectInfo.setTestSrcDirs(testSrcDirs.stream().map(srcDir -> projectDir.toPath().relativize(srcDir.toPath()).toString()).collect(Collectors.toList()));
                } catch (Exception e) {
                    getLogger().error("Get test src dirs error", e);
                }
            }
            projectInfos.add(thisProjectInfo);
        });
        Config cfg = new Config(projectInfos);
        try {
            Writer writer = new FileWriter("manifest.json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(cfg));
            writer.close();
        } catch (IOException e) {
            getLogger().error("Write serialized config failed", e);
        }
    }

    private boolean isAndroidApp(Project project) {
        PluginContainer plugins = project.getPlugins();
        return plugins.hasPlugin("com.android.application")
                || plugins.hasPlugin("com.android.library")
                || plugins.hasPlugin("com.android.test")
                || plugins.hasPlugin(" com.android.feature");
    }

}
