package nl.lakedigital.djfc.service;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.easymock.EasyMock.expect;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(EasyMockRunner.class)
public class TagServiceTest extends EasyMockSupport {
    @TestSubject
    private TagService tagService = new TagService();

    @Mock
    private RdwService rdwService;

    //mapnaam/img.jpg
    @Test
    public void alleenMapNaam() {
        String bestandsnaam = "mapnaam/img.jpg";
        List<String> verwacht = new ArrayList<>();
        verwacht.add("mapnaam");
        verwacht.add("klazienaveneryoungtimervrienden");
        verwacht.add("oldtimer");
        verwacht.add("youngtimer");

        replayAll();

        assertThat(tagService.genereerTags(bestandsnaam, ""), is(verwacht));

        verifyAll();
    }

    //map naam/img.jpg
    @Test
    public void alleenMapNaamMetSpatie() {
        String bestandsnaam = "map naam/img.jpg";
        List<String> verwacht = new ArrayList<>();
        verwacht.add("klazienaveneryoungtimervrienden");
        verwacht.add("oldtimer");
        verwacht.add("naam");
        verwacht.add("map");
        verwacht.add("youngtimer");

        replayAll();

        assertThat(tagService.genereerTags(bestandsnaam, ""), is(verwacht));

        verifyAll();
    }

    //map,naam/img.jpg
    @Test
    public void alleenMapNaamMetKomma() {
        String bestandsnaam = "map,naam/img.jpg";
        List<String> verwacht = new ArrayList<>();
        verwacht.add("klazienaveneryoungtimervrienden");
        verwacht.add("oldtimer");
        verwacht.add("naam");
        verwacht.add("map");
        verwacht.add("youngtimer");

        replayAll();

        assertThat(tagService.genereerTags(bestandsnaam, ""), is(verwacht));

        verifyAll();
    }

    //mapnaam1/mapnaam2/img.jpg
    @Test
    public void alleenMapNaamDubbeleMapNaam() {
        String bestandsnaam = "mapnaam1/mapnaam2/img.jpg";
        List<String> verwacht = new ArrayList<>();
        verwacht.add("mapnaam1");
        verwacht.add("klazienaveneryoungtimervrienden");
        verwacht.add("mapnaam2");
        verwacht.add("oldtimer");
        verwacht.add("youngtimer");

        replayAll();

        assertThat(tagService.genereerTags(bestandsnaam, ""), is(verwacht));

        verifyAll();
    }

    //map1 naam1/map2 naam2/img.jpg
    @Test
    public void alleenMapNaamDubbeleMapNaamMetSpatie() {
        String bestandsnaam = "map1 naam1/map2 naam2/img.jpg";
        List<String> verwacht = new ArrayList<>();
        verwacht.add("klazienaveneryoungtimervrienden");
        verwacht.add("oldtimer");
        verwacht.add("map2");
        verwacht.add("map1");
        verwacht.add("naam2");
        verwacht.add("naam1");
        verwacht.add("youngtimer");

        replayAll();

        assertThat(tagService.genereerTags(bestandsnaam, ""), is(verwacht));

        verifyAll();
    }

    //map1,naam1/map2,naam2/img.jpg
    @Test
    public void alleenMapNaamDubbeleMapNaamMetKomma() {
        String bestandsnaam = "map1,naam1/map2,naam2/img.jpg";
        List<String> verwacht = new ArrayList<>();
        verwacht.add("klazienaveneryoungtimervrienden");
        verwacht.add("oldtimer");
        verwacht.add("map2");
        verwacht.add("map1");
        verwacht.add("naam2");
        verwacht.add("naam1");
        verwacht.add("youngtimer");

        replayAll();

        assertThat(tagService.genereerTags(bestandsnaam, ""), is(verwacht));

        verifyAll();
    }

    //mapnaam/img kenteken1.jpg
    @Test
    public void alleenMapNaamMetKenteken() {
        String bestandsnaam = "mapnaam/img kenteken1.jpg";
        List<String> verwacht = new ArrayList<>();
        verwacht.add("mapnaam");
        verwacht.add("klazienaveneryoungtimervrienden");
        verwacht.add("a");
        verwacht.add("b");
        verwacht.add("oldtimer");
        verwacht.add("youngtimer");

        List<String> kentekeninfo = newArrayList("a", "b");


        expect(rdwService.leesHashTags("kenteken1")).andReturn(kentekeninfo);

        replayAll();

        assertThat(tagService.genereerTags(bestandsnaam, ""), is(verwacht));

        verifyAll();
    }

    //mapnaam/img kenteken1 kenteken2.jpg
    @Test
    public void alleenMapNaamMetKentekens() {
        String bestandsnaam = "mapnaam/img kenteken1 kenteken2.jpg";
        List<String> verwacht = new ArrayList<>();
        verwacht.add("mapnaam");
        verwacht.add("klazienaveneryoungtimervrienden");
        verwacht.add("a");
        verwacht.add("b");
        verwacht.add("oldtimer");
        verwacht.add("c");
        verwacht.add("d");
        verwacht.add("youngtimer");

        List<String> kentekeninfo1 = newArrayList("a", "b");
        List<String> kentekeninfo2 = newArrayList("c", "d");

        expect(rdwService.leesHashTags("kenteken1")).andReturn(kentekeninfo1);
        expect(rdwService.leesHashTags("kenteken2")).andReturn(kentekeninfo2);

        replayAll();

        assertThat(tagService.genereerTags(bestandsnaam, ""), is(verwacht));


        verifyAll();
    }
}