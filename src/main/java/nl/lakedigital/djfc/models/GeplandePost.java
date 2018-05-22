package nl.lakedigital.djfc.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class GeplandePost {
    public enum Media {
        FACEBOOK, INSTAGRAM;
    }

    private Media media;
    private LocalDateTime tijdstip;
    private StackFile stackFile;

    public GeplandePost(Media media, LocalDateTime tijdstip, StackFile stackFile) {
        this.media = media;
        this.tijdstip = tijdstip;
        this.stackFile = stackFile;
    }

    public Media getMedia() {
        return media;
    }

    public LocalDateTime getTijdstip() {
        return tijdstip;
    }

    public StackFile getStackFile() {
        return stackFile;
    }

    public void setStackFile(StackFile stackFile) {
        this.stackFile = stackFile;
    }
}
