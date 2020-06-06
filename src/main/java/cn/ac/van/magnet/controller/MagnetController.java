package cn.ac.van.magnet.controller;

import cn.ac.van.magnet.bean.NormalResp;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class MagnetController {
    @PostMapping("/getMagnet")
    public HashMap getMagnet(@RequestParam String code) {
        final String magnetHead = "magnet:?xt=urn:btih:";
        final String baseURL = "https://bteve.com/search/";
        //爬取标题及详情页路径
        ArrayList<HashMap<String, String>> parentList = new ArrayList<>();
        try {
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
            return NormalResp.ok(parentList);
        } catch (Exception e) {
            e.printStackTrace();
            return NormalResp.failed();
        }
    }
}
