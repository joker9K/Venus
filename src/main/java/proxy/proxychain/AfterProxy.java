package proxy.proxychain;

import java.lang.reflect.Method;

/**
 * Created by zhangwt n 2017/11/21.
 */
public class AfterProxy extends AbstractProxy{

    @Override
    public void after(Class<?> cls, Method method, Object[] params) {
        System.out.println("After");
    }
}
