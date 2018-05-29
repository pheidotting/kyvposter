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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.*;

@Service
public class PostInplanService {
    private final static Logger LOGGER = LoggerFactory.getLogger(PostInplanService.class);

    @Inject
    private StackStorageService stackStorageService;

    public enum Dag {
        Maandag(MONDAY, 2, 3, 17, 0, 22, 30), Dinsdag(TUESDAY, 2, 3, 17, 0, 22, 30), Woensdag(WEDNESDAY, 2, 3, 17, 0, 22, 30), Donderdag(THURSDAY, 2, 3, 17, 0, 22, 30), Vrijdag(FRIDAY, 2, 3, 17, 0, 22, 30), Zaterdag(SATURDAY, 3, 4, 15, 0, 22, 30), Zondag(SUNDAY, 4, 6, 12, 0, 22, 30);
        private DayOfWeek dayOfWeek;
        private int minimumAantalPosts;
        private int maximumAantalPosts;
        private LocalTime startTijd;
        private LocalTime eindTijd;

        Dag(DayOfWeek dayOfWeek, int minimumAantalPosts, int maximumAantalPosts, int startTijdUur, int startTijdMinuut, int eindTijdUur, int eindTijdMinuut) {
            this.dayOfWeek = dayOfWeek;
            this.minimumAantalPosts = minimumAantalPosts;
            this.maximumAantalPosts = maximumAantalPosts;
            this.startTijd = LocalTime.of(startTijdUur, startTijdMinuut);
            this.eindTijd = LocalTime.of(eindTijdUur, eindTijdMinuut);
        }

        public DayOfWeek getDayOfWeek() {
            return dayOfWeek;
        }

        public int getMinimumAantalPosts() {
            return minimumAantalPosts;
        }

        public int getMaximumAantalPosts() {
            return maximumAantalPosts;
        }

        public LocalTime getStartTijd() {
            return startTijd;
        }

        public LocalTime getEindTijd() {
            return eindTijd;
        }

        public static Dag getFromDayOfWeek() {
            return getFromDayOfWeek(LocalDate.now().getDayOfWeek());
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

        int aantalPosts = ThreadLocalRandom.current().nextInt(dag.getMinimumAantalPosts(), dag.getMaximumAantalPosts());

        LOGGER.info("Het is vandaag {}, dus we gaan {} posts inplannen per Social Media, dus {} in totaal", dag.toString(), aantalPosts, (aantalPosts * GeplandePost.Media.values().length));

        List<StackFile> stackFiles = null;
        try {
            stackFiles = stackStorageService.leesRandom(aantalPosts);
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOGGER.info("{} Stack bestanden opgehaald", stackFiles.size());

        if (stackFiles != null) {
            int tussenRuimte = bepaalRuimteTussenPosts(dag.getStartTijd(), dag.getEindTijd(), aantalPosts);

            int finalAantalPosts = aantalPosts;
            final int[] teller = {0};
            stackFiles.stream().forEach(new Consumer<StackFile>() {
                @Override
                public void accept(StackFile stackFile) {
                    Random generator = new Random(System.currentTimeMillis());
                    //                    GeplandePost.Media media= GeplandePost.Media.FACEBOOK;
                    for (GeplandePost.Media media : GeplandePost.Media.values()) {
                        //                        LOGGER.info("Media : {}",media);
                        LocalTime randomTime = null;
                        boolean ok = false;

                        while (!ok) {
                            randomTime = LocalTime.MIN.plusSeconds(generator.nextLong());
                            //                            LOGGER.info("A {}",randomTime);
                            //                            LOGGER.info("B {}",teller[0]);

                            //                            boolean tijdvakOk = false;
                            while (!pastBinnenTijdvakDag(dag, randomTime)) {
                                randomTime = LocalTime.MIN.plusSeconds(generator.nextLong());
                            }
                            //                            LOGGER.info("tijdvakOk {}",tijdvakOk);
                            boolean nietTeDichtBijAnderen = nietTeDichtBijAnderen(result, randomTime, tussenRuimte);

                            if (nietTeDichtBijAnderen) {
                                ok = true;
                            } else {
                                //                                LOGGER.info("Tijd {}, teller {}",randomTime,teller[0]);
                                LocalTime finalRandomTime = randomTime;
                                //                                result.stream().map(new Function<GeplandePost, LocalTime>() {
                                //                                    @Override
                                //                                    public LocalTime apply(GeplandePost geplandePost) {
                                //                                        return geplandePost.getTijdstip().toLocalTime();
                                //                                    }
                                //                                }).sorted(new Comparator<LocalTime>() {
                                //                                    @Override
                                //                                    public int compare(LocalTime o1, LocalTime o2) {
                                //                                        return o1.compareTo(o2);
                                //                                    }
                                //                                }).forEach(new Consumer<LocalTime>() {
                                //                                    @Override
                                //                                    public void accept(LocalTime geplandePost) {
                                ////                                        LOGGER.info("{} - {}",geplandePost,geplandePost.until(finalRandomTime,ChronoUnit.MINUTES));
                                //                                    }
                                //                                });
                                teller[0]++;
                            }

                            //                            && teller[0] < 11;
                            if (teller[0] > 20) {
                                //                                LOGGER.info("Geen passende tijd gevonden, opnieuw, tussenruimte {}",tussenRuimte);
                                result.clear();
                                return;
                            }
                            //                            teller[0] = 0;
                        }
                        teller[0] = 0;

                        LocalDateTime tijdstip = LocalDateTime.of(datum, randomTime);
                        //                        LOGGER.info("tijdstip : {}",tijdstip);

                        result.add(new GeplandePost(media, tijdstip, stackFile));
                    }
                }
            });

            if (teller[0] > 10) {
                return planPosts(datum);
            }
        }

        return result.stream().sorted(new Comparator<GeplandePost>() {
            @Override
            public int compare(GeplandePost o1, GeplandePost o2) {
                return o1.getTijdstip().compareTo(o2.getTijdstip());
            }
        }).collect(Collectors.toList());
    }

    private boolean pastBinnenTijdvakDag(Dag dag, LocalTime tijd) {
        if (LocalDateTime.of(LocalDate.now(), tijd).isBefore(LocalDateTime.of(LocalDate.now(), dag.getStartTijd()))) {
            return false;
        } else if (LocalDateTime.of(LocalDate.now(), tijd).isAfter(LocalDateTime.of(LocalDate.now(), dag.getEindTijd()))) {
            return false;
        }

        return true;
    }

    protected boolean nietTeDichtBijAnderen(List<GeplandePost> geplandePosts, LocalTime localTime, int minutenTussenPosts) {
        final boolean[] ok = {true};
        geplandePosts.stream().map(new Function<GeplandePost, LocalTime>() {
            @Override
            public LocalTime apply(GeplandePost geplandePost) {
                return geplandePost.getTijdstip().toLocalTime();
            }
        }).sorted(new Comparator<LocalTime>() {
            @Override
            public int compare(LocalTime o1, LocalTime o2) {
                return o1.compareTo(o2);
            }
        }).forEach(new Consumer<LocalTime>() {
            @Override
            public void accept(LocalTime localTimeBestaand) {
                if (localTime.isAfter(localTimeBestaand.minusMinutes(minutenTussenPosts)) && localTime.isBefore(localTimeBestaand.plusMinutes(minutenTussenPosts))) {
                    ok[0] = false;
                }
            }
        });

        return ok[0];
    }

    protected int bepaalRuimteTussenPosts(LocalTime begintijd, LocalTime eindtijd, int aantalPosts) {
        long aantalMinuten = begintijd.until(eindtijd, ChronoUnit.MINUTES);

        long tussenruimte = aantalMinuten / (aantalPosts + 1);

        int result = (int) (tussenruimte - (tussenruimte * 0.5));

        if (result < 1) {
            result = 1;
        }
        return result;
    }
}

