package com.wsj.server.util;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wsj.server.api.BaseApi;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.boot.system.ApplicationHome;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClashUtil {
    private static String template = "port: 7890\n" +
            "socks-port: 7891\n" +
            "allow-lan: true\n" +
            "mode: Rule\n" +
            "log-level: info\n" +
            "external-controller: :9090\n" +
            "proxies: []\n" +
            "proxy-groups:\n" +
            "  - name: \uD83D\uDD30 节点选择\n" +
            "    type: select\n" +
            "    proxies: []\n" +
            "rules:\n" +
            "  - DOMAIN-SUFFIX,amazon.co.jp,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,d3c33hcgiwev3.cloudfront.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,payments-jp.amazon.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,s3-ap-northeast-1.amazonaws.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,s3-ap-southeast-2.amazonaws.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,a248.e.akamai.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,a771.dscq.akamai.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,testflight.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,clubhouseapi.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,joinclubhouse.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,clubhouse.pubnubapi.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,aex.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bibox.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,binance.cc,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,binance.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,binance.us,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bitcointalk.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bitfinex.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bithumb.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bitmex.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bitstamp.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bittrex.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bybit.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,coinbase.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,coincheck.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,coingecko.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,coinmarketcap.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,coinone.co.kr,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ftx.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,gate.io,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,gemini.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,huobi.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,korbit.co.kr,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,kraken.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,kucoin.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,liquid.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,okex.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,poloniex.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,uniswap.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,zb.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,discord.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,discordapp.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,discordapp.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,dropbox.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,dropboxapi.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,dropboxusercontent.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,cdninstagram.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,facebook.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,facebook.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,fb.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,fb.me,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,fbaddins.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,fbcdn.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,fbsbx.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,fbworkmail.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,instagram.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,m.me,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,messenger.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,oculus.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,oculuscdn.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,rocksdb.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,whatsapp.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,whatsapp.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,github.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,github.io,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,githubusercontent.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,appspot.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,blogger.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,getoutline.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,gvt0.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,gvt1.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,gvt3.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,xn--ngstr-lra8j.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ytimg.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-KEYWORD,google,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-KEYWORD,.blogspot.,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,line.me,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,line-apps.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,line-scdn.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,naver.jp,\uD83D\uDD30 节点选择\n" +
            "  - IP-CIDR,103.2.30.0/23,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - IP-CIDR,125.209.208.0/20,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - IP-CIDR,147.92.128.0/17,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - IP-CIDR,203.104.144.0/21,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - DOMAIN-SUFFIX,aka.ms,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,onedrive.live.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,streaming.mediaservices.windows.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,assets1.xboxlive.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,assets2.xboxlive.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,az416426.vo.msecnd.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,az668014.vo.msecnd.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nyt.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nytchina.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nytcn.me,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nytco.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nytimes.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nytimg.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nytlog.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nytstyle.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pinterest.at,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pinterest.ca,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pinterest.co.uk,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pinterest.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pinterest.de,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pinterest.fr,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pinterest.jp,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pinterest.se,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pixiv.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pixiv.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pximg.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,redd.it,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,reddit.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,redditmedia.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,telegra.ph,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,telegram.org,\uD83D\uDD30 节点选择\n" +
            "  - IP-CIDR,91.108.4.0/22,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - IP-CIDR,91.108.8.0/22,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - IP-CIDR,91.108.12.0/22,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - IP-CIDR,91.108.16.0/22,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - IP-CIDR,91.108.20.0/22,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - IP-CIDR,91.108.56.0/22,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - IP-CIDR,91.105.192.0/23,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - IP-CIDR,149.154.160.0/20,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - IP-CIDR,185.76.151.0/24,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - IP-CIDR6,2001:b28:f23d::/48,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - IP-CIDR6,2001:b28:f23f::/48,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - IP-CIDR6,2001:67c:4e8::/48,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - IP-CIDR6,2001:b28:f23c::/48,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - IP-CIDR6,2a0a:f280::/32,\uD83D\uDD30 节点选择,no-resolve\n" +
            "  - DOMAIN-SUFFIX,pscp.tv,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,periscope.tv,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,t.co,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,twimg.co,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,twimg.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,twitpic.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,twitter.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,vine.co,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,wikileaks.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,wikimapia.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,wikimedia.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,wikinews.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,wikipedia.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,wikiquote.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,4shared.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,9cache.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,9gag.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,abc.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,abc.net.au,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,abebooks.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ao3.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,apigee.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,apkcombo.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,apk-dl.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,apkfind.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,apkmirror.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,apkmonk.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,apkpure.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,aptoide.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,archive.is,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,archive.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,archiveofourown.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,archiveofourown.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,arte.tv,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,artstation.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,arukas.io,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ask.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,avg.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,avgle.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,badoo.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bandcamp.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bandwagonhost.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bangkokpost.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bbc.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,behance.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,biggo.com.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bit.ly,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bloglovin.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bloomberg.cn,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bloomberg.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,blubrry.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,book.com.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,booklive.jp,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,books.com.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,boslife.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,box.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,brave.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,businessinsider.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,buzzfeed.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bwh1.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,castbox.fm,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,cbc.ca,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,cdw.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,change.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,channelnewsasia.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ck101.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,clarionproject.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,cloudcone.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,clyp.it,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,cna.com.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,comparitech.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,conoha.jp,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,crucial.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,cts.com.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,cw.com.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,cyberctm.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,cyclingnews.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,dailymotion.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,dailyview.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,dandanzan.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,daum.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,daumcdn.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,dcard.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,deadline.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,deepdiscount.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,depositphotos.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,deviantart.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,disconnect.me,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,disqus.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,dlercloud.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,dmhy.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,dns2go.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,dowjones.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,duckduckgo.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,duyaoss.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,dw.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,dynu.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,earthcam.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ebookservice.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,economist.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,edgecastcdn.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,edx-cdn.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,elpais.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,enanyang.my,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,encyclopedia.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,esoir.be,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,etherscan.io,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,euronews.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,evozi.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,exblog.jp,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,feeder.co,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,feedly.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,feedx.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,firech.at,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,flickr.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,flipboard.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,flitto.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,foreignpolicy.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,fortawesome.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,freetls.fastly.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,friday.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ft.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ftchinese.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ftimg.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,genius.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,getlantern.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,getsync.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,globalvoices.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,goo.ne.jp,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,goodreads.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,gov.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,gravatar.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,greatfire.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,gumroad.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,heroku.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,hightail.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,hk01.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,hkbf.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,hkbookcity.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,hkej.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,hket.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,hootsuite.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,hudson.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,huffpost.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,hyread.com.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ibtimes.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,i-cable.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,icij.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,icoco.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,imgur.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,independent.co.uk,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,initiummall.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,inoreader.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,insecam.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ipfs.io,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,issuu.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,istockphoto.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,japantimes.co.jp,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,jiji.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,jinx.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,jkforum.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,joinmastodon.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,justmysocks.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,justpaste.it,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,kadokawa.co.jp,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,kakao.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,kakaocorp.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,kik.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,kingkong.com.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,knowyourmeme.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,kobo.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,kobobooks.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,kodingen.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,lemonde.fr,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,lepoint.fr,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,lihkg.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,limbopro.xyz,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,listennotes.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,livestream.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,logimg.jp,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,logmein.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,mail.ru,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,mailchimp.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,marc.info,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,matters.news,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,maying.co,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,medibang.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,medium.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,mega.nz,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,mergersandinquisitions.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,mingpao.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,mixi.jp,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,mixlr.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,mobile01.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,mubi.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,myspace.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,myspacecdn.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nanyang.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nationalinterest.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,naver.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nbcnews.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ndr.de,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,neowin.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,newstapa.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nexitally.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nhk.or.jp,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nii.ac.jp,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nikkei.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nitter.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nofile.io,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,notion.so,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,now.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nrk.no,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nuget.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,nyaa.si,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ok.ru,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,on.cc,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,orientaldaily.com.my,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,overcast.fm,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,paltalk.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,parsevideo.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pawoo.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pbxes.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pcdvd.com.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pchome.com.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pcloud.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,peing.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,picacomic.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pinimg.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,player.fm,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,plurk.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,po18.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,potato.im,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,potatso.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,prism-break.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,proxifier.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pt.im,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pts.org.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pubu.com.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pubu.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,pureapk.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,quora.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,quoracdn.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,qz.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,radio.garden,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,rakuten.co.jp,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,rarbgprx.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,reabble.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,readingtimes.com.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,readmoo.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,redbubble.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,resilio.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,reuters.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,reutersmedia.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,rfi.fr,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,roadshow.hk,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,rsshub.app,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,scmp.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,scribd.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,seatguru.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,shadowsocks.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,shindanmaker.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,shopee.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,signal.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,sina.com.hk,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,slideshare.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,softfamous.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,spiegel.de,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,startpage.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,steamcommunity.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,steemit.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,steemitwallet.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,straitstimes.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,streamable.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,streema.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,substack.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,substack.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,t66y.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,tapatalk.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,teco-hk.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,teco-mo.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,teddysun.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,textnow.me,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,theguardian.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,theinitium.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,themoviedb.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,thetvdb.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,time.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,tineye.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,tiny.cc,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,tinyurl.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,torproject.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,tradingview.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,tumblr.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,turbobit.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,tutanota.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,tvboxnow.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,udn.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,unseen.is,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,upmedia.mg,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,uptodown.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,urbandictionary.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ustream.tv,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,uwants.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,v2ex.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,v2fly.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,v2ray.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,viber.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,videopress.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,vimeo.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,voachinese.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,voanews.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,voxer.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,vzw.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,w3schools.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,washingtonpost.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,wattpad.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,whoer.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,wikiwand.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,winudf.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,wire.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,wn.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,wordpress.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,worldcat.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,wsj.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,wsj.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,xhamster.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,xn--90wwvt03e.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,xn--i2ru8q2qg.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,xnxx.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,xvideos.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,yadi.sk,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,yahoo.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,yandex.ru,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ycombinator.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,yesasia.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,yes-news.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,yomiuri.co.jp,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,you-get.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,zaobao.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,zello.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,zeronet.io,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,z-lib.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,zoom.us,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,cc.tvbs.com.tw,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,ocsp.int-x3.letsencrypt.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,us.weibo.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,edu,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,gov,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,mil,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,abc.xyz,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,advertisercommunity.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ampproject.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,android.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,androidify.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,autodraw.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,capitalg.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,certificate-transparency.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,chrome.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,chromeexperiments.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,chromestatus.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,chromium.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,creativelab5.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,debug.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,deepmind.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,dialogflow.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,firebaseio.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,getmdl.io,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ggpht.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,gmail.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,gmodules.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,godoc.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,gstatic.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,gv.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,gwtproject.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,itasoftware.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,madewithcode.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,material.io,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,page.link,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,polymer-project.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,recaptcha.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,shattered.io,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,synergyse.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,telephony.goog,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,tensorflow.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,tfhub.dev,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,tiltbrush.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,waveprotocol.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,waymo.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,webmproject.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,webrtc.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,whatbrowser.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,widevine.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,x.company,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,youtu.be,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,yt.be,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ytimg.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,t.me,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,tdesktop.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,telegram.me,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,telesco.pe,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-KEYWORD,.facebook.,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,facebookmail.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,noxinfluencer.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,smartmailcloud.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,weebly.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,twitter.jp,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,appsto.re,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,books.itunes.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,smoot.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,beta.music.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,lookup-api.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,apps.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,books.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,itunes.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,tv.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,amp-api.podcasts.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,bookkeeper.itunes.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,gateway.icloud.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,apple.news,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,news-assets.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,news-client.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,news-client-search.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,news-edge.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,news-events.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,apple.comscoreresearch.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,go.dev,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,golang.org,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,aicoin.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,aimoon.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,bing.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,cccat.io,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,dubox.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,duboxcdn.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,ifixit.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,linkedin.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,mangakakalot.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,shopeemobile.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,sushi.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,appleid.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,developer.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,www.icloud.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,ocsp.apple.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,cacerts.digicert.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,crl3.digicert.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,crl4.digicert.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,ocsp.digicert.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,inkbunny.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,metapix.net,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,s3.amazonaws.com,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN-SUFFIX,zaobao.com.sg,\uD83D\uDD30 节点选择\n" +
            "  - DOMAIN,international-gfe.download.nvidia.com,\uD83D\uDD30 节点选择";
    public static String updateClashBode() throws Exception {
        String url = "https://clashnode.com";
        BaseApi clash = SpringUtil.getBean(BaseApi.class);
        String test = clash.getRequest(url);
        Pattern pattern = Pattern.compile("(?<=href=\")https://\\S+\\.html");
        Matcher matcher = pattern.matcher(test);
        if (!matcher.find(0)) {
            return url + "正则匹配最新节点html地址失败" ;
        }
        //获取最新的节点地址
        String nodeUrl = matcher.group();
        String node = clash.getRequest(nodeUrl);
        Pattern compile = Pattern.compile("https://\\S+\\.ya?ml");
        Matcher matcher1 = compile.matcher(node);
        if (!matcher1.find(0)) {
            return url + "正则匹配yaml地址失败" ;
        }
        String resultUrl = matcher1.group();
        String content = clash.getRequest(resultUrl);
        //可能需要调整
        Yaml yaml = new Yaml();
        Map hashMap = yaml.loadAs(content, Map.class);
        if (hashMap.containsKey("proxies")) {
            filter(hashMap,true);
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml wyaml = new Yaml(dumperOptions);
            String savePath = getSavePath();
            File dumpfile = new File(savePath + "clash.yaml"); //保存yaml
            try(FileWriter writer = new FileWriter(dumpfile)) {
                wyaml.dump(hashMap, writer);
            } catch (IOException e) {
                e.printStackTrace();
                return url +  "生成yaml失败" ;
            }
            System.out.println("解析节点成功  sucess !!!");
            return null;
        }
        return url + "解析节点失败" ;
    }

    public static String updateClashFree() throws Exception {
        String url = "https://clashfree.eu.org/";
        BaseApi clash = SpringUtil.getBean(BaseApi.class);
        String test = clash.getRequest(url);
        Pattern pattern = Pattern.compile("(?<=href=\")https://\\S+\\.html");
        Matcher matcher = pattern.matcher(test);
        if (!matcher.find(0)) {
            return url + "正则匹配最新节点html地址失败";
        }
        //获取最新的节点地址
        String nodeUrl = matcher.group();
        String node = clash.getRequest(nodeUrl);
        Pattern compile = Pattern.compile("https://\\S+\\.ya?ml");
        Matcher matcher1 = compile.matcher(node);
        if (!matcher1.find(0)) {
            return url + "正则匹配yaml地址失败";
        }
        String resultUrl = matcher1.group();
        String content = clash.getRequest(resultUrl);
        //可能需要调整
        Yaml yaml = new Yaml();
        Map hashMap = yaml.loadAs(content, Map.class);
        if (hashMap.containsKey("proxies")) {
            filter(hashMap,false);
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml wyaml = new Yaml(dumperOptions);
            String savePath = getSavePath();
            File dumpfile = new File(savePath + "clash3.yaml"); //保存yaml
            try(FileWriter writer = new FileWriter(dumpfile)) {
                wyaml.dump(hashMap, writer);
            } catch (IOException e) {
                e.printStackTrace();
                return url + "生成yaml失败";
            }
            System.out.println("解析节点成功  sucess !!!");
            return null;
        }
        return url + "解析节点失败";
    }


    public static String updateNodeFree() throws Exception {
        String url = "https://nodefree.org/t/clash";
        BaseApi clash = SpringUtil.getBean(BaseApi.class);
        String test = clash.getRequest(url);
        Pattern pattern = Pattern.compile("(?<=href=\")https://\\S+\\.html");
        Matcher matcher = pattern.matcher(test);
        if (!matcher.find(0)) {
            return url + "正则匹配最新节点html地址失败" ;
        }
        //获取最新的节点地址
        String nodeUrl = matcher.group();
        String node = clash.getRequest(nodeUrl);
        Pattern compile = Pattern.compile("https://\\S+\\.ya?ml");
        Matcher matcher1 = compile.matcher(node);
        if (!matcher1.find(0)) {
            return url + "正则匹配yaml地址失败" ;
        }
        String resultUrl = matcher1.group();
        String content = clash.getRequest(resultUrl);
        //可能需要调整
        Yaml yaml = new Yaml();
        Map hashMap = yaml.loadAs(content, Map.class);
        if (hashMap.containsKey("proxies")) {
            filter(hashMap,true);
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml wyaml = new Yaml(dumperOptions);
            String savePath = getSavePath();
            File dumpfile = new File(savePath + "clash2.yaml"); //保存yaml
            try(FileWriter writer = new FileWriter(dumpfile)) {
                wyaml.dump(hashMap, writer);
            } catch (IOException e) {
                e.printStackTrace();
                return url + "生成yaml失败" ;
            }
            System.out.println("解析节点成功  sucess !!!");
            return null;
        }
        return url + "解析节点失败" ;
    }

    private static void filter(Map hashMap,boolean autoSelect) {
        List array =(List) hashMap.get("proxies");
        List<String> nameList = new ArrayList<>();
        Iterator<Object> iterator = array.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            Map map = (Map) obj;
            String type = (String) map.getOrDefault("type",null);
            if ("vless".equals(type) || map.containsKey("plugin")) {
                nameList.add((String) map.get("name"));
                iterator.remove();
            }
        }
        if(hashMap.containsKey("proxy-groups")){
            List arr = (List) hashMap.get("proxy-groups");
            for (int i = 0; i < arr.size(); i++) {
                Map obj = (Map)arr.get(i);
                if (i == 0 && autoSelect && obj.containsKey("type")) {
                    obj.put("type", "url-test");
                    obj.put("url", "http://www.gstatic.com/generate_204");
                    obj.put("interval",300);
                }
                List proxies = (List)obj.get("proxies");
                Iterator<Object> iterator1 = proxies.iterator();
                while (iterator1.hasNext()) {
                    String next = (String)iterator1.next();
                    if (nameList.contains(next)) {
                        iterator1.remove();
                        continue;
                    }
                    if (i == 0 && autoSelect && next.equals("DIRECT")) {
                        iterator1.remove();
                    }
                }
            }
        }
    }

    public static String githubV2rayToClash() throws Exception {
        String url = "https://github.com/freefq/free";
        BaseApi clash = SpringUtil.getBean(BaseApi.class);
        String test = clash.getRequest(url);
        Pattern pattern = Pattern.compile("vmess://[a-zA-Z0-9+/=]+");
        Matcher matcher = pattern.matcher(test);
        Set<String> jdList = new HashSet<>();
        while (matcher.find()) {
            jdList.add(matcher.group().trim());
        }
        if (jdList.size() == 0) {
            return "https://github.com/freefq/free 没有解析到vmess";
        }
        return vmessToCLash(jdList);
    }

    public static String vmessToCLash(Set<String> jdList) throws IOException {
        List<JSON> proxies = new ArrayList<>();
        for (String s : jdList) {
            String chineseString = StringEscapeUtils.unescapeJava(new String(Base64.getDecoder().decode(s.substring(8, s.length()))));
            JSON j = JSONUtil.parse(chineseString);
            JSON json = new JSONObject();
            JSON opts = new JSONObject();
            JSON header = new JSONObject();
            opts.putByPath("path",j.getByPath("path"));
            header.putByPath("host",j.getByPath("host"));
            opts.putByPath("headers",header);
            json.putByPath("type", "vmess");
            json.putByPath("name", j.getByPath("ps"));
            json.putByPath("ws-opts", opts);
            json.putByPath("server", j.getByPath("add"));
            json.putByPath("port", j.getByPath("port"));
            json.putByPath("uuid", j.getByPath("id"));
            json.putByPath("alterId", j.getByPath("aid"));
            json.putByPath("cipher", null == j.getByPath("scy")?"auto":j.getByPath("scy"));
            json.putByPath("network", j.getByPath("net"));
            json.putByPath("tls", "tls".equals(j.getByPath("tls")));
            proxies.add(json);
        }
        Yaml yaml = new Yaml();
        Map hashMap = yaml.loadAs(template, Map.class);
        hashMap.put("proxies", proxies);
        List groups = (List)hashMap.get("proxy-groups");
        Map o = (Map)groups.get(0);
        o.put("proxies", proxies.stream().map(x->x.getByPath("name")).collect(Collectors.toList()));

        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml wyaml = new Yaml(dumperOptions);
        String savePath = getSavePath();
        File dumpfile = new File(savePath + "clash4.yaml"); //保存yaml
        try(FileWriter writer = new FileWriter(dumpfile)) {
            wyaml.dump(hashMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
            return "生成yaml失败" ;
        }
        System.out.println("解析节点成功  sucess !!!");
        return  null;
    }


    public static String getSavePath() throws IOException {
        ApplicationHome h = new ApplicationHome(ClashUtil.class);
        File jarF = h.getSource();
        String staticPath = jarF.getParentFile().toString()+File.separator+"files"+File.separator;
        File newFile = new File(staticPath);
        if (!newFile.exists()) {
            newFile.mkdir();
        }
        return staticPath;
    }
}
