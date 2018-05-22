package nl.lakedigital.djfc.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

public class LeesBestandenService {
    //    RdwService rdwService = new RdwService();
    //
    public Collection<File> leesBestanden() throws Exception {
        String pad = "/Users/patrickheidotting/Pictures/TestKYV";
        String output = "/Users/patrickheidotting/kyvbestanden";
        String[] extensies = {"jpg", "JPG"};

        Collection<File> bestanden = FileUtils.listFiles(new File(pad), extensies, false);

        for (File f : bestanden) {
            File newFile = null;
            //                System.out.println(f);
            String newName;

            String[] split = f.getName().split(" ");
            if (split.length == 1) {
                newName = f.getName();
            } else {
                StringBuilder stringBuilder = new StringBuilder();

                List<String> tags = newArrayList();
                for (int i = 1; i < split.length; i++) {
                    if (i > 1) {
                        tags.add(split[i].trim().replace("#", "").replace("-", "").replace(".jpg", ""));
                    }
                }
                Collections.sort(tags, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o2.compareTo(o1);
                    }
                });
                for (String tag : tags) {
                    if (!"".equals(stringBuilder.toString())) {
                        stringBuilder.append("/");
                    }
                    stringBuilder.append(tag);
                }
                stringBuilder.append("/");
                stringBuilder.append(genereerName());
                stringBuilder.append(".jpg");
                newName = stringBuilder.toString();
            }

            System.out.println(newName);

            //                String kentekens = f.getName().substring(8).trim().toUpperCase().replace(".JPG", "");

            //                System.out.println(kentekens);

            //                String[] kentekensArr = kentekens.split(" ");
            //
            //                Set<String> hashtags = new HashSet<>();
            //                StringBuilder sb = new StringBuilder();
            //                for (String kenteken : kentekensArr) {
            //                    //                    System.out.println(kenteken);
            //                    hashtags.addAll(rdwService.leesHashTags(kenteken));
            //
            //                }
            //                if (hashtags.size() > 1) {
            //                    for (String hashtag : hashtags) {
            //                        if (hashtag != null) {
            //                            sb.append(" #");
            //                            sb.append(hashtag.replace(" ", ""));
            //                        }
            //                    }
            //                }
            //                sb.append(" #oldtimer");
            //                sb.append(" #youngtimer");
            //                sb.append(" #klazienaveen");
            //                sb.append(" #klazienaveneryoungtimervrienden");
            //                sb.append(" #oldtimerdag");
            //                sb.append(" #oldtimerdag2018");
            //                String hashtagsString = sb.toString();
            //
            //                //                System.out.println(hashtagsString);

            newFile = new File(output + "/" + newName);
            //
            //            System.out.println(f.toString() + " naar : " + newFile.toString());
            //
            FileUtils.copyFile(f, newFile);
            //            f.delete();
            //
        }

        return FileUtils.listFiles(new File(pad), extensies, false);
    }

    private String newName(String pad) {
        String name = genereerName();

        File f = new File(pad + "/" + name);

        while (f.exists()) {
            name = genereerName();
            f = new File(pad + "/" + name);
        }

        return name;
    }

    private String genereerName() {
        String uuid = UUID.randomUUID().toString().replace("-", "");

        StringBuffer newName = new StringBuffer();

        for (int i = 0; i < uuid.length(); i++) {
            char c = uuid.charAt(i);
            if (!NumberUtils.isNumber(String.valueOf(c))) {
                newName.append(c);
            }
        }

        return newName.toString();
    }
}
