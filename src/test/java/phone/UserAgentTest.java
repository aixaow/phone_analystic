package phone;

import com.phone.etl.ip.UserAgentUtil;

import static com.phone.etl.ip.UserAgentUtil.parserUserAgent;

/**
 * @author axiao
 * @date Create 20:40 2018/9/18 0018
 * @description:
 */
public class UserAgentTest {
    public static void main(String[] args) {
         String user = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";
         UserAgentUtil.UserAgentInfo res = parserUserAgent(user);
        System.out.println(res);

    }
}
