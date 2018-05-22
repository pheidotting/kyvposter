package nl.lakedigital.djfc.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class TagService {
    public List<String> genereerTags(String bestandsnaam, String pad) {
        Set<String> tags = new HashSet<>();

        bestandsnaam = bestandsnaam.replace(pad, "");
        String[] dirs = bestandsnaam.split("/");
        for (int i = 0; i < dirs.length - 1; i++) {
            if (dirs[i] != null && !"".equals(dirs[i])) {
                for(String dir : dirs[i].split(" ")){
                    for (String d : dir.split(",")) {
                        tags.add(d);
                    }
                }
            }
        }

        tags.add("klazienaveneryoungtimervrienden");
        tags.add("youngtimer");
        tags.add("oldtimer");

        return newArrayList(tags);
    }
}
