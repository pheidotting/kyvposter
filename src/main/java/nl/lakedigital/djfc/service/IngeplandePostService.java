package nl.lakedigital.djfc.service;

import nl.lakedigital.djfc.domain.IngeplandePost;
import nl.lakedigital.djfc.repository.IngeplandePostRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IngeplandePostService {
    @Inject
    private IngeplandePostRepository ingepladePostRepository;

    public List<IngeplandePost> ingeplandePostsVoorDatum(LocalDate datum) {
        return ingepladePostRepository.ingeplandePostsVoorDatum(datum);
    }

    public List<IngeplandePost> ingeplandeOnverzondenPostsVoorDatum(LocalDate datum) {
        return ingepladePostRepository.ingeplandeOnverzondenPostsVoorDatum(datum);
    }

    public List<IngeplandePost> leesPostsMetZelfdeResource(IngeplandePost ingeplandePost) {
        return ingepladePostRepository.leesBijResource(ingeplandePost.getResource());
    }

    public IngeplandePost lees(Long id) {
        return ingepladePostRepository.lees(id);
    }

    public void stelUit(IngeplandePost ingeplandePost, int aantalMinuten) {
        ingeplandePost.setTijdstipIngepland(LocalDateTime.now().plusMinutes(aantalMinuten));
        opslaan(ingeplandePost);
    }

    public List<IngeplandePost> overgeblevenPosts() {
        return ingepladePostRepository.overgeblevenPosts();
    }

    public void markeerAlsVerzonden(IngeplandePost ingeplandePost) {
        ingeplandePost.setTijdstipUitgevoerd(LocalDateTime.now());
        ingepladePostRepository.opslaan(ingeplandePost);
    }

    public IngeplandePost laatstVerstuurdePost() {
        return ingepladePostRepository.laatstVerstuurdePost();
    }

    public void opslaan(List<IngeplandePost> ingeplandePosts) {
        ingepladePostRepository.opslaan(ingeplandePosts);
    }

    public void opslaan(IngeplandePost ingeplandePost) {
        ingepladePostRepository.opslaan(ingeplandePost);
    }

    public void opruimen() {
        ingepladePostRepository.opruimen();
    }

    public void pakOp(IngeplandePost ingeplandePost) {
        ingeplandePost.setOpgepakt(true);
        ingepladePostRepository.opslaan(ingeplandePost);
    }

    public boolean isOpgepakt(IngeplandePost ingeplandePost) {
        return ingepladePostRepository.lees(ingeplandePost.getId()).isOpgepakt();
    }
}
