package co.elastic.code;

public class SingleProjectTest extends BaseProjectTest {

    @Override
    protected String getProjectName() {
        return "singleProject";
    }

    @Override
    protected String getExpectedConfig() {
        return "{\n" +
                "  \"projectInfos\": [\n" +
                "    {\n" +
                "      \"android\": false,\n" +
                "      \"path\": \":\",\n" +
                "      \"repos\": [\n" +
                "        {\n" +
                "          \"repoType\": \"MAVEN\",\n" +
                "          \"url\": \"https://repo.maven.apache.org/maven2/\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"dependencies\": [\n" +
                "        {\n" +
                "          \"groupId\": \"com.google.guava\",\n" +
                "          \"artifactId\": \"guava\",\n" +
                "          \"version\": \"18.0\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"srcDirs\": [\n" +
                "        \"src/main/java\"\n" +
                "      ],\n" +
                "      \"testSrcDirs\": [\n" +
                "        \"src/test/java\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }
}
