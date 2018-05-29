package nl.lakedigital.djfc.repository;

import nl.lakedigital.djfc.domain.IngeplandePost;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Repository
public class IngeplandePostRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(IngeplandePostRepository.class);

    @Autowired
    private SessionFactory sessionFactory;

    public Session getSession() {
        try {
            return sessionFactory.getCurrentSession();
        } catch (HibernateException e) {//NOSONAR
            return sessionFactory.openSession();
        }
    }

    @Transactional
    public void opslaan(IngeplandePost ingeplandePost) {
        LOGGER.info("Opslaan enkel");
        opslaan(newArrayList(ingeplandePost));
    }

    @Transactional
    public void opslaan(List<IngeplandePost> ingeplandePosts) {
        LOGGER.info("Opslaan {} post(s)", ingeplandePosts.size());
        for (IngeplandePost ingeplandePost : ingeplandePosts) {
            if (ingeplandePost.getId() == null) {
                getSession().save(ingeplandePost);
            } else {
                getSession().merge(ingeplandePost);
            }
        }
    }

    @Transactional
    public IngeplandePost lees(Long id){
        return getSession().get(IngeplandePost.class,id);
    }

    @Transactional
    public List<IngeplandePost> leesBijResource(String resource){
        Query query = getSession().getNamedQuery("IngeplandePost.leesBijResource");
        query.setParameter("resource", resource);

        return query.list();
    }

    @Transactional
    public IngeplandePost laatstVerstuurdePost() {
        Query query = getSession().getNamedQuery("IngeplandePost.laatstVerstuurdePost");

        return (IngeplandePost) query.uniqueResult();
    }

    @Transactional
    public List<IngeplandePost> overgeblevenPosts() {
        Query query = getSession().getNamedQuery("IngeplandePost.overgeblevenPosts");

        return query.list();
    }

    @Transactional
    public List<IngeplandePost> ingeplandePostsVoorDatum(LocalDate datum) {
        LocalDateTime startTijdstip = LocalDateTime.of(datum, LocalTime.of(0, 0, 1));
        LocalDateTime eindTijdstip = LocalDateTime.of(datum, LocalTime.of(23, 59, 59));

        Query query = getSession().getNamedQuery("IngeplandePost.ingeplandePostsVoorDatum");
        query.setParameter("startTijdstip", startTijdstip);
        //        query.setParameter("eindTijdstip", eindTijdstip);

        return query.list();
    }

    @Transactional
    public List<IngeplandePost> ingeplandeOnverzondenPostsVoorDatum(LocalDate datum) {
        LocalDateTime startTijdstip = LocalDateTime.of(datum, LocalTime.of(0, 0, 1));
        LocalDateTime eindTijdstip = LocalDateTime.of(datum, LocalTime.of(23, 59, 59));

        Query query = getSession().getNamedQuery("IngeplandePost.ingeplandeOnverzondenPostsVoorDatum");
        query.setParameter("startTijdstip", startTijdstip);
        //        query.setParameter("eindTijdstip", eindTijdstip);

        return query.list();
    }
}
