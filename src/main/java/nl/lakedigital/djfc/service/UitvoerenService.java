package nl.lakedigital.djfc.service;

import nl.lakedigital.djfc.models.GeplandePost;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

@Service
public class UitvoerenService {
    @Inject
    private List<SocialMediaService> socialMediaServices;

    public void voeruit(GeplandePost geplandePost) throws IOException {
        SocialMediaService socialMediaService = socialMediaServices.stream().filter(new Predicate<SocialMediaService>() {
            @Override
            public boolean test(SocialMediaService socialMediaService) {
                return socialMediaService.voorMij(geplandePost.getMedia());
            }
        }).findFirst().get();

        socialMediaService.voeruit(geplandePost);
    }
}
