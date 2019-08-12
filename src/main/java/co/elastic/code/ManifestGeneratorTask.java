package co.elastic.code;

import co.elastic.code.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ExternalDependency;
import org.gradle.api.artifacts.ResolvedDependency;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author poytr1
 */
public class ManifestGeneratorTask extends DefaultTask {

    private HashSet<ResolvedDependency> chidrenVisited = new HashSet<>();

    @TaskAction
    void generateConfigFile() {
        ArrayList<ProjectInfo> projectInfos = new ArrayList<>();
        getProject().allprojects(project -> {
            ProjectInfo thisProjectInfo = new ProjectInfo();
            thisProjectInfo.setName(project.getName());
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
                    if (((IvyArtifactRepository) r).getUrl() != null) {
                        url = ((IvyArtifactRepository) r).getUrl().toString();
                    }
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
                conf.getAllDependencies().withType(ExternalDependency.class).stream().map(dep -> new Dependency(dep.getGroup(), dep.getName(), dep.getVersion())).forEach(deps::add);
                if (Arrays.stream(conf.getClass().getDeclaredMethods()).anyMatch(m -> m.getName().equals("isCanBeResolved")) && conf.isCanBeResolved()) {
                    conf.getResolvedConfiguration().getFirstLevelModuleDependencies().forEach(d -> {
                        deps.add(new Dependency(d.getModuleGroup(), d.getModuleName(), d.getModuleVersion()));
                        chidrenVisited.add(d);
                        d.getChildren().forEach(c -> handleTransitive(c, deps));
                    });
                    if (shouldIncludeLocalArtifacts(project)) {
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
            }
            HashMap<Dependency, Dependency> dependencyDependencyHashMap = new HashMap<>(10000);
            deps.forEach(dependency -> {
                if (!dependencyDependencyHashMap.containsKey(dependency)) {
                    dependencyDependencyHashMap.put(dependency, dependency);
                } else if (dependency.compareTo(dependencyDependencyHashMap.get(dependency)) > 0) {
                    dependencyDependencyHashMap.put(dependency, dependency);
                }
            });
            thisProjectInfo.setDependencies(new ArrayList<>(dependencyDependencyHashMap.values()));
            // set sourceSet-related infos
            try {
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
            } catch (Exception e) {
                thisProjectInfo.setDefaultSrcDirs();
                thisProjectInfo.setDefaultTestSrcDirs();
            }
            projectInfos.add(thisProjectInfo);
        });
        // serialize the config to manifest file
        Config cfg = new Config(projectInfos);
        try {
            String basePath = getProject().getPath().substring(1).replaceAll(":", "/");
            Writer writer = new FileWriter("".equals(basePath) ? "manifest.json" : basePath + "/manifest.json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(cfg));
            writer.close();
        } catch (IOException e) {
            getLogger().error("Write serialized config failed", e);
        }
    }

    private void handleTransitive(ResolvedDependency transitive, ArrayList<Dependency> deps) {
        deps.add(new Dependency(transitive.getModuleGroup(), transitive.getModuleName(), transitive.getModuleVersion()));
        if (!chidrenVisited.contains(transitive)) {
            chidrenVisited.add(transitive);
            transitive.getChildren().forEach(c -> handleTransitive(c, deps));
        }
    }

    private boolean shouldIncludeLocalArtifacts(Project project) {
        if (project.hasProperty("manifest.resolveLocal")) {
            Object prop = project.property("manifest.resolveLocal");
            return prop instanceof String ? Boolean.parseBoolean((String) prop) : (Boolean) prop;
        } else {
            return false;
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
