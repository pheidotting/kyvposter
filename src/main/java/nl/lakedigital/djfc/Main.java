package nl.lakedigital.djfc;

import nl.lakedigital.djfc.service.LeesBestandenService;

import java.io.File;
import java.util.Collection;

public class Main {

    public static void main(String[] args) throws Exception {
        LeesBestandenService leesBestandenService = new LeesBestandenService();
        //        FacebookService facebookService=new FacebookService();
        //
        //
        Collection<File> bestanden = leesBestandenService.leesBestanden();
        System.out.println(bestanden.size());

        //    facebookService.leesBerichten();
        //
    }

}
