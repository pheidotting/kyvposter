package nl.lakedigital.djfc.service;

import nl.lakedigital.djfc.domain.IngeplandePost;

public class OpruimenDavResourceService implements Runnable {
    private IngeplandePost ingeplandePost;

    public OpruimenDavResourceService(IngeplandePost ingeplandePost) {
        this.ingeplandePost = ingeplandePost;
    }

    @Override
    public void run() {
        StackStorageService stackStorageService = new StackStorageService();
        stackStorageService.opruimen(this.ingeplandePost);
    }
}
