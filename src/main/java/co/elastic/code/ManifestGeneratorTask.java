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
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author poytr1
 */
public class ManifestGeneratorTask extends DefaultTask {

    @TaskAction
    void generateConfigFile() {
        ArrayList<ProjectInfo> projectInfos = new ArrayList<>();
        getProject().allprojects(project -> {
            ProjectInfo thisProjectInfo = new ProjectInfo();
            thisProjectInfo.setPath(project.getPath());
            // set Android-related infos
            if (isAndroidApp(project)) {
                thisProjectInfo.setAndroid(true);
                Object androidConfig = project.findProperty("android");
                if (androidConfig != null) {
                    try {
                        String androidVersion = (String) MethodUtils.invokeMethod(androidConfig, true, "getCompileSdkVersion");
                        if (androidVersion != null) {
                            thisProjectInfo.setAndroidSdkVersion(androidVersion);
                        }
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        getLogger().error("failed to get the Android version", e);
                    }
                }
            }
            // set repo-related infos
            ArrayList<Repo> repos = new ArrayList<>();
            for (ArtifactRepository r : project.getRepositories()) {
                Repo repo = new Repo();
                String url = "";
                // TODO(pcxu): set credentials for private repo
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
                if (!url.startsWith("file:/")) {
                    repo.setUrl(url);
                    repos.add(repo);
                }
            }
            thisProjectInfo.setRepos(repos.stream().distinct().collect(Collectors.toList()));
            // set dependency-related infos
            ArrayList<Dependency> deps = new ArrayList<>();
            for (Configuration conf : project.getConfigurations()) {
                conf.getAllDependencies().stream().filter(dep -> !"unspecified".equals(dep.getName())).map(dep -> new Dependency(dep.getGroup(), dep.getName(), dep.getVersion())).forEach(deps::add);
                // get all local dependencies inside this repo
                if (conf.isCanBeResolved()) {
                    Set<File> resolvedArtifactFiles = new HashSet<>();
                    conf.getResolvedConfiguration().getResolvedArtifacts().forEach(a -> resolvedArtifactFiles.add(a.getFile()));
                    Set<File> allResolvedDependencyFiles = conf.getResolvedConfiguration().getFiles();
                    allResolvedDependencyFiles.removeAll(resolvedArtifactFiles);
                    try {
                        allResolvedDependencyFiles.forEach(f -> deps.add(new Dependency(f.getAbsolutePath().substring(project.getBuildFile().getParent().length() + 1))));
                    } catch (StringIndexOutOfBoundsException e) {
                        // do nothing
                    }
                }
            }
            thisProjectInfo.setDependencies(deps.stream().distinct().collect(Collectors.toList()));
            // set sourceSet-related infos
            SourceSetContainer sourceSetContainer = (SourceSetContainer) project.getProperties().get("sourceSets");
            if (sourceSetContainer != null) {
                File projectDir = project.getProjectDir();
                try {
                    Set<File> srcDirs = sourceSetContainer.getByName("main").getJava().getSrcDirs();
                    thisProjectInfo.setSrcDirs(srcDirs.stream().map(srcDir -> projectDir.toPath().relativize(srcDir.toPath()).toString()).collect(Collectors.toList()));
                } catch (Exception e) {
                    thisProjectInfo.setDefaultSrcDirs();
                }
                try {
                    Set<File> testSrcDirs = sourceSetContainer.getByName("test").getJava().getSrcDirs();
                    thisProjectInfo.setTestSrcDirs(testSrcDirs.stream().map(srcDir -> projectDir.toPath().relativize(srcDir.toPath()).toString()).collect(Collectors.toList()));
                } catch (Exception e) {
                    thisProjectInfo.setDefaultTestSrcDirs();
                }
            }
            projectInfos.add(thisProjectInfo);
        });
        // serialize the config to manifest file
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
