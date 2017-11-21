package proxy.proxychain;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by zhangwt n 2017/11/21.
 */
public class ProxyChain {

    private List<Proxy> proxyList;
    private int currentProxyIndex = 0;
    private Class<?> targetClass;
    private Object targetObject;
    private Method targetMethod;
    private Object[] methodParams;
    private MethodProxy methodProxy;
    private Object methodResult;

    public ProxyChain(List<Proxy> proxyList, Class<?> targetClass, Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy) {
        this.proxyList = proxyList;
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodParams = methodParams;
        this.methodProxy = methodProxy;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public MethodProxy getMethodProxy() {
        return methodProxy;
    }

    public Object getMethodResult() {
        return methodResult;
    }

    public Object doProxyChain()throws Throwable{
        if(currentProxyIndex < proxyList.size()){
            methodResult = proxyList.get(currentProxyIndex++).doProxy(this);
        }else {
            try {
                methodResult = methodProxy.invokeSuper(targetObject, methodParams);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }
        return methodResult;
    }
}
