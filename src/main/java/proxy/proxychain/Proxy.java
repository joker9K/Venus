package proxy.proxychain;

/**
 * Created by zhangwt n 2017/11/21.
 */
public interface Proxy {

    Object doProxy(ProxyChain proxyChain)throws Throwable;
}
