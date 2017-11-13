package test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author zhangwt
 * @date 2017/9/27 11:31.
 */
public class Test2 {


    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(URLEncoder.encode("https://wx.qq.com/?&lang=zh_CN", "UTF-8"));
    }
}
