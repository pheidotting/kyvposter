package nl.lakedigital.djfc.domain;

import nl.lakedigital.djfc.models.GeplandePost;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="POSTS")
@NamedQueries({
        @NamedQuery(name="IngeplandePost.ingeplandePostsVoorDatum",query = "select p from IngeplandePost p where p.tijdstipIngepland > :startTijdstip"),// and p.tijdstipUitgevoerd < :eindTijdstip"),//
        @NamedQuery(name="IngeplandePost.ingeplandeOnverzondenPostsVoorDatum",query = "select p from IngeplandePost p where p.tijdstipUitgevoerd is null and p.tijdstipIngepland > :startTijdstip"),// and p.tijdstipIngepland < :eindTijdstip")
        @NamedQuery(name = "IngeplandePost.overgeblevenPosts", query = "select p from IngeplandePost p where p.tijdstipUitgevoerd is null"),// and p.tijdstipIngepland < :eindTijdstip")
        @NamedQuery(name = "IngeplandePost.leesBijResource", query = "select p from IngeplandePost p where p.resource = :resource"),//
        @NamedQuery(name = "IngeplandePost.laatstVerstuurdePost", query = "select p from IngeplandePost p order by p.tijdstipUitgevoerd desc limit 1")
})
public class IngeplandePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "INGEPLAND")
    private LocalDateTime tijdstipIngepland;
    @Column(name = "UITGEVOERD")
    private LocalDateTime tijdstipUitgevoerd;
    @Column(name = "RESOURCE")
    private String resource;
    @Column(name = "MEDIA")
    private GeplandePost.Media media;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTijdstipIngepland() {
        return tijdstipIngepland;
    }

    public void setTijdstipIngepland(LocalDateTime tijdstipIngepland) {
        this.tijdstipIngepland = tijdstipIngepland;
    }

    public LocalDateTime getTijdstipUitgevoerd() {
        return tijdstipUitgevoerd;
    }

    public void setTijdstipUitgevoerd(LocalDateTime tijdstipUitgevoerd) {
        this.tijdstipUitgevoerd = tijdstipUitgevoerd;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public GeplandePost.Media getMedia() {
        return media;
    }

    public void setMedia(GeplandePost.Media media) {
        this.media = media;
    }
}
