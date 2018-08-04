package nl.lakedigital.djfc.service;

import nl.lakedigital.djfc.domain.IngeplandePost;
import nl.lakedigital.djfc.models.GeplandePost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

@Service
public class UitvoerenService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UitvoerenService.class);

    @Inject
    private List<SocialMediaService> socialMediaServices;

    @Inject
    private IngeplandePostService ingeplandePostService;
    @Inject
    private PostInplanService postInplanService;

    public void voeruit(GeplandePost geplandePost, int postsVandaag) throws IOException {
        IngeplandePost laatsUitgevoerdePost = ingeplandePostService.laatstVerstuurdePost();
        PostInplanService.Dag dag = PostInplanService.Dag.getFromDayOfWeek(LocalDate.now().getDayOfWeek());
        int aantalMinutenTussenPosts = postInplanService.bepaalRuimteTussenPosts(dag.getStartTijd(), dag.getEindTijd(), postsVandaag);

        LOGGER.info("Vorige post is uitgevoerd op {}", laatsUitgevoerdePost.getTijdstipUitgevoerd());
        if (laatsUitgevoerdePost != null && laatsUitgevoerdePost.getTijdstipUitgevoerd() != null && laatsUitgevoerdePost.getTijdstipUitgevoerd().plusMinutes(aantalMinutenTussenPosts).isAfter(LocalDateTime.now())) {
            IngeplandePost ingeplandePost = ingeplandePostService.lees(geplandePost.getId());
            LOGGER.info("Uitstellen met {} minuten", aantalMinutenTussenPosts);
            ingeplandePostService.stelUit(ingeplandePost, aantalMinutenTussenPosts);
        } else {
            SocialMediaService socialMediaService = socialMediaServices.stream().filter(new Predicate<SocialMediaService>() {
                @Override
                public boolean test(SocialMediaService socialMediaService) {
                    return socialMediaService.voorMij(geplandePost.getMedia());
                }
            }).findFirst().get();
            try {
                socialMediaService.voeruit(geplandePost);

                IngeplandePost ingeplandePost = ingeplandePostService.lees(geplandePost.getId());
                ingeplandePostService.markeerAlsVerzonden(ingeplandePost);
            } catch (Exception e) {
                LOGGER.error("Fout bij plaatsen foto : {}", e.getMessage());
            }
        }
    }
}
