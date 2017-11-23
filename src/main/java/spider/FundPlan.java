package spider;

import lombok.Data;

import java.util.*;

/**
 * Created by zhangwt n 2017/11/21.
 */
@Data
public class FundPlan {
    private String email;
    private List<Time> dateList = new ArrayList<>(Collections.singletonList(new Time(14, 40, 0)));//推送时间,默认2点40
    private Set<String> codeSet = new HashSet<>(); //基金代码
}
