package nl.lakedigital.djfc.models;

import java.util.Collections;
import java.util.List;

public class StackFile {
    private List<String> tags;
    private String url;

    public StackFile(List<String> tags, String url) {
        this.tags = tags;
        this.url = url;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getUrl() {
        return url;
    }

    public String tagsToString() {
        StringBuilder stringBuilder = new StringBuilder();

        Collections.shuffle(tags);

        for (String tag : tags) {
            stringBuilder.append("#");
            stringBuilder.append(tag);
            stringBuilder.append(" ");
        }

        String tags = stringBuilder.toString().trim().replace("#null", "").replace("  ", " ");

        return tags;
    }
}
