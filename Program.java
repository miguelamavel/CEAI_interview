import ceai.Crawler;
import ceai.FrequentElements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Program {

    public static void main(String[] args) throws MalformedURLException {
//        FrequentElements<Long> frequentElements = new FrequentElements<Long>(Arrays.asList(1L,1L,1L,2L,2L,3L,4L,3L,3L));
//        System.out.println("number " + frequentElements.findKMostFrequentElements(2).stream()
//                .map(Object::toString)
//                .collect(Collectors.joining(", ")));

        List<URL> seedList = Arrays.asList(new URL("http://stackoverflow.com/"));
        Crawler crawler = new Crawler(seedList);
        crawler.crawlSeeds();
    }
}
