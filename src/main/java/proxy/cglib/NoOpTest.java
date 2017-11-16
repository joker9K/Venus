package proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

/**
 * Created by zhangwt n 2017/11/16.
 * 执行原方法
 */
public class NoOpTest {

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SampleClass.class);
        enhancer.setCallback(NoOp.INSTANCE);
        SampleClass proxy = (SampleClass) enhancer.create();
        System.out.println(proxy.test1("Hello world!"));
    }
}
