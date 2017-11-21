package proxy.proxychain;

import java.lang.reflect.Method;

/**
 * Created by zhangwt n 2017/11/21.
 */
public abstract class AbstractProxy implements Proxy{
    @Override
    public final Object doProxy(ProxyChain proxyChain) throws Throwable{
        Object result = null;

        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        begin();
        try {
            if (filter(cls, method, params)) {
                before(cls, method, params);
                result = proxyChain.doProxyChain();
                after(cls, method, params);
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            error(cls, method, params, e);
            throw e;
        } finally {
            end();
        }
        return result;
    }

    public void begin() {
    }

    public boolean filter(Class<?> cls, Method method, Object[] params) {
        return true;
    }

    public void before(Class<?> cls, Method method, Object[] params) {
    }

    public void after(Class<?> cls, Method method, Object[] params) {
    }

    public void error(Class<?> cls, Method method, Object[] params, Throwable e) {
    }

    public void end() {
    }
}
