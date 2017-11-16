package proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;

/**
 * Created by zhangwt n 2017/11/16.
 * 延迟加载
 */
public class LazyLoaderTest {

    public static void main(String[] args) {
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(PropertyBean.class);
        PropertyBean bean =(PropertyBean)enhancer.create(PropertyBean.class, new LazyLoader() {
            @Override
            public Object loadObject() throws Exception {
                PropertyBean bean=new PropertyBean();
                bean.setValue("OK");
                return bean;
            }
        });

        System.out.println(bean.value);
        //在CGLib的实现中只要去访问该对象内属性的getter方法，就会自动触发代理类回调
        System.out.println(bean.getValue());
        System.out.println(bean.value);

    }
}
