package proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by zhangwt n 2017/11/16.
 * 代理方法
 */
public class MethodInterceptorTest {

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SampleClass.class);
        enhancer.setCallback(new MethodInterceptor() {
            //o为由CGLib动态生成的代理类实例,method为上文中实体类所调用的被代理的方法引用,objects为参数值列表,methodProxy为生成的代理类对方法的代理引用
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("Before method");
                Object object = methodProxy.invokeSuper(o,objects);
                System.out.println("After method");
                return object;
            }
        });
        SampleClass proxy = (SampleClass) enhancer.create();
        proxy.test3("Hello world!");
    }
}
