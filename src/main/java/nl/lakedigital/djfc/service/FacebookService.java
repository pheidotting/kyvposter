package nl.lakedigital.djfc.service;

import com.restfb.*;
import com.restfb.types.GraphResponse;
import nl.lakedigital.djfc.models.GeplandePost;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FacebookService extends SocialMediaService {
    private String accessCode = "EAACTjLkmFvYBAEZBZCbvmuRsfWbOKWotR9kNRZCXdp4CYXxRB3ZAk6Y93smYojHRw8BZB5zxyl5b1YPfySLYMY1CFZASZBpZC8moZCPOw567Dn4hxa6hPtfiJgMJ7bFbRg7J9UiAiZAx3k1ZAkA5szZAaqqUA4LWMu5oKWtdmwZBbu0bZAYgZDZD";
    private FacebookClient fbClient;

    @Override
    public void voeruit(GeplandePost geplandePost) throws IOException {
        fbClient = new DefaultFacebookClient(accessCode, Version.VERSION_2_6);

        InputStream stackBestand = getStackBestand(geplandePost);

        //       byte[] buffer = new byte[stackBestand.available()];
        fbClient.publish("me/photos", GraphResponse.class, BinaryAttachment.with("Klazienavener Youngtimer Vrienden.jpg", getBytesFromInputStream(stackBestand), "image/jpeg"), Parameter.with("message", geplandePost.getStackFile().tagsToString()));
    }

    @Override
    boolean voorMij(GeplandePost.Media media) {
        return media == GeplandePost.Media.FACEBOOK;
    }

    protected byte[] getBytesFromInputStream(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[65535];
            for (int len; (len = is.read(buffer)) != -1; ) {
                os.write(buffer, 0, len);
            }
            os.flush();
            return os.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }
}
