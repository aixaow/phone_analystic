package phone;

import com.phone.etl.ip.LogUtil;

import java.util.Map;

/**
 * @author axiao
 * @date Create 8:59 2018/9/19 0019
 * @description:
 */
public class LogTest {
    public static void main(String[] args) {
        String log = "114.61.94.253^A1531110990.123^Ahh^A/BCImg.gif?en=e_l&ver=1&pl=website&sdk=js&u_ud=27F69684-BBE3-42FA-AA62-71F98E208444&u_mid=Aidon&u_sd=38F66FBB-C6D6-4C1C-8E05-72C31675C00A&c_time=1449917532123&l=zh-CN&b_iev=Mozilla%2F5.0%20(Windows%20NT%206.1%3B%20WOW64)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Chrome%2F46.0.2490.71%20Safari%2F537.36&b_rst=1280*768";
        Map map = LogUtil.parserLog(log);
        System.out.println(map);
    }
}
