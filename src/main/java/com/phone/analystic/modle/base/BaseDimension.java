package com.phone.analystic.modle.base;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author axiao
 * @date Create 22:44 2018/9/19 0019
 * @description: 所有基础类的顶级父类
 *
 * 根据日期，操作系统和浏览器查询 新增用户的数量的sql语句
 *
 * SELECT
 * SUM(sdb.new_install_users)
 * FROM stats_device_browser sdb
 * LEFT JOIN dimension_date dd
 * ON dd.id = sdb.date_dimension_id
 * LEFT JOIN dimension_platform dp
 * ON sdb.platform_dimension_id = dp.id
 * LEFT JOIN dimension_browser db
 * ON sdb.browser_dimension_id = db.id
 * WHERE dd.calendar = "2018-09-20"
 * and dp.platform_name = "ios"
 * and db.browser_name = "IE"
 * #GROUP BY sdb.date_dimension_id,sdb.platform_dimension_id,sdb.browser_dimension_id
 */
public abstract class BaseDimension implements WritableComparable<BaseDimension> {

}
