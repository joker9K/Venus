package spider;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Created by zhangwt n 2017/11/20.
 * 发送邮件内容实体
 */
@ToString
@Builder
@Data
public class MailContent {

    private String to;//目标邮箱
    private String subject;//邮件标题
    private String content;//邮件内容
    private String affix;//附件地址
    private String affixName;//附件标题
}
