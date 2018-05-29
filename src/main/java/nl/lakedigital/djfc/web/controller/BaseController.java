package nl.lakedigital.djfc.web.controller;

import nl.lakedigital.djfc.domain.IngeplandePost;
import nl.lakedigital.djfc.models.GeplandePost;
import nl.lakedigital.djfc.models.StackFile;
import nl.lakedigital.djfc.service.*;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUploadPhotoRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

@RequestMapping("/facebook")
@Controller
public class BaseController {
    //    private String MY_APP_ID = "162232610985718";
    //    private String MY_APP_SECRET = "7d3fd4251f896efe79366447686547be";
    //
    //    private FacebookClient client;
    //
    //    @Inject
    //    private FacebookService facebookService;
    @Inject
    private StackStorageService stackStorageService;
    //    @Inject
    //    private PostInplanService postInplanService;
    @Inject
    private TagService tagService;
    @Inject
    private IngeplandePostService ingeplandePostService;
    @Inject
    private InstagramService instagramService;
    @Inject
    private FacebookService facebookService;

    //
    //    @RequestMapping(method = RequestMethod.GET, value = "/login")
    //    @ResponseBody
    //    public RedirectView login() {
    //        ScopeBuilder scopeBuilder = new ScopeBuilder();
    //        scopeBuilder.addPermission(FacebookPermissions.PUBLISH_PAGES);
    //        scopeBuilder.addPermission(FacebookPermissions.MANAGE_PAGES);
    //        scopeBuilder.addPermission(FacebookPermissions.PUBLISH_ACTIONS);
    //
    //        client = new DefaultFacebookClient(Version.VERSION_2_6);
    //        String loginDialogUrlString = client.getLoginDialogUrl(MY_APP_ID, "http://localhost:8080/kyvposter/facebook/done", scopeBuilder);
    //
    //        return new RedirectView(loginDialogUrlString);
    //    }
    //
    //    @RequestMapping(method = RequestMethod.GET, value = "/done")
    //    @ResponseBody
    //    public String done() throws IOException {
    //
    ////        FacebookClient.AccessToken accessToken = client.obtainUserAccessToken(MY_APP_ID,
    ////                MY_APP_SECRET, "http://localhost:8080/kyvposter/facebook/done", code);//
    ////
    ////        facebookService.setAccessCode(accessToken.getAccessToken());
    //        facebookService.setAccessCode("EAACEdEose0cBAGHZBFKvc1njGQHeRuZB9Ak9ZCQLnQEFEfCQ2nGS1ZC2OAkhE4yt3DKrwPGANvrMKr1LAZAscLQNZCNjYdizzzfZASWeLMUgqpCg58Tazn79hXqWEXzeTHP9qZAh5NPgJLXzgXhTzQZBSHVNvd4TbGoZACMZAeo4Azrs4u1uRwWWwwytZAyR8Xh7IsVy1psMfGzCLQZDZD");
    ////        client= new DefaultFacebookClient("EAACEdEose0cBAC4eAkL1z3kAZCrNTRGjEmkUKZAOMVeFZBf2tDEZAnIUj2P4NFrGEQzYj0a3LVYXJXU2P51Y7WgUSNhWZBS8LAcyyRV3ZBgqGfgVIl1qZCtJ9EkVPOmcabStskob1OPSowX2BYQTCsil8NO9f9Br9eV2as00QzigZAcyMp4UZAswL1a884gVY3uLT53o5eREz1gZDZD");
    ////facebookService.extendAccesscode(MY_APP_ID,MY_APP_SECRET);
    ////        facebookService.publish();
    ////        Page page = client.fetchObject("534731113372521", Page.class,
    ////                Parameter.with("fields","access_token"));
    //
    //return "";
    ////        return facebookService.publish(stackStorageService.leesRandom());//accessToken.getAccessToken();
    //    }
    //
    @RequestMapping(method = RequestMethod.GET, value = "/instagram")
    @ResponseBody
    public String instagram() throws IOException {
        Instagram4j instagram = Instagram4j.builder().username("klveneryoungtimervrienden").password("klazienaveen").build();
        instagram.setup();
        instagram.login();

        //        InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest("patrickheidotting"));
        //        System.out.println("ID for @github is " + userResult.getUser().getPk());
        //        System.out.println("Number of followers: " + userResult.getUser().getFollower_count());

        instagram.sendRequest(new InstagramUploadPhotoRequest(new File("/Users/patrickheidotting/Pictures/TestKYV/feefffaaaca.jpg"), "Posted with Instagram4j, how cool is that?"));

        return "";//String.valueOf(userResult.getUser().getFollower_count());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/instagram/{id}")
    @ResponseBody
    public String instagram(@PathVariable("id") Long id) throws IOException {
        IngeplandePost ingeplandePost = ingeplandePostService.lees(id);

        StackFile stackFile = new StackFile(tagService.genereerTags(ingeplandePost.getResource(), stackStorageService.getWEBDAV_PATH()), ingeplandePost.getResource());
        GeplandePost geplandePost = new GeplandePost(ingeplandePost.getMedia(), ingeplandePost.getTijdstipIngepland(), stackFile);

        //        instagramService.voeruit(geplandePost);
        facebookService.voeruit(geplandePost);

        //        Instagram4j instagram = Instagram4j.builder().username("klveneryoungtimervrienden").password("klazienaveen").build();
        //        instagram.setup();
        //        instagram.login();
        //
        ////        InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest("patrickheidotting"));
        ////        System.out.println("ID for @github is " + userResult.getUser().getPk());
        ////        System.out.println("Number of followers: " + userResult.getUser().getFollower_count());
        //
        //        instagram.sendRequest(
        //        new InstagramUploadPhotoRequest(
        //                new File("/Users/patrickheidotting/Pictures/TestKYV/feefffaaaca.jpg"),
        //                "Posted with Instagram4j, how cool is that?"));

        return "";//String.valueOf(userResult.getUser().getFollower_count());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/opruimen")
    @ResponseBody
    public void opruimen() throws IOException {
        IngeplandePost ingeplandePost = ingeplandePostService.lees(118L);

        stackStorageService.opruimen(ingeplandePost);
    }
    ////
    ////    @RequestMapping(method = RequestMethod.GET, value = "/inplannen")
    ////    @ResponseBody
    //////    public List<GeplandePost> inplannen() throws IOException {
    ////        return postInplanService.planPosts(LocalDate.now()).stream().map(new Function<GeplandePost, GeplandePost>() {
    ////            @Override
    ////            public GeplandePost apply(GeplandePost geplandePost) {
    ////                System.out.println(geplandePost.getStackFile().tagsToString());
    ////                 geplandePost.setTags(geplandePost.getStackFile().getTags());
    ////                 geplandePost.setStackFile(null);
    ////
    ////                 return geplandePost;
    ////            }
    ////        }).collect(Collectors.toList());
    ////    }
}
