package nl.lakedigital.djfc.service;

import nl.lakedigital.djfc.models.GeplandePost;
import nl.lakedigital.djfc.models.StackFile;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.collect.Lists.newArrayList;
import static org.easymock.EasyMock.expect;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(EasyMockRunner.class)
public class PostInplanServiceTest extends EasyMockSupport {
    @TestSubject
    private PostInplanService postInplanService = new PostInplanService();

    @Mock
    private StackStorageService stackStorageService;

    @Test
    public void testInplannen() throws IOException {
        LocalDate localDate = LocalDate.of(2018, 5, 16);//Woensdag

        StackFile stackFile1 = new StackFile(newArrayList("#1"), null);
        StackFile stackFile2 = new StackFile(newArrayList("#2"), null);
        StackFile stackFile3 = new StackFile(newArrayList("#3"), null);

        expect(stackStorageService.leesRandom(PostInplanService.Dag.Woensdag.getAantalPosts())).andReturn(newArrayList(stackFile1, stackFile2, stackFile3));

        replayAll();

        List<GeplandePost> result = postInplanService.planPosts(localDate);

        result.stream().forEach(new Consumer<GeplandePost>() {
            @Override
            public void accept(GeplandePost geplandePost) {
                System.out.println(geplandePost.getTijdstip()+" - " + geplandePost.getMedia());
            }
        });

        verifyAll();

        assertThat(result.size(), is(PostInplanService.Dag.Woensdag.getAantalPosts() * GeplandePost.Media.values().length));
    }

    @Test
    public void nietTeDichtBijAnderen() {
        GeplandePost geplandePost1 = new GeplandePost(GeplandePost.Media.FACEBOOK, LocalDateTime.of(2018, 1, 1, 12, 00),null);
        GeplandePost geplandePost2 = new GeplandePost(GeplandePost.Media.FACEBOOK, LocalDateTime.of(2018, 1, 1, 13, 00),null);
        GeplandePost geplandePost3 = new GeplandePost(GeplandePost.Media.FACEBOOK, LocalDateTime.of(2018, 1, 1, 15, 30),null);

        List<GeplandePost> lijst = newArrayList(geplandePost3, geplandePost2, geplandePost1);

        assertTrue(postInplanService.nietTeDichtBijAnderen(lijst, LocalTime.of(11, 29), 30));
        assertFalse(postInplanService.nietTeDichtBijAnderen(lijst, LocalTime.of(12, 29), 30));
        assertFalse(postInplanService.nietTeDichtBijAnderen(lijst, LocalTime.of(11, 31), 30));
        assertTrue(postInplanService.nietTeDichtBijAnderen(lijst, LocalTime.of(13, 31), 30));
        assertTrue(postInplanService.nietTeDichtBijAnderen(lijst, LocalTime.of(14, 1), 30));
        assertTrue(postInplanService.nietTeDichtBijAnderen(lijst, LocalTime.of(14, 29), 30));
        assertTrue(postInplanService.nietTeDichtBijAnderen(lijst, LocalTime.of(14, 31), 30));
        assertFalse(postInplanService.nietTeDichtBijAnderen(lijst, LocalTime.of(15, 01), 30));
        assertFalse(postInplanService.nietTeDichtBijAnderen(lijst, LocalTime.of(15, 59), 30));
        assertTrue(postInplanService.nietTeDichtBijAnderen(lijst, LocalTime.of(16, 1), 30));
    }
}