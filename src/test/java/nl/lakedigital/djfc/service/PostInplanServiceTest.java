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

import static com.google.common.collect.Lists.newArrayList;
import static org.easymock.EasyMock.anyInt;
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

        expect(stackStorageService.leesRandom(anyInt())).andReturn(newArrayList(stackFile1, stackFile2, stackFile3)).anyTimes();

        replayAll();

        List<GeplandePost> result = postInplanService.planPosts(localDate);

        verifyAll();

        assertTrue(result.size() > 0);
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

    @Test
    public void bepaalRuimteTussenPosts() {
        assertThat(postInplanService.bepaalRuimteTussenPosts(LocalTime.of(9, 0), LocalTime.of(10, 0), 2), is(16));
        assertThat(postInplanService.bepaalRuimteTussenPosts(LocalTime.of(9, 0), LocalTime.of(10, 0), 10), is(4));
        assertThat(postInplanService.bepaalRuimteTussenPosts(LocalTime.of(9, 0), LocalTime.of(10, 0), 60), is(1));
        assertThat(postInplanService.bepaalRuimteTussenPosts(LocalTime.of(9, 0), LocalTime.of(10, 0), 120), is(1));
        assertThat(postInplanService.bepaalRuimteTussenPosts(LocalTime.of(12, 0), LocalTime.of(22, 30), 5), is(84));
    }
}