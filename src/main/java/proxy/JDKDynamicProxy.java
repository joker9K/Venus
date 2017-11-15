package proxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by zhangwt n 2017/11/13.
 * jdk自带的动态代理
 */
public class JDKDynamicProxy implements InvocationHandler{

    private Object target;

    public JDKDynamicProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(target,args);
        after();
        return result;
    }


    private void before(){
        System.out.println("Before!");
    }

    private void after(){
        System.out.println("After");
    }


    @SuppressWarnings("unchecked")
    public <T> T getProxy(){
        return (T)Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
    }


    public static void main(String[] args) {
        JDKDynamicProxy proxy = new JDKDynamicProxy(new HelloImpl());
        Hello hello = proxy.getProxy();
        hello.say("Jack");
    }
}
