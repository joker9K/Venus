package proxy.proxychain;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by zhangwt n 2017/11/21.
 */
public class ProxyManager {
    private Class<?> targetClass;
    private List<Proxy> proxyList;

    public ProxyManager(Class<?> targetClass, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.proxyList = proxyList;
    }

    @SuppressWarnings("unchecked")
    public <T> T createProxy() {
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                ProxyChain proxyChain = new ProxyChain(proxyList,targetClass, target, method, args, proxy);
                return proxyChain.doProxyChain();
            }
        });
    }
}
