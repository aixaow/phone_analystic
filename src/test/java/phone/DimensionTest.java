package phone;

import com.phone.analystic.modle.base.DateDimension;
import com.phone.analystic.modle.base.PlatformDimension;
import com.phone.common.DateEnum;

/**
 * @author axiao
 * @date Create 19:32 2018/9/20 0020
 * @description:  将维度信息输入，获取维度对应的id
 */
public class DimensionTest {
    public static void main(String[] args) {
        PlatformDimension pl = new PlatformDimension("ios");
        DateDimension dt = DateDimension.buildDate(324789342343829L, DateEnum.DAY);
//        int id = aa(dt); //1
    }
}
