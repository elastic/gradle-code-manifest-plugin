package co.elastic.code;

public class MultiProjectTest extends  BaseProjectTest {

    @Override
    protected String getProjectName() {
        return "multiProject";
    }

    @Override
    protected String getExpectedConfig() {
        return "{\n" +
                "  \"projectInfos\": [\n" +
                "    {\n" +
                "      \"android\": false,\n" +
                "      \"repos\": [\n" +
                "        {\n" +
                "          \"repoType\": \"MAVEN\",\n" +
                "          \"url\": \"https://repo.maven.apache.org/maven2/\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"dependencies\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"android\": false,\n" +
                "      \"repos\": [\n" +
                "        {\n" +
                "          \"repoType\": \"MAVEN\",\n" +
                "          \"url\": \"https://repo.maven.apache.org/maven2/\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"dependencies\": [\n" +
                "        {\n" +
                "          \"groupId\": \"org.apache.logging.log4j\",\n" +
                "          \"artifactId\": \"log4j-api\",\n" +
                "          \"version\": \"2.9.1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"groupId\": \"org.apache.logging.log4j\",\n" +
                "          \"artifactId\": \"log4j-core\",\n" +
                "          \"version\": \"2.9.1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"groupId\": \"org.apache.logging.log4j\",\n" +
                "          \"artifactId\": \"log4j-slf4j-impl\",\n" +
                "          \"version\": \"2.9.1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"groupId\": \"com.google.guava\",\n" +
                "          \"artifactId\": \"guava\",\n" +
                "          \"version\": \"23.0\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"groupId\": \"org.mockito\",\n" +
                "          \"artifactId\": \"mockito-core\",\n" +
                "          \"version\": \"2.11.0\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"groupId\": \"junit\",\n" +
                "          \"artifactId\": \"junit\",\n" +
                "          \"version\": \"4.12\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"srcDirs\": [\n" +
                "        \"src/main/java\"\n" +
                "      ],\n" +
                "      \"testSrcDirs\": [\n" +
                "        \"src/test/java\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"android\": false,\n" +
                "      \"repos\": [\n" +
                "        {\n" +
                "          \"repoType\": \"MAVEN\",\n" +
                "          \"url\": \"https://repo.maven.apache.org/maven2/\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"dependencies\": [\n" +
                "        {\n" +
                "          \"groupId\": \"org.apache.logging.log4j\",\n" +
                "          \"artifactId\": \"log4j-api\",\n" +
                "          \"version\": \"2.9.1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"groupId\": \"org.apache.logging.log4j\",\n" +
                "          \"artifactId\": \"log4j-core\",\n" +
                "          \"version\": \"2.9.1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"groupId\": \"org.apache.logging.log4j\",\n" +
                "          \"artifactId\": \"log4j-slf4j-impl\",\n" +
                "          \"version\": \"2.9.1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"groupId\": \"com.google.guava\",\n" +
                "          \"artifactId\": \"guava\",\n" +
                "          \"version\": \"23.0\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"groupId\": \"org.mockito\",\n" +
                "          \"artifactId\": \"mockito-core\",\n" +
                "          \"version\": \"2.11.0\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"groupId\": \"junit\",\n" +
                "          \"artifactId\": \"junit\",\n" +
                "          \"version\": \"4.12\"\n" +
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
