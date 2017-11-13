package proxy;

/**
 * Created by zhangwt n 2017/11/13.
 */
public class HelloImpl implements Hello {


    @Override
    public void say(String name) {
        System.out.println("Hello! "+name);
    }
}
