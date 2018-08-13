package com.wstar.house.core;

import com.virjar.dungproxy.client.httpclient.CrawlerHttpClient;
import com.virjar.vscrawler.core.net.session.CrawlerSession;
import com.virjar.vscrawler.core.processor.GrabResult;
import com.virjar.vscrawler.core.processor.SeedProcessor;
import com.virjar.vscrawler.core.seed.Seed;
import com.wstar.house.entity.HouseAddressEntity;
import com.wstar.house.entity.HouseHeaderInfoEntitiy;
import com.wstar.house.entity.HouseItemEntity;
import com.wstar.house.entity.HouseSmallPicture;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.util.*;

public class HouseListProcessor implements SeedProcessor {
    private static String HOUSE_LIST_URL_PATTERN = "^https\\://nj\\.focus\\.cn/loupan(/p\\d+)?/\\?saleStatus=6$";

    private static String NEW_HOUSE_LIST_URL = "https://nj.focus.cn/loupan";

    @Override
    public void process(Seed seed, CrawlerSession crawlerSession, GrabResult grabResult) {
        try {
            if (seed.getData().matches(HOUSE_LIST_URL_PATTERN)) {
                String html = crawlerSession.getCrawlerHttpClient().get(seed.getData());
                Document document = Jsoup.parse(html);

                // 抓取房子列表
                List<HouseItemEntity> houseItems = crawHouseListPage(document);

                // 抓取房子更多信息
                for (HouseItemEntity houseItemEntity : houseItems) {
                    Map<String,String> urls = crawlHouseMoreUrls(crawlerSession.getCrawlerHttpClient(), houseItemEntity.getDetailLink());
                    if (seed.getData().contains("nanjing"))
                        houseItemEntity.setCity("nanjing");
                    System.out.println(urls);
                }

                // 数据写入数据库
                if (houseItems.size() > 0) {
//                    HouseDao.insertHouseItems(houseItems);
                }

                // 提取下一页URL
                Elements pageElems = document.select("div.page > ul.clearfix > li.fr > a.active");
                Element nextPageElem = pageElems.first().nextElementSibling();
                if (null != nextPageElem && !"last".equals(nextPageElem.className())) {
                    String nextPageUrl = nextPageElem.attr("href");
                    grabResult.addSeed(NEW_HOUSE_LIST_URL + nextPageUrl);

                    File file = new File("e:/seed.txt");
                    FileUtils.writeStringToFile(file, NEW_HOUSE_LIST_URL + nextPageUrl + "\r\n", true);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static SSLConnectionSocketFactory createSSLConnSocketFactory()
            throws Exception {
        SSLContext sslcontext = SSLContexts
                .custom()
                .loadTrustMaterial(
                        new File(
                                "e:/trust.keystore"),
                        "123456".toCharArray(), new TrustSelfSignedStrategy())
                .build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext, new String[] { "TLSv1" }, null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        return sslsf;
    }

    private List<HouseItemEntity> crawHouseListPage(Document document) {
        Elements elements = document.select("div.nhouse_list_content > div.nhouse_list > div > ul > li");
        Iterator<Element> elementIterator = elements.iterator();
        List<HouseItemEntity> houseItems = new ArrayList<HouseItemEntity>();
        while (elementIterator.hasNext()) {
            HouseItemEntity houseItemEntity = new HouseItemEntity();

            Element baseElement = elementIterator.next();

            if (baseElement.select("div.nlc_img").size() > 0
                    && baseElement.select("div.nlc_details").size() > 0) {
                Element smallPicLink = baseElement.child(0).child(0).child(0);
                Elements imgElements = baseElement.select("div.nlc_img > a > img");

                HouseSmallPicture houseSmallPicture = new HouseSmallPicture();
                houseSmallPicture.setTargetLink(smallPicLink.attr("href"));
                houseSmallPicture.setPictureUrl(imgElements.last().attr("src"));

                Elements houseDetailElement = baseElement.select("div.nlc_details");
                Element houseNameElement = houseDetailElement.select("div.nlcd_name > a").get(0);

                Elements houseRoomsElem = houseDetailElement.select("div.house_type > a");
                Iterator<Element> houseTypeIter = houseRoomsElem.iterator();
                List<String> houseRooms = new ArrayList<String>();
                while (houseTypeIter.hasNext()) {
                    Element houseTypeElem = houseTypeIter.next();
                    houseRooms.add(houseTypeElem.text());
                }

                Element houseAddressElem = houseDetailElement.select("div.relative_message > div.address > a").first();
                String houseAddress = houseAddressElem.ownText();
                if (StringUtils.isNotEmpty(houseAddress)) {
                    HouseAddressEntity houseAddressEntity = new HouseAddressEntity();
                    int _index1 = houseAddress.indexOf("[");
                    int _index2 = houseAddress.indexOf("]");
                    if (_index1 > -1 && _index2 > -1) {
                        houseAddressEntity.setRegion(houseAddress.substring(_index1 + 1, _index2));
                        houseAddressEntity.setDetailAddress(houseAddress.substring(_index2 + 1).trim());
                    } else {
                        houseAddressEntity.setDetailAddress(houseAddress);
                    }
                    houseItemEntity.setHouseAddress(houseAddressEntity);
                }

                Elements houseTypeElems = houseDetailElement.select("div.fangyuan > a");
                Iterator<Element> _elementIterator = houseTypeElems.iterator();
                List<String> houseTypes = new ArrayList<String>();
                while (_elementIterator.hasNext()) {
                    Element element = _elementIterator.next();
                    houseTypes.add(element.ownText());
                }

                // 抓取房屋价格
                String price = "";
                Elements priceElems = houseDetailElement.select("div.nhouse_price");
                if (priceElems != null && priceElems.first() != null) {
                    price = priceElems.first().select("span").first().ownText();
                }

                Element houseAreaRegionElem = houseDetailElement.select("div.house_type").first();
                houseItemEntity.setAreaRange(houseAreaRegionElem.ownText());
//                houseItemEntity.setHouseTypes(houseTypes);
                houseItemEntity.setHouseRooms(houseRooms);
                houseItemEntity.setHouseName(houseNameElement.text());
                houseItemEntity.setDetailLink(houseNameElement.attr("href"));
                houseItemEntity.setHouseSmallPicture(houseSmallPicture);
                houseItemEntity.setPrice(price);
                houseItems.add(houseItemEntity);
            }
        }

        return houseItems;
    }

    private Map<String,String> crawlHouseMoreUrls(CrawlerHttpClient crawlerHttpClient, String url) {
        String html = crawlerHttpClient.get(url);
        Document document = Jsoup.parse(html);

        Map<String, String> urls = new HashMap<>();
        Elements elements = document.select("div.nav div.navleft > a");
        for (Element element : elements) {
            if (!element.ownText().contains("在线问答")
                    && !element.ownText().contains("房源租售")
                    && !element.ownText().contains("业主论坛")
                    && !element.ownText().contains("点评")) {
                if (element.ownText().contains("楼盘首页")) {
                    urls.put("houseHeader", element.attr("href"));
                } else if (element.ownText().contains("详细信息")) {
                    urls.put("houseDetail", element.attr("href"));
                } else if (element.ownText().contains("动态")) {
                    urls.put("houseDynamic", element.attr("href"));
                } else if (element.ownText().contains("相册")) {
                    urls.put("housePhotoAlbum", element.attr("href"));
                } else if (element.ownText().contains("走势")) {
                    urls.put("housePriceTrend", element.attr("href"));
                } else if (element.ownText().contains("房源")) {
                    urls.put("houseResource", element.attr("href"));
                } else if (element.ownText().contains("户型")) {
                    urls.put("houseType", element.attr("href"));
                }
            }
        }

        return urls;
    }

    /**
     * 抓取楼盘首页信息
     */
    private HouseHeaderInfoEntitiy crawlHouseHeaderInfo(CrawlerHttpClient crawlerHttpClient, String url) {
        HouseHeaderInfoEntitiy houseHeaderInfoEntitiy = new HouseHeaderInfoEntitiy();

        String html = crawlerHttpClient.get(url);
        Document document = Jsoup.parse(html);

        // 抓取一种页面架构
        if (document.select("div.banner-box > div.banner > div.lp-info").size() > 0) {

        } else if (document.select("div.firstright > div.information").size() > 0) {
            Elements informationElement = document.select("div.firstright > div.information");

            // 抓取另一种页面架构
            Elements elements = informationElement.select("div.inf_left1 > div.tit > h1 > strong");
            if (elements.size() > 0) {
                houseHeaderInfoEntitiy.setHouseName(elements.first().ownText());
            }
            elements = informationElement.select("div.information_li > div.inf_left > span.prib");
            if (elements.size() > 0) {
                houseHeaderInfoEntitiy.setAveragePrice(elements.first().ownText());
            }
            elements = informationElement.select("div.information_li > div.inf_right.fl.pr > div.fnzoushi01 > p");
            if (elements.size() > 0) {
                String priceDescrip = elements.first().ownText() + "\n"
                                    + elements.first().nextElementSibling().ownText();
                houseHeaderInfoEntitiy.setPriceDescription(priceDescrip);
            }
        }

        return houseHeaderInfoEntitiy;
    }
}
