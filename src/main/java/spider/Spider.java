package spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import redis.RedisPool;
import redis.clients.jedis.Jedis;

import java.text.MessageFormat;

/**
 * Created by zhangwt n 2017/11/20.
 */
public class Spider {
    private final static String FUND_URL = "http://fund.eastmoney.com/{0}.html";


    public static void crawl(String name){
        WebDriver webDriver = WebDriverServiceUtil.getInstance();
        FundPlan plan = DataQuery.findFundPlan(name);
        StringBuilder sb = new StringBuilder("");
        for(String code : plan.getCodeSet()){
            try {
                webDriver.get(MessageFormat.format(FUND_URL,code));
                String content = webDriver.getPageSource();
                Document doc = Jsoup.parse(content);
                Element fundName = doc.getElementsByClass("fundDetail-tit").first();
                System.out.println("基金名称:"+fundName.getElementsByTag("div").first().text());
                //净值估算时间
                Element jzgs_time = doc.getElementById("gz_gztime");
                //净值估算
                Element jzgs = doc.getElementById("gz_gsz");
                System.out.println("净值估算时间:"+jzgs_time.text());
                System.out.println("净值估算:"+jzgs.text());
                //净值估算变化值
                Element jzgsbhz = doc.getElementById("gz_gszze");
                System.out.println("净值估算变化值:"+jzgsbhz.text());
                //净值估算变化百分比
                Element jzgsbhbfb = doc.getElementById("gz_gszzl");
                System.out.println("净值估算变化百分比:"+jzgsbhbfb.text());
                FundDetail fundDetail = FundDetail.builder().name(fundName.getElementsByTag("div").first().text()).time(jzgs_time.text()).jzgs(jzgs.text()).jzgsbhz(jzgsbhz.text()).jzgsbhbfb(jzgsbhbfb.text()).build();
                sb.append(fundDetail.toString());
                sb.append("\n\n\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MailContent mail = MailContent.builder().to(plan.getEmail()).subject("基金日报").content(sb.toString()).build();
        MailUtil.send(mail);
    }

    public static void main(String[] args) {
       crawl("马苋飞");
    }
}
