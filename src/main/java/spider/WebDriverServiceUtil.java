package spider;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author zhangwt
 * @date 2017/6/19 19:55.
 */
@Slf4j
public class WebDriverServiceUtil {
    private final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";


    public static WebDriver getInstance(){
        System.setProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/opt/webdriver/phantomjs");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX+"userAgent", USER_AGENT);
        caps.setBrowserName("chrome");
        caps.setPlatform(Platform.MAC);
        //设置属性,包括关闭安全策略,图片加载,开启缓存,忽略https错误,ssl加密协议
        List<String> cliArgsCap = new ArrayList<>();
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
                cliArgsCap);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX+"User-Agent", USER_AGENT);
        WebDriver driver = new PhantomJSDriver(caps);
        driver.manage().window().setSize(new Dimension(2560,1600));
        /*全局设置，关于JavaScript代码的异步处理的超时时间。AJAX请求。*/
        driver.manage().timeouts().setScriptTimeout(30L, TimeUnit.SECONDS);
        /*全局设置，页面加载的最长等待时间。*/
        driver.manage().timeouts().pageLoadTimeout(20L, TimeUnit.SECONDS);
        /*全局设置，当元素识别不到的时候，可以接受的最长等待时间。*/
        driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
        return driver;
    }


    public static WebDriver getChromeInstance(){

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-web-security");
        options.addArguments("-disable-infobars");
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");


//        options.addArguments("--proxy="+getSocks5Proxy());
//        options.addArguments("--proxy-type=socks5");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        System.setProperty("webdriver.chrome.driver", "/opt/webdriver/chromedriver");
        WebDriver webDriver = new ChromeDriver(capabilities);
//        webDriver.manage().window().maximize();
        return webDriver;
    }


    public static WebDriver getFirefoxInstance(){
//        System.setProperty("webdriver.firefox.bin","/Applications/Firefox.app/Contents/MacOS/firefox");
//        System.setProperty("webdriver.firefox.bin","/usr/local/firefox/firefox");
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
        firefoxBinary.addCommandLineOptions("--disable-web-security");
        System.setProperty("webdriver.gecko.driver", "/opt/webdriver/geckodriver");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
//        firefoxOptions.setLegacy(true);
//        System.setProperty("webdriver.firefox.marionette","/Applications/Firefox.app/Contents/MacOS/firefox");
        System.setProperty("webdriver.firefox.marionette", "false");
        FirefoxDriver driver = new FirefoxDriver(firefoxOptions);
        return driver;
    }

}
