package co.elastic.code.model;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Main config for a repo
 */
public class Config {
    private List<ProjectInfo> projectInfos;

    private String rootName;

    public Config(List<ProjectInfo> projectInfos, String rootName) {
        this.setProjectInfos(projectInfos);
        this.setRootName(rootName);
    }

    public void setProjectInfos(List<ProjectInfo> projectInfos){
        this.projectInfos = projectInfos;
    }

    public List<ProjectInfo> getProjectInfos() {
        return this.projectInfos;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public String getRootName() {
        return rootName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("config", projectInfos)
                .append("rootName", rootName)
                .toString();
    }
}