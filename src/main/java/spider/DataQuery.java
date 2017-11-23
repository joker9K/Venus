package spider;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.RedisPool;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by zhangwt n 2017/11/21.
 */
@Slf4j
public class DataQuery {

    public static void addFundCode(String name, String code) throws Exception {
        if(StringUtils.isBlank(name) || StringUtils.isBlank(code)){
            throw new Exception("基金持有人或基金代码不能为空");
        }
        Jedis jedis = RedisPool.getJedis();
        Map<String,String> map =jedis.hgetAll("fundCode");
        if(map == null){
            Set<String> set = new HashSet<>(Collections.singleton(code));
            map = new HashMap<>();
            FundPlan plan = new FundPlan();
            plan.setCodeSet(set);
            map.put(name, JSON.toJSONString(plan));
        }else if(StringUtils.isBlank(map.get(name))) {
            FundPlan plan = new FundPlan();
            plan.setCodeSet(new HashSet<>(Collections.singleton(code)));
            map.put(name,JSON.toJSONString(plan));
        }else {
            String codeStr = map.get(name);
            FundPlan plan = JSON.parseObject(codeStr,FundPlan.class);
            plan.getCodeSet().add(code);
            map.put(name,JSON.toJSONString(plan));
        }
        String reult = jedis.hmset("fundCode",map);
        System.out.println(reult);
    }


    public static  void addFundCodes(String name, String... codes){
        if(codes.length > 0){
            for (String code:codes){
                try {
                    addFundCode(name,code);
                } catch (Exception e) {
                    log.error("基金持有人:{},基金代码:{}",name,code,e);
                }
            }
        }
    }

    public static void addFundTime(String name,int hour,int minute,int second) throws Exception {
        if(StringUtils.isBlank(name)){
            throw new Exception("基金持有人不能为空");
        }
        if(hour>23 || hour<0){
            throw new Exception("时格式不合法");
        }
        if(minute>59 || minute<0){
            throw new Exception("分格式不合法");
        }
        if(second>59 || second<0){
            throw new Exception("秒格式不合法");
        }
        Jedis jedis = RedisPool.getJedis();
        Map<String,String> map =jedis.hgetAll("fundCode");
        if(map == null){
            List<Time> list = new ArrayList<>(Collections.singletonList(new Time(hour,minute,second)));
            map = new HashMap<>();
            FundPlan plan = new FundPlan();
            plan.setDateList(list);
            map.put(name, JSON.toJSONString(plan));
        }else if(StringUtils.isBlank(map.get(name))) {
            FundPlan plan = new FundPlan();
            plan.setDateList(Collections.singletonList(new Time(hour,minute,second)));
            map.put(name,JSON.toJSONString(plan));
        }else {
            String codeStr = map.get(name);
            FundPlan plan = JSON.parseObject(codeStr,FundPlan.class);
            plan.getDateList().add(new Time(hour,minute,second));
            map.put(name,JSON.toJSONString(plan));
        }
        String reult = jedis.hmset("fundCode",map);
        System.out.println(reult);
    }

    public static FundPlan findFundPlan(String name){
        Jedis jedis = RedisPool.getJedis();
        Map<String,String> map =jedis.hgetAll("fundCode");
        if(map == null){
            return null;
        }
        String codeStr = map.get(name);
        if(StringUtils.isBlank(codeStr)){
            return null;
        }
        return JSON.parseObject(codeStr,FundPlan.class);
    }

    public static void addFundEmail(String name,String email) throws Exception {
        if(StringUtils.isBlank(name) || StringUtils.isBlank(email)){
            throw new Exception("基金持有人或绑定邮箱不能为空");
        }
        Jedis jedis = RedisPool.getJedis();
        Map<String,String> map =jedis.hgetAll("fundCode");
        if(map == null){
            map = new HashMap<>();
            FundPlan plan = new FundPlan();
            plan.setEmail(email);
            map.put(name, JSON.toJSONString(plan));
        }else if(StringUtils.isBlank(map.get(name))) {
            FundPlan plan = new FundPlan();
            plan.setEmail(email);
            map.put(name,JSON.toJSONString(plan));
        }else {
            String codeStr = map.get(name);
            FundPlan plan = JSON.parseObject(codeStr,FundPlan.class);
            plan.setEmail(email);
            map.put(name,JSON.toJSONString(plan));
        }
        String reult = jedis.hmset("fundCode",map);
        System.out.println(reult);
    }


    public static void main(String[] args) {
//        Jedis jedis = RedisPool.getJedis();002168 110022 003299 165312
//        Map<String, String> map = new HashMap<>();001617
//        List<String> list = new ArrayList<>(Arrays.asList("002168","110022","003299","165312"));
//        System.out.println(JSON.toJSONString(list));
//        String content = JSON.toJSONString(list);
////        List<String> list1 = JSON.parseArray(content).toJavaList(String.class);
//        map.put("马苋飞",content);
//        jedis.hmset("fundCode",map);

        try {
            addFundCodes("张文韬","001548","001594","001631","001938");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(1);

    }
}
