package proxy.proxychain;

import java.lang.reflect.Method;

/**
 * Created by zhangwt n 2017/11/21.
 */
public class BeforeProxy extends AbstractProxy {

    @Override
    public void before(Class<?> cls, Method method, Object[] params) {
        System.out.println("Before");
    }
}
