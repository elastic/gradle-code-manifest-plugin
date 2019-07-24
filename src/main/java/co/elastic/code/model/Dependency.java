package co.elastic.code.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.gradle.util.VersionNumber;

/**
 * @author poytr1
 */
public class Dependency implements Comparable<Dependency> {
    private String groupId;

    private String artifactId;

    private String version;

    private String path;

    public Dependency(String groupId, String artifactId, String version) {
        this.setGroupId(groupId);
        this.setArtifactId(artifactId);
        this.setVersion(version);
    }

    public Dependency(String path) {
        this.setPath(path);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public int compareTo(Dependency other) {
        if (this.equals(other)) {
            return VersionNumber.parse(version).compareTo(VersionNumber.parse(other.version));
        }
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Dependency) {
            String otherGroupId = ((Dependency) obj).getGroupId();
            String otherArtifactId= ((Dependency) obj).getArtifactId();
            String otherPath = ((Dependency) obj).getPath();
            if (otherGroupId != null && otherArtifactId != null) {
                return (otherGroupId.equals(groupId)) && (otherArtifactId.equals(artifactId));
            } else if (otherPath != null) {
                return otherPath.equals(path);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(groupId)
                .append(artifactId)
                .append(path)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("groupId", groupId)
                .append("artifactId", artifactId)
                .append("version", version)
                .append("path", path)
                .toString();
    }


}
