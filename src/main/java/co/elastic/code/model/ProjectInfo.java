package co.elastic.code.model;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author poytr1
 */
public class ProjectInfo {
    /** Property Android */
    private Boolean android=false;

    /** Property androidSdkVersion */
    private String androidSdkVersion;

    private List<Repo> repos;

    private List<Dependency> dependencies;

    private List<String> srcDirs;

    private List<String> testSrcDirs;

    /**
     * Gets the isAndroid
     */
    public Boolean isAndroid() {
        return this.android;
    }

    /**
     * Sets the isAndroid
     */
    public void setAndroid(Boolean value) {
        this.android = value;
    }

    /**
     * Gets the androidSdkVersion
     */
    public String getAndroidSdkVersion() {
        return this.androidSdkVersion;
    }

    /**
     * Sets the androidSdkVersion
     */
    public void setAndroidSdkVersion(String value) {
        this.androidSdkVersion = value;
    }

    public void setRepos(List<Repo> repos) {
        this.repos = repos;
    }

    public List<Repo> getRepos() {
        return repos;
    }

    public void setDependencies(List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public void setDefaultSrcDirs() {
        this.setSrcDirs(Collections.singletonList("src/main/java"));
    }

    public void setDefaultTestSrcDirs() {
        this.setTestSrcDirs(Collections.singletonList("src/test/java"));
    }

    public void setSrcDirs(List<String> srcDirs) {
        this.srcDirs = srcDirs;
    }

    public List<String> getSrcDirs() {
        return srcDirs;
    }

    public void setTestSrcDirs(List<String> testSrcDirs) {
        this.testSrcDirs = testSrcDirs;
    }

    public List<String> getTestSrcDirs() {
        return testSrcDirs;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("android", android)
                .append("repos", repos)
                .append("deps", dependencies)
                .toString();
    }
}
