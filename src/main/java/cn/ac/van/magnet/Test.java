package cn.ac.van.magnet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) throws IOException {
        final String magnetHead = "magnet:?xt=urn:btih:";
        final String baseURL = "https://bteve.com/search/";
        String code = "GVG-450";
        //爬取标题及详情页路径
        ArrayList<HashMap<String, String>> parentList = new ArrayList();
        Document doc = Jsoup.connect(baseURL + code).get();
        Elements hrefList = doc.select("div[class=data-list] a[href]");
        for (Element el : hrefList) {
            parentList.add(new HashMap<String, String>() {
                {
                    put("title", el.attr("title"));
                    put("src", el.attr("href"));
                }
            });
        }
        //遍历结果集，取出哈希值并拼接为磁力链接
        for (int i = 0; i < parentList.size(); i++) {
            HashMap<String, String> tmp = parentList.get(i);
            String originSrc = tmp.get("src");
            tmp.put("src", magnetHead + originSrc.replace("https://bteve.com/magnet/detail/hash/", ""));
            parentList.set(i, tmp);
        }
        System.out.println(parentList.toString());
    }
}
