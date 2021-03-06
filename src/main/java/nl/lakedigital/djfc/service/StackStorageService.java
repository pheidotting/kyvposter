package nl.lakedigital.djfc.service;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import nl.lakedigital.djfc.domain.IngeplandePost;
import nl.lakedigital.djfc.models.StackFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class StackStorageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StackStorageService.class);

    @Inject
    private TagService tagService;
    @Inject
    private IngeplandePostService ingeplandePostService;

    private Sardine sardine;

    @PostConstruct
    public void init() {
        sardine = SardineFactory.begin("pheidotting", "Herman79!");
    }

    private final String WEBDAV_SERVER = "https://pheidotting.stackstorage.com";
    private final String WEBDAV_PATH = "/remote.php/webdav/KlazienavenerYoungtimerVrienden";

    public Sardine getSardine() {
        return sardine;
    }

    public String getPad() {
        return WEBDAV_SERVER + WEBDAV_PATH;
    }

    public String getWEBDAV_SERVER() {
        return WEBDAV_SERVER;
    }

    public String getWEBDAV_PATH() {
        return WEBDAV_PATH;
    }

    public List<StackFile> leesRandom(int aantal) throws IOException {
        List<DavResource> resources = haalLijstOpServerOp(WEBDAV_SERVER + WEBDAV_PATH);
        List<StackFile> result = newArrayList();

        List<DavResource> filteredResources = resources.stream().filter(davResource -> !davResource.toString().endsWith("/")).collect(Collectors.toList());

        Collections.shuffle(filteredResources);
        filteredResources.subList(0, aantal).stream().forEach(davResource -> result.add(new StackFile(tagService.genereerTags(davResource.toString(), WEBDAV_PATH), davResource.toString())));

        return result;
    }

    public List<DavResource> haalLijstOpServerOp(String pad) throws IOException {
        List<DavResource> resources = sardine.list(pad);
        List<DavResource> result = new ArrayList<>();

        resources.stream().forEach(davResource -> {
            if (davResource.toString().endsWith(".jpg")) {
                result.add(davResource);
            }
        });

        List<DavResource> resources1 = resources.stream().filter(davResource -> {
            String p = pad + "/";
            return davResource.toString().endsWith("/") && !p.equals(WEBDAV_SERVER + davResource.toString()) && !pad.equals(WEBDAV_SERVER + davResource.toString());
        }).collect(Collectors.toList());

        for (DavResource davResource : resources1) {
            try {
                result.addAll(haalLijstOpServerOp(WEBDAV_SERVER + davResource.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public List<DavResource> haalDirectoriesOpServerOp(String pad) throws IOException {
        List<DavResource> resources = sardine.list(pad);
        List<DavResource> result = new ArrayList<>();

        resources.stream().forEach(davResource -> {
            if (davResource.isDirectory()) {
                result.add(davResource);
            }
        });

        for (DavResource davResource : resources) {
            try {
                if (!pad.equals(WEBDAV_SERVER + davResource.toString())) {
                    result.addAll(haalDirectoriesOpServerOp(WEBDAV_SERVER + davResource.toString()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public void opruimen(IngeplandePost ingeplandePost) {
        List<IngeplandePost> ingeplandePosts = ingeplandePostService.leesPostsMetZelfdeResource(ingeplandePost);

        if (!ingeplandePosts.stream().filter(ingeplandePost1 -> {
            LOGGER.info("{}", ingeplandePost1.getTijdstipUitgevoerd());
            return ingeplandePost1.getTijdstipUitgevoerd() == null;
        }).findAny().isPresent()) {
            LOGGER.info("opruimen : {}", WEBDAV_SERVER + ingeplandePost.getResource());
            try {
                getSardine().delete(WEBDAV_SERVER + ingeplandePost.getResource());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //        try {
        //            for (DavResource davResource : haalDirectoriesOpServerOp(WEBDAV_SERVER + WEBDAV_PATH)) {
        //                if (sardine.list(WEBDAV_SERVER + davResource.getPath()).size() == 0) {
        //                    sardine.delete(davResource.getPath());
        //                }
        //            }
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
    }
}
