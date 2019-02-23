package co.elastic.code;

public class AndroidProjectTest extends BaseProjectTest {

    @Override
    protected String getProjectName() {
        return "androidProject";
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
                "          \"url\": \"https://jcenter.bintray.com/\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"dependencies\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"android\": true,\n" +
                "      \"androidSdkVersion\": \"android-25\",\n" +
                "      \"repos\": [\n" +
                "        {\n" +
                "          \"repoType\": \"MAVEN\",\n" +
                "          \"url\": \"https://jcenter.bintray.com/\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"dependencies\": [\n" +
                "        {\n" +
                "          \"groupId\": \"com.android.support.test.espresso\",\n" +
                "          \"artifactId\": \"espresso-core\",\n" +
                "          \"version\": \"2.2.2\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"groupId\": \"com.android.support\",\n" +
                "          \"artifactId\": \"appcompat-v7\",\n" +
                "          \"version\": \"25.1.1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"groupId\": \"com.android.support.constraint\",\n" +
                "          \"artifactId\": \"constraint-layout\",\n" +
                "          \"version\": \"1.0.1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"path\": \"libs/u2fRequestHandler-v1-1.22.0-SNAPSHOT.jar\"\n" +
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
