package nl.lakedigital.djfc.service;

import nl.lakedigital.djfc.models.GeplandePost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

public abstract class SocialMediaService {
    private final static Logger LOGGER = LoggerFactory.getLogger(SocialMediaService.class);
    @Inject
    protected StackStorageService stackStorageService;

    protected InputStream getStackBestand(GeplandePost geplandePost) {
        try {
            LOGGER.info("Ophalen {}", stackStorageService.getWEBDAV_SERVER() + geplandePost.getStackFile().getUrl());
            return stackStorageService.getSardine().get(stackStorageService.getWEBDAV_SERVER() + geplandePost.getStackFile().getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    abstract void voeruit(GeplandePost geplandePost) throws IOException;

    abstract boolean voorMij(GeplandePost.Media media);
}
