package proxy;

/**
 * Created by zhangwt n 2017/11/13.
 * 静态代理
 */
public class StaticProxy implements Hello{

    private Hello hello;

    public StaticProxy() {
        this.hello = new HelloImpl();
    }

    @Override
    public void say(String name) {
        before();
        hello.say(name);
        after();
    }


    private void before(){
        System.out.println("Before!");
    }

    private void after(){
        System.out.println("After");
    }

    public static void main(String[] args) {
        Hello hello = new StaticProxy();
        hello.say("Jack");
    }

}
