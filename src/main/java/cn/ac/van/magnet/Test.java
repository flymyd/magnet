package cn.ac.van.magnet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        String code = "ABP-839";
        final String baseURL = "http://www.javlibrary.com/cn/vl_searchbyid.php?keyword=";
        try {
            Document doc = Jsoup.connect(baseURL + code).get();
            String title = doc.select("h3[class=post-title text]").text();
            String imgSrc = doc.select("img[id=video_jacket_img]").attr("src");
            imgSrc = "http:" + imgSrc;
            String actress = doc.select("span[class=star] a").text();
            String timeDuration = doc.select("div[id=video_length] span[class=text]").text();
            System.out.println(title);
            System.out.println(imgSrc);
            System.out.println(actress);
            System.out.println(timeDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
