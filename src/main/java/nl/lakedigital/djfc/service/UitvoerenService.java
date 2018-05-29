package nl.lakedigital.djfc.service;

import nl.lakedigital.djfc.domain.IngeplandePost;
import nl.lakedigital.djfc.models.GeplandePost;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

@Service
public class UitvoerenService {
    @Inject
    private List<SocialMediaService> socialMediaServices;

    @Inject
    private IngeplandePostService ingeplandePostService;
    @Inject
    private PostInplanService postInplanService;

    public void voeruit(GeplandePost geplandePost) throws IOException {
        IngeplandePost ingeplandePost = ingeplandePostService.laatstVerstuurdePost();
        PostInplanService.Dag dag = PostInplanService.Dag.getFromDayOfWeek(LocalDate.now().getDayOfWeek());
        int aantalMinutenTussenPosts = postInplanService.bepaalRuimteTussenPosts(dag.getStartTijd(), dag.getEindTijd(), dag.getMaximumAantalPosts());

        if (ingeplandePost.getTijdstipUitgevoerd().plusMinutes(aantalMinutenTussenPosts).isAfter(LocalDateTime.now())) {
            ingeplandePostService.stelUit(ingeplandePost, aantalMinutenTussenPosts);
        } else {
            SocialMediaService socialMediaService = socialMediaServices.stream().filter(new Predicate<SocialMediaService>() {
                @Override
                public boolean test(SocialMediaService socialMediaService) {
                    return socialMediaService.voorMij(geplandePost.getMedia());
                }
            }).findFirst().get();

            socialMediaService.voeruit(geplandePost);
        }
    }
}
