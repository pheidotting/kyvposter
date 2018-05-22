package nl.lakedigital.djfc.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import nl.lakedigital.djfc.domain.IngeplandePost;
import nl.lakedigital.djfc.repository.IngeplandePostRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class IngeplandePostService {
    @Inject
    private IngeplandePostRepository ingepladePostRepository;

    public List<IngeplandePost> ingeplandePostsVoorDatum(LocalDate datum){
        return ingepladePostRepository.ingeplandePostsVoorDatum(datum);
    }
    public List<IngeplandePost> ingeplandeOnverzondenPostsVoorDatum(LocalDate datum){
        return ingepladePostRepository.ingeplandeOnverzondenPostsVoorDatum(datum);
    }

    public List<IngeplandePost> leesPostsMetZelfdeResource(IngeplandePost ingeplandePost){
        return ingepladePostRepository.leesBijResource(ingeplandePost.getResource());
    }
public IngeplandePost lees(Long id){
   return     ingepladePostRepository.lees(id);
}
    public void markeerAlsVerzonden(IngeplandePost ingeplandePost){
        ingeplandePost.setTijdstipUitgevoerd(LocalDateTime.now());
        ingepladePostRepository.opslaan(ingeplandePost);
    }

    public void opslaan(List<IngeplandePost> ingeplandePosts){
        ingepladePostRepository.opslaan(ingeplandePosts);
    }
    public void opslaan(IngeplandePost ingeplandePost){
        ingepladePostRepository.opslaan(ingeplandePost);
    }
}
