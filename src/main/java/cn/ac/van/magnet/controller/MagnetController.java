package cn.ac.van.magnet.controller;

import cn.ac.van.magnet.bean.NormalResp;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class MagnetController {
    @PostMapping("/getMagnet")
    public HashMap getMagnet(@RequestParam String code) {
        final String magnetHead = "magnet:?xt=urn:btih:";
        final String baseURL = "https://btsow.space/search/";
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
                tmp.put("src", magnetHead + originSrc.replace("https://btsow.space/magnet/detail/hash/", ""));
                parentList.set(i, tmp);
            }
            return NormalResp.ok(parentList);
        } catch (Exception e) {
            e.printStackTrace();
            return NormalResp.failed();
        }
    }

    @PostMapping("/getMagnetBatch")
    public HashMap getMagnetBatch(@RequestBody CodesBatchReq codesBatchReq) {
        final String magnetHead = "magnet:?xt=urn:btih:";
        final String baseURL = "https://btsow.space/search/";
        //爬取标题及详情页路径
        ArrayList<HashMap<String, String>> parentList = new ArrayList<>();
        try {
            for (String code : codesBatchReq.getCodes()) {
                Document doc = Jsoup.connect(baseURL + code).get();
                Elements hrefList = doc.select("div[class=data-list] a[href]");
                int resNum = codesBatchReq.getNums();
                if (resNum>hrefList.size()) resNum = hrefList.size();
                for (int ii = 0;ii<resNum;ii++){
                    Element el = hrefList.get(ii);
                    parentList.add(new HashMap<String, String>() {
                        {
                            put("title", el.attr("title"));
                            put("src", el.attr("href"));
                        }
                    });
                }
                for (int i = 0; i < parentList.size(); i++) {
                    HashMap<String, String> tmp = parentList.get(i);
                    String originSrc = tmp.get("src");
                    tmp.put("src", magnetHead + originSrc.replace("https://btsow.space/magnet/detail/hash/", ""));
                    parentList.set(i, tmp);
                }
                Thread.sleep(1000);
            }
            return NormalResp.ok(parentList);
        } catch (Exception e) {
            e.printStackTrace();
            return NormalResp.failed();
        }
    }

    @PostMapping("/getInfo")
    public HashMap getInfo(@RequestParam String code) {
        final String baseURL = "http://www.javlibrary.com/cn/vl_searchbyid.php?keyword=";
        try {
            Document doc = Jsoup.connect(baseURL + code).get();
            String title = doc.select("h3[class=post-title text]").text();
            String imgSrc = "http:" + doc.select("img[id=video_jacket_img]").attr("src");
            String actress = doc.select("span[class=star] a").text();
            String timeDuration = doc.select("div[id=video_length] span[class=text]").text();
            return NormalResp.ok(new HashMap<String, String>() {
                {
                    put("title", title);
                    put("imgSrc", imgSrc);
                    put("actress", actress);
                    put("timeDuration", timeDuration);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return NormalResp.failed();
        }
    }
}

@Data
class CodesBatchReq{
    private String[] codes;
    private Integer nums;
}
