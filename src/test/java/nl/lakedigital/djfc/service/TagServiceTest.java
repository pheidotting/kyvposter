package nl.lakedigital.djfc.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.*;
import org.junit.*;
import org.junit.runner.RunWith;

import java.util.*;
import java.util.ArrayList;

@RunWith(EasyMockRunner.class)
public class TagServiceTest extends EasyMockSupport {
    @TestSubject
    private TagService tagService=new TagService();

    @Test
    public void genereerTagsBestandsnaam() {
        String bestandsnaam = "def,ghi";
        List<String> verwacht = new ArrayList<>();
        verwacht.add("klazienaveneryoungtimervrienden");

        assertThat(tagService.genereerTags(bestandsnaam,""),is(verwacht));
    }
    @Test
    public void genereerTagsBestandsnaamMetDirs() {
        String bestandsnaam = "xyz/jkl/def,ghi";
        List<String> verwacht = new ArrayList<>();
        verwacht.add("klazienaveneryoungtimervrienden");
        verwacht.add("xyz");
        verwacht.add("jkl");

        assertThat(tagService.genereerTags(bestandsnaam,""),is(verwacht));
    }
    @Test
    public void genereerTagsBestandsnaamMetDirsMetSpatie() {
        String bestandsnaam = "xyz/jkl mno/def,ghi";
        List<String> verwacht = new ArrayList<>();
        verwacht.add("klazienaveneryoungtimervrienden");
        verwacht.add("xyz");
        verwacht.add("jkl");
        verwacht.add("mno");

        assertThat(tagService.genereerTags(bestandsnaam,""),is(verwacht));
    }
    @Test
    public void genereerTagsBestandsnaamMetWebDavPad() {
        String bestandsnaam = "webdavpad/def,ghi";
        List<String> verwacht = new ArrayList<>();
        verwacht.add("klazienaveneryoungtimervrienden");

        assertThat(tagService.genereerTags(bestandsnaam,"webdavpad"),is(verwacht));
    }
}