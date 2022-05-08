package com.ctg.mes.config.common.util;

import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: zhaoyang
 * Date: 2018/8/3/0003
 * Description: ip string 工具类
 */
public class IpStringUtils {

    //ipv6正则表达式，可匹配ipv6和ipv4地址
    private static final String IPV6_PATTERN = "^\\s*((([0-9A-Fa-f]{1,4}:){7}(([0-9A-Fa-f]{1,4})|:))|(([0-9A-Fa-f]{1,4}:){6}(:|((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})|(:[0-9A-Fa-f]{1,4})))|(([0-9A-Fa-f]{1,4}:){5}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){0,1}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){0,2}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){0,3}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:)(:[0-9A-Fa-f]{1,4}){0,4}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(:(:[0-9A-Fa-f]{1,4}){0,5}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})))(%.+)?\\s*$";
    //ipv6正则表达式，去掉了^和$，仅用于removeIpFromRedisInfoLine()
    private static final String IPV6_PATTERN_CONTAIN = "\\s*((([0-9A-Fa-f]{1,4}:){7}(([0-9A-Fa-f]{1,4})|:))|(([0-9A-Fa-f]{1,4}:){6}(:|((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})|(:[0-9A-Fa-f]{1,4})))|(([0-9A-Fa-f]{1,4}:){5}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){0,1}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){0,2}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){0,3}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:)(:[0-9A-Fa-f]{1,4}){0,4}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(:(:[0-9A-Fa-f]{1,4}){0,5}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})))(%.+)?\\s*";
    //ipv4正则表达式，仅可匹配ipv4地址
    private static final String IPV4_PATTERN = "^((25[0-5]|2[0-4]\\d|[0-1]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[0-1]?\\d\\d?)$";
    //域名的正则表达式
    private static final String DOMAIN_NAME_PATTERN = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+.?";
    private static final String OPEN_BRACKET = "[";
    private static final String CLOSE_BRACKET = "]";
    private static final String IPV6_PORT_SUFFIX = "]:";

    public static final String DEFL_PORT_SUFFIX = ":";
    public static final String DEFL_INSTANCE_SPLIT="#";

    /**
     * 分离connUrl中的ip和端口号
     *
     * @param connUrl 连接地址（192.168.0.182:8080 或 [2400:a480:f:80:c3::206]:8080 或 192.168.0.182
     *                或 2400:a480:f:80:c3::206）
     * @return 有端口号则返回 ip 和 端口号 的string数组
     * 无端口号则返回只有 ip 的string数组
     */
    public static String[] splitIpAndPort(String connUrl) {
        if (!Pattern.matches(IPV6_PATTERN, connUrl)) {
            // ip地址+端口号
            if (connUrl.contains(CLOSE_BRACKET)) {
                //ipv6地址+端口号
                String[] res = new String[2];
                String[] str = connUrl.split(IPV6_PORT_SUFFIX);
                String port = str[1];
                String ip = str[0].substring(1); // 去掉'['
                res[0] = ip;
                res[1] = port;
                return res;
            } else {
                // 其他host地址+端口号
                int idx = connUrl.lastIndexOf(DEFL_PORT_SUFFIX);
                String host = idx != -1 ? connUrl.substring(0, idx) : connUrl;
                String port = idx != -1 ? connUrl.substring(idx + 1) : "";
                return new String[]{host, port};
            }
        } else {
            // ip地址,无端口号
            return new String[]{connUrl, ""};
        }

    }

    /**
     * 构建ip string
     *
     * @param ip ipv4或者ipv6地址
     * @return ipv4 直接返回
     * ipv6 两端加上"[]" 例：[2400:a480:f:80:c3::74]
     */
    public static String constructIpString(String ip) {
        if (Pattern.matches(IPV6_PATTERN, ip) && !Pattern.matches(IPV4_PATTERN, ip)) {
            // ipv6地址
            return OPEN_BRACKET + ip + CLOSE_BRACKET;
        } else {
            return ip;
        }
    }

    /**
     * 判断url中是否是ipv6地址的url
     *
     * @param url   url地址
     * @return  boolean
     */
    public static boolean isUrlContainIpv6Address(String url) {
        return url.contains(OPEN_BRACKET) && url.contains(CLOSE_BRACKET);
    }

    /**
     * 移除redis info中replication中的slave中的ip地址
     * 防止ipv6地址中的冒号干扰com.ctg.itrdc.cache.toolkits.utils.RedisInfoUtils#filterInfoLine(java.lang.String)方法
     *
     * @param line redis info
     * @return 删除ip地址后的string
     * @author: Xiongzy
     * @date: 2018/8/17/0017
     */
    public static String removeIpFromRedisInfoLine(String line) {
        return line.replaceAll(IPV6_PATTERN_CONTAIN, "");
    }

    /**
     * 判断游标地址或者url中的ip地址是否是ipv6地址（e.g. [2400:a480:f:80:c3::74]:8080:1234）
     *
     * @param url [2400:a480:f:80:c3::74]:8080:1234 或 10.142.90.30:6379:1234
     *            [2400:a480:f:80:c3::74]:8080 或 10.142.90.30:6379
     * @return 其中的ip地址是ipv6地址 返回true
     * ipv4地址 返回false
     */
    public static boolean isCursorContainIpv6Address(String url) {
        return url.contains(IPV6_PORT_SUFFIX);
    }

    /**
     * 将redis.conf文件中的slaveOf属性转成url（ip：port）
     *
     * @param slaveOfInfo redis.conf中的slaveof属性 "192.168.0.1 22222" 或 "2400:a480:f:80:c3::74 8080"
     * @return 192.168.0.1:22222 或 [2400:a480:f:80:c3::74]:8080
     */
    public static String convertRedisSlaveInfoToUrl(String slaveOfInfo) {
        String[] hostAndPort = slaveOfInfo.trim().split("\\s+");
        String host = hostAndPort[0];
        String port = hostAndPort[1];
        if (Pattern.matches(IPV6_PATTERN, host) && !Pattern.matches(IPV4_PATTERN, host)) {
            //ipv6地址
            host = OPEN_BRACKET + host + CLOSE_BRACKET;
        }
        return host + DEFL_PORT_SUFFIX + port;
    }

    /**
     * 将url转成redis中的salve属性
     *
     * @param ip 192.168.0.1:22222 或 [2400:a480:f:80:c3::74]:8080
     * @return redis.conf中的slaveof属性 "192.168.0.1 22222" 或 "2400:a480:f:80:c3::74 8080"
     */
    public static String convertUrlToRedisSlaveInfo(String ip,String port) {
        //String[] hostAndPort = splitIpAndPort(url);
        return ip + " " + port;
    }

    /**
     * 得到ip port code
     *
     * @param IpPortCode ip:code 或 ip:port:code
     *                   192.168.0.1:1234 或 2400:a480:f:80:c3::74:1234
     *                   192.168.0.1:1234:1234 或 [2400:a480:f:80:c3::74]:8080:1234
     * @return ip:code 返回 {ip，code}
     *          ip：port：code 返回 {ip，port，code}
     */
    public static String[] splitIpPortAndCode(String IpPortCode) {
        int index = IpPortCode.lastIndexOf(DEFL_PORT_SUFFIX);
        String ipPort = index == -1 ? IpPortCode : IpPortCode.substring(0, index);
        String code = index == -1 ? "" : IpPortCode.substring(index + 1);
        String[] ipAndPort = splitIpAndPort(ipPort);
        return StringUtils.isEmpty(ipAndPort[1]) ? new String[]{ipAndPort[0], code} : new String[]{ipAndPort[0], ipAndPort[1], code};

    }

    /**
     * 判断host地址是否是ip地址或者域名
     *
     * @param host host地址
     * @return  boolean
     */
    public static boolean isLegalHostAddress(String host) {
        return Pattern.matches(IPV6_PATTERN, host) || Pattern.matches(DOMAIN_NAME_PATTERN, host);
    }

    /**
     * 判断ip地址是否是ipv6地址
     * @param ip ip地址
     * @return boolean true ipv6地址
     */
    public static boolean isIpv6Address(String ip) {
        return Pattern.matches(IPV6_PATTERN, ip) && !Pattern.matches(IPV4_PATTERN, ip);
    }
    public static boolean isIp(String ip){
        return Pattern.matches(IPV6_PATTERN, ip)||Pattern.matches(IPV4_PATTERN, ip);
    }

    /**
     * 判断ipv6地址是否相等
     * @param ip1 ip地址
     * @param ip2 ip地址
     * @return
     */
    public static boolean isEqualWithIpv6Address(String ip1, String ip2) throws UnknownHostException {
        String ipString1 = InetAddress.getByName(ip1).getHostAddress();
        String ipString2 = InetAddress.getByName(ip2).getHostAddress();
        return ipString1.equals(ipString2);
    }
    public static  void main(String args[]){
        System.out.println(isIpv6Address("10.224.64.0/20"));
    }

}