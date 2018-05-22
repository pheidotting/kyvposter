package nl.lakedigital.djfc.service;

import nl.lakedigital.djfc.models.GeplandePost;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUploadPhotoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class InstagramService extends SocialMediaService {
    private final static Logger LOGGER = LoggerFactory.getLogger(InstagramService.class);

    @Override
    public void voeruit(GeplandePost geplandePost) throws IOException {
        InputStream stackBestand = getStackBestand(geplandePost);

        Instagram4j instagram = Instagram4j.builder().username("klveneryoungtimervrienden").password("klazienaveen").build();
        instagram.setup();
        instagram.login();

        File targetFile = File.createTempFile("instagram", "post");

        OutputStream outputStream = new FileOutputStream(targetFile);

        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = stackBestand.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }

        targetFile.deleteOnExit();

        instagram.sendRequest(new InstagramUploadPhotoRequest(targetFile, geplandePost.getStackFile().tagsToString()));
    }

    @Override
    boolean voorMij(GeplandePost.Media media) {
        return media == GeplandePost.Media.INSTAGRAM;
    }
}
