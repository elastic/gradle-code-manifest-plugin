package co.elastic.code.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author poytr1
 */
public class Dependency {
    private String groupId;

    private String artifactId;

    private String version;

    public Dependency(String groupId, String artifactId, String version) {
        this.setgroupId(groupId);
        this.setArtifactId(artifactId);
        this.setVersion(version);
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setgroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getgroupId() {
        return groupId;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(groupId)
                .append(artifactId)
                .append(version)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("groupId", groupId)
                .append("artifactId", artifactId)
                .append("version", version)
                .toString();
    }


}
