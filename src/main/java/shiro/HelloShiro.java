package shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

/**
 * Created by zhangwt n 2017/11/23.
 */
@Slf4j
public class HelloShiro {

    public static void main(String[] args) {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        //登录
        UsernamePasswordToken token = new UsernamePasswordToken("shiro","1994");

        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            log.info("登录失败");
            return;
        }
        log.info("登录成功！Hello "+subject.getPrincipal());
        //注销
        subject.logout();

    }
}
