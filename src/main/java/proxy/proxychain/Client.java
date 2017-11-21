package proxy.proxychain;

import proxy.HelloImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwt n 2017/11/21.
 */
public class Client {

    public static void main(String[] args) {
        List<Proxy> proxyList = new ArrayList<>();
        proxyList.add(new BeginProxy());
        proxyList.add(new BeforeProxy());
        proxyList.add(new AfterProxy());

        ProxyManager proxyManager = new ProxyManager(HelloImpl.class, proxyList);
        HelloImpl helloProxy = proxyManager.createProxy();

        helloProxy.say("Jack");
    }
}
