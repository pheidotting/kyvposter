package nl.lakedigital.djfc.service;

import com.google.common.util.concurrent.RateLimiter;
import nl.lakedigital.djfc.domain.IngeplandePost;
import nl.lakedigital.djfc.models.GeplandePost;
import nl.lakedigital.djfc.models.StackFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostInplannenEnUitvoerenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostInplannenEnUitvoerenService.class);

    @Inject
    private IngeplandePostService ingeplandePostService;
    @Inject
    private PostInplanService postInplanService;
    @Inject
    private UitvoerenService uitvoerenService;
    @Inject
    private TagService tagService;
    @Inject
    private StackStorageService stackStorageService;

    private RateLimiter rateLimiter = RateLimiter.create(1);

    public void planEnVoerUit() {
        List<IngeplandePost> alleIngeplandePostsVandaag = ingeplandePostService.ingeplandePostsVoorDatum(LocalDate.now());
        List<IngeplandePost> alleIngeplandeOnverzondenPostsVandaag = ingeplandePostService.ingeplandeOnverzondenPostsVoorDatum(LocalDate.now());
        List<IngeplandePost> overgeblevenPosts = ingeplandePostService.overgeblevenPosts();

        if (alleIngeplandePostsVandaag.isEmpty()) {
            PostInplanService.Dag dag = PostInplanService.Dag.getFromDayOfWeek();
            overgeblevenPosts.stream().forEach(ingeplandePost -> ingeplandePost.setTijdstipIngepland(LocalDateTime.of(LocalDate.now(), dag.getStartTijd().plusMinutes(10))));
            ingeplandePostService.opslaan(overgeblevenPosts);

            List<IngeplandePost> nieuweLijst = postInplanService.planPosts(LocalDate.now()).stream().map(geplandePost -> {
                LOGGER.info("{} - {}", geplandePost.getMedia(), geplandePost.getTijdstip());
                IngeplandePost ingeplandePost = new IngeplandePost();

                ingeplandePost.setResource(geplandePost.getStackFile().getUrl());
                ingeplandePost.setTijdstipIngepland(geplandePost.getTijdstip());
                ingeplandePost.setMedia(geplandePost.getMedia());

                return ingeplandePost;
            }).collect(Collectors.toList());

            ingeplandePostService.opslaan(nieuweLijst);
            alleIngeplandeOnverzondenPostsVandaag = nieuweLijst;
        }


        alleIngeplandeOnverzondenPostsVandaag.stream().filter(ingeplandePost -> ingeplandePost.getTijdstipIngepland().isBefore(LocalDateTime.now())).forEach(ingeplandePost -> {
            rateLimiter.acquire();

            StackFile stackFile = new StackFile(tagService.genereerTags(ingeplandePost.getResource(), stackStorageService.getWEBDAV_PATH()), ingeplandePost.getResource());

            GeplandePost geplandePost = new GeplandePost(ingeplandePost.getId(), ingeplandePost.getMedia(), ingeplandePost.getTijdstipIngepland(), stackFile);

            LOGGER.info("Uitvoeren post met id {}, media is {}", ingeplandePost.getId(), ingeplandePost.getMedia());
            try {
                uitvoerenService.voeruit(geplandePost);
            } catch (IOException e) {
                e.printStackTrace();
            }

            stackStorageService.opruimen(ingeplandePost);
            ingeplandePostService.opruimen();
        });
    }
}
