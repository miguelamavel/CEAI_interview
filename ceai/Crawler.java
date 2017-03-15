package ceai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {
    private Queue<URL> urlsToCrawl;

    public Crawler(List<URL> seedList){
        urlsToCrawl = new LinkedList<>();

        for (URL url : seedList) {
            urlsToCrawl.offer(url);
        }
    }

    public void crawlSeeds() {

        int count = 0;
        while (urlsToCrawl.size()>0) {
            URL currentUrl = urlsToCrawl.poll();
            System.out.println("Crawling " + currentUrl.toString());
            List<URL> urlList = findUrlsInPage(currentUrl, getResponseFromURL(currentUrl));

            if (urlList==null)
                continue;

            for (URL urlElement : urlList) {
                urlsToCrawl.offer(urlElement);
            }
            count++;


            if(count > 4)
                break;
        }
    }

    private List<URL> findUrlsInPage(URL url, String response) {
        List<URL> urlList = new ArrayList<>();
        Pattern p = Pattern.compile("href=\"(.*?)\"");
        Matcher m = p.matcher(response);
        while (m.find()) {
            String urlString = m.group();
            urlString = urlString.replace("\"","");
            urlString = urlString.replace("href=","");

            if(urlString.length()<4 || !urlString.substring(0,4).equals("http")) {
                if (urlString.length()<2 || urlString.substring(0,2).equals("//"))
                    urlString = "http:" + urlString;
                else
                    urlString = url.toString() + urlString.substring(1);
            }
            try {
                urlList.add(new URL(urlString));
            }
            catch (MalformedURLException e) {
                //ignore malformed urls, probably bookmarks
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return urlList;
    }

    private static String getResponseFromURL(URL url) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String response = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            response = buffer.toString();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return response;
    }
}
