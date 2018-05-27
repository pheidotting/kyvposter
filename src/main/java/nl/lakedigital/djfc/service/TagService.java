package nl.lakedigital.djfc.service;

import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class TagService {
    @Inject
    private RdwService rdwService;

    public List<String> genereerTags(String bestandsnaam, String pad) {
        Set<String> tags = new HashSet<>();
        String kenteken = null;

        bestandsnaam = bestandsnaam.replace(pad, "");
        String[] dirs = bestandsnaam.split("/");
        for (int i = 0; i < dirs.length; i++) {
            if (dirs[i] != null && !"".equals(dirs[i])) {
                if (!dirs[i].endsWith(".jpg")) {
                    for (String dir : dirs[i].split(" ")) {
                        for (String d : dir.split(",")) {
                            tags.add(d);
                            kenteken = d;
                        }
                    }
                } else {
                    String[] parts = dirs[i].split(" ");
                    if (parts.length > 1) {
                        for (int j = 1; j < parts.length; j++) {
                            tags.addAll(rdwService.leesHashTags(parts[j].replace(".jpg", "")));
                        }

                    }
                }
                //                for(String dir : dirs[i].split(" ")){
                //                    for (String d : dir.split(",")) {
                //                        tags.add(d);
                //                        kenteken=d;
                //                    }
                //                }
            }
        }

        //        if (kenteken != null) {
        //            tags.addAll(rdwService.leesHashTags(kenteken));
        //        }

        tags.add("klazienaveneryoungtimervrienden");
        tags.add("youngtimer");
        tags.add("oldtimer");

        return newArrayList(tags);
    }
}
