package com.phone.analystic.mr.nu;

import com.phone.analystic.modle.StatsUserDimension;
import com.phone.analystic.modle.value.map.TimeOutputValue;
import com.phone.analystic.modle.value.reduce.OutputWritable;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * @author axiao
 * @date Create 18:02 2018/9/20 0020
 * @description:
 */
public class NewUserReducer extends Reducer<StatsUserDimension,TimeOutputValue,
        StatsUserDimension,OutputWritable> {
    //get维度的id的方法
    //

    //2018-09-20 ios IE 8.0 new_user 3688
    //2018-09-20 ios IE 9.0 new_user 12
    //2018-09-20 ios IE 8.0 houryly_user 12
    //2018-09-20 ios IE 9.0 houryly_user 500


}
