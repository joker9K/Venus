package spider;

import lombok.Builder;
import lombok.Data;

/**
 * Created by zhangwt n 2017/11/20.
 */
@Builder
@Data
public class FundDetail {
    private String name;
    private String time;
    private String jzgs;
    private String jzgsbhz;
    private String jzgsbhbfb;

    @Override
    public String toString() {
        return "基金名称:" + name +
                "\n净值估算时间:" + time +
                "\n净值估算:" + jzgs +
                "\n净值估算变化值:" + jzgsbhz +
                "\n净值估算变化百分比:" + jzgsbhbfb;
    }
}
