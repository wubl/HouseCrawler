package com.wstar.house.core;

import com.virjar.vscrawler.core.VSCrawler;
import com.virjar.vscrawler.core.VSCrawlerBuilder;
import com.virjar.vscrawler.core.processor.BindRouteProcessor;

public class HouseCrawlerExecutor {
    public static void main(String[] args) {
        VSCrawler vsCrawler = VSCrawlerBuilder.create()
                .setProcessor(new HouseListProcessor())
                .setSessionPoolCoreSize(15)// 核心15个用户
                .setSessionPoolMaxSize(55)// 最多55个用户
                .setSessionPoolReuseDuration(60 * 1000)// 每个用户1分钟之内不能同时使用
                .setSessionPoolMaxOnlineDuration(10 * 60 * 1000)// 每个用户最多存活10分钟
                .setWorkerThreadNumber(10)// 爬虫线程数目为 10个
                .build();
        vsCrawler.clearTask();

        vsCrawler.start();
        vsCrawler.pushSeed("https://nj.focus.cn/loupan/?saleStatus=6");
    }
}
