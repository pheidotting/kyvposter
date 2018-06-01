package nl.lakedigital.djfc.service;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.TestSubject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(EasyMockRunner.class)
public class StackStorageServiceTest extends EasyMockSupport {
    @TestSubject
    private StackStorageService stackStorageService = new StackStorageService();

    @Test
    @Ignore
    public void haalDirectoriesOpServerOp() throws IOException {
        List<String> leeg = new ArrayList<>();
        stackStorageService.init();
        stackStorageService.haalDirectoriesOpServerOp(stackStorageService.getWEBDAV_SERVER() + stackStorageService.getWEBDAV_PATH()).stream().forEach(davResource -> {
            try {
                if (stackStorageService.getSardine().list(stackStorageService.getWEBDAV_SERVER() + davResource.getPath()).size() == 0) {
                    leeg.add(stackStorageService.getWEBDAV_SERVER() + davResource.getPath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        leeg.stream().forEach(s -> System.out.println(s));
    }
}