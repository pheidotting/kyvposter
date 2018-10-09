package nl.lakedigital.djfc.service;

import com.google.common.util.concurrent.RateLimiter;
import nl.lakedigital.djfc.domain.IngeplandePost;
import nl.lakedigital.djfc.models.GeplandePost;
import nl.lakedigital.djfc.models.StackFile;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

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
            overgeblevenPosts.stream().forEach(ingeplandePost -> {
                ingeplandePost.setTijdstipIngepland(LocalDateTime.of(LocalDate.now(), dag.getStartTijd().plusMinutes(10)));
                ingeplandePost.setOpgepakt(false);
            });
            ingeplandePostService.opslaan(overgeblevenPosts);

            List<IngeplandePost> nieuweLijst = postInplanService.planPosts(LocalDate.now(), overgeblevenPosts.size()).stream().map(geplandePost -> {
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
            if (!ingeplandePostService.isOpgepakt(ingeplandePost)) {
                ingeplandePostService.pakOp(ingeplandePost);

                LOGGER.trace("net voor rateLimiter.acquire();");
                rateLimiter.acquire();
                LOGGER.trace("net na rateLimiter.acquire();");

                StackFile stackFile = new StackFile(tagService.genereerTags(ingeplandePost.getResource(), stackStorageService.getWEBDAV_PATH()), ingeplandePost.getResource());

                GeplandePost geplandePost = new GeplandePost(ingeplandePost.getId(), ingeplandePost.getMedia(), ingeplandePost.getTijdstipIngepland(), stackFile);

                LOGGER.info("Uitvoeren post met id {}, media is {}", ingeplandePost.getId(), ingeplandePost.getMedia());

                PostInplanService.Dag vandaag = PostInplanService.Dag.getFromDayOfWeek();
                LocalDateTime nu = LocalDateTime.now();
                LocalDateTime start = LocalDateTime.of(LocalDate.now(), vandaag.getStartTijd());
                LocalDateTime eind = LocalDateTime.of(LocalDate.now(), vandaag.getEindTijd());
                if (nu.isAfter(start) && nu.isBefore(eind)) {
                    try {
                        uitvoerenService.voeruit(geplandePost, alleIngeplandePostsVandaag.size());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //                (new Thread(new OpruimenDavResourceService(ingeplandePost))).start();

                LOGGER.debug("Opruimen1");
                stackStorageService.opruimen(ingeplandePost);
                LOGGER.debug("Opruimen2");
                ingeplandePostService.opruimen();
                LOGGER.debug("Opruimen3");

                String pad = "/opt/jetty/webapps";
                File dir = new File(pad);
                File warFile = newArrayList(dir.listFiles()).stream().filter(new Predicate<File>() {
                    @Override
                    public boolean test(File file) {
                        LOGGER.debug("{}", file);
                        return file.getName().startsWith("kyv");
                    }
                }).findFirst().get();

                LOGGER.debug("Found : {}", warFile);
                File newFile = new File(pad + File.separator + "kyv" + System.currentTimeMillis() + ".war");
                try {
                    LOGGER.debug("Kopieer {} naar {}", warFile, newFile);
                    FileUtils.copyFile(warFile, newFile);
                    warFile.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
