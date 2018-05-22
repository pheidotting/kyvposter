package nl.lakedigital.djfc.service;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(EasyMockRunner.class)
public class TagServiceTest extends EasyMockSupport {
    @TestSubject
    private TagService tagService=new TagService();

    @Test
    public void genereerTagsBestandsnaam() {
        String bestandsnaam = "def,ghi";
        List<String> verwacht = new ArrayList<>();
        verwacht.add("klazienaveneryoungtimervrienden");
        verwacht.add("oldtimer");
        verwacht.add("youngtimer");

        assertThat(tagService.genereerTags(bestandsnaam,""),is(verwacht));
    }
    @Test
    public void genereerTagsBestandsnaamMetDirs() {
        String bestandsnaam = "xyz/jkl/def,ghi";
        List<String> verwacht = new ArrayList<>();
        verwacht.add("klazienaveneryoungtimervrienden");
        verwacht.add("oldtimer");
        verwacht.add("xyz");
        verwacht.add("jkl");
        verwacht.add("youngtimer");

        assertThat(tagService.genereerTags(bestandsnaam,""),is(verwacht));
    }
    @Test
    public void genereerTagsBestandsnaamMetDirsMetKomma() {
        String bestandsnaam = "fgh,xyz/jkl mno/def,ghi";
        List<String> verwacht = new ArrayList<>();
        verwacht.add("klazienaveneryoungtimervrienden");
        verwacht.add("oldtimer");
        verwacht.add("fgh");
        verwacht.add("xyz");
        verwacht.add("jkl");
        verwacht.add("youngtimer");
        verwacht.add("mno");

        assertThat(tagService.genereerTags(bestandsnaam,""),is(verwacht));
    }
    @Test
    public void genereerTagsBestandsnaamMetWebDavPad() {
        String bestandsnaam = "webdavpad/def,ghi";
        List<String> verwacht = new ArrayList<>();
        verwacht.add("klazienaveneryoungtimervrienden");
        verwacht.add("oldtimer");
        verwacht.add("youngtimer");

        assertThat(tagService.genereerTags(bestandsnaam,"webdavpad"),is(verwacht));
    }
}