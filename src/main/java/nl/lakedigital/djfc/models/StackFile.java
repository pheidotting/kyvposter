package nl.lakedigital.djfc.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

        for (String tag : tags) {
            stringBuilder.append("#");
            stringBuilder.append(tag);
            stringBuilder.append(" ");
        }

        return stringBuilder.toString().trim();
    }
}
