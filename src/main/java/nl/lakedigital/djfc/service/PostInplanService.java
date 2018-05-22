package nl.lakedigital.djfc.service;

import nl.lakedigital.djfc.models.GeplandePost;
import nl.lakedigital.djfc.models.StackFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.*;

@Service
public class PostInplanService {
    private final static Logger LOGGER =LoggerFactory.getLogger(PostInplanService.class);

    @Inject
    private StackStorageService stackStorageService;

    public enum Dag {
        Maandag(3, MONDAY, 17, 0, 22, 30), Dinsdag(3, TUESDAY, 17, 0, 22, 30), Woensdag(3, WEDNESDAY, 17, 0, 22, 30), Donderdag(3, THURSDAY, 17, 0, 22, 30), Vrijdag(3, FRIDAY, 17, 0, 22, 30), Zaterdag(4, SATURDAY, 15, 0, 22, 30), Zondag(5, SUNDAY, 12, 0, 22, 30);
        private DayOfWeek dayOfWeek;
        private int aantalPosts;
        private LocalTime startTijd;
        private LocalTime eindTijd;

        Dag(int aantalPosts, DayOfWeek dayOfWeek, int startTijdUur, int startTijdMinuut, int eindTijdUur, int eindTijdMinuut) {
            this.dayOfWeek = dayOfWeek;
            this.aantalPosts = aantalPosts;
            this.startTijd = LocalTime.of(startTijdUur, startTijdMinuut);
            this.eindTijd = LocalTime.of(eindTijdUur, eindTijdMinuut);
        }

        public DayOfWeek getDayOfWeek() {
            return dayOfWeek;
        }

        public int getAantalPosts() {
            return aantalPosts;
        }

        public LocalTime getStartTijd() {
            return startTijd;
        }

        public LocalTime getEindTijd() {
            return eindTijd;
        }

        public static Dag getFromDayOfWeek(DayOfWeek dayOfWeek) {
            for (Dag dag : Dag.values()) {
                if (dag.getDayOfWeek() == dayOfWeek) {
                    return dag;
                }
            }
            return null;
        }
    }

    public List<GeplandePost> planPosts(LocalDate datum) {
        final List<GeplandePost> result = new ArrayList<>();

        Dag dag = Dag.getFromDayOfWeek(datum.getDayOfWeek());

        LOGGER.info("Het is vandaag {}, dus we gaan {} posts inplannen per Social Media, dus {} in totaal", dag.toString(),dag.getAantalPosts(),(dag.getAantalPosts()*GeplandePost.Media.values().length));

        List<StackFile> stackFiles = null;
        try {
            stackFiles = stackStorageService.leesRandom(dag.getAantalPosts());
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOGGER.info("{} Stack bestanden opgehaald",stackFiles.size());

        if (stackFiles != null) {
            stackFiles.stream().forEach(new Consumer<StackFile>() {
                @Override
                public void accept(StackFile stackFile) {
                    LOGGER.info("{}",stackFile.getUrl());
                    Random generator = new Random(System.currentTimeMillis());
                    for (GeplandePost.Media media : GeplandePost.Media.values()) {
                        LOGGER.info("Media : {}",media);
                        LocalTime randomTime = LocalTime.now();

                        while (!tijdOk(dag, randomTime, result)) {
                            randomTime = LocalTime.MIN.plusSeconds(generator.nextLong());
                        }

                        LocalDateTime tijdstip = LocalDateTime.of(datum, randomTime);
                        LOGGER.info("tijdstip : {}",tijdstip);

                        result.add(new GeplandePost(media, tijdstip, stackFile));
                    }
                }
            });
        }

        return result.stream().sorted(new Comparator<GeplandePost>() {
            @Override
            public int compare(GeplandePost o1, GeplandePost o2) {
                return o1.getTijdstip().compareTo(o2.getTijdstip());
            }
        }).collect(Collectors.toList());
    }

    private boolean tijdOk(Dag dag, LocalTime tijd, List<GeplandePost> geplandePosts) {
        if (tijd.isBefore(dag.getStartTijd())) {
            return false;
        } else if (tijd.isAfter(dag.getEindTijd())) {
            return false;
        } else if (!nietTeDichtBijAnderen(geplandePosts, tijd, 30)) {
            return false;
        }

        return true;
    }

    protected boolean nietTeDichtBijAnderen(List<GeplandePost> geplandePosts, LocalTime localTime, int minutenTussenPosts) {
        final boolean[] ok = {true};
        geplandePosts.stream().map(geplandePost -> geplandePost.getTijdstip().toLocalTime()).forEach(localTimeBestaand -> {
            if (localTime.isAfter(localTimeBestaand.minusMinutes(minutenTussenPosts)) && localTime.isBefore(localTimeBestaand.plusMinutes(minutenTussenPosts))) {
                ok[0] = false;
            }
        });

        return ok[0];
    }
}
