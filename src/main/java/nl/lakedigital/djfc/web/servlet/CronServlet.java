package nl.lakedigital.djfc.web.servlet;

import nl.lakedigital.djfc.service.PostInplannenEnUitvoerenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class CronServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(CronServlet.class);

    @Inject
    private PostInplannenEnUitvoerenService postInplannenEnUitvoerenService;

    @Scheduled(fixedDelay = 58000)
    public void run() {
//        LOGGER.info("RUN FORREST");
        postInplannenEnUitvoerenService.planEnVoerUit();
    }
}
