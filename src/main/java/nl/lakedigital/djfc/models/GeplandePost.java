package nl.lakedigital.djfc.models;

import java.time.LocalDateTime;

public class GeplandePost {
    public enum Media {
        FACEBOOK, INSTAGRAM;
    }

    private Long id;
    private Media media;
    private LocalDateTime tijdstip;
    private StackFile stackFile;

    public GeplandePost(Long id, Media media, LocalDateTime tijdstip, StackFile stackFile) {
        this.id = id;
        this.media = media;
        this.tijdstip = tijdstip;
        this.stackFile = stackFile;
    }

    public Long getId() {
        return id;
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
