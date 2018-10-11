1、编写事件的维度类和修改操作基础维度服务
2、编写udf函数

create function phone_event as 'com.phone.analystic.hive.EventDimensionUdf' using jar 'hdfs://hadoop01:9000/phone/udfjars/phone_analystic-1.0.jar';
create function phone_date as 'com.phone.analystic.hive.DateDimensionUdf' using jar 'hdfs://hadoop01:9000/phone/udfjars/phone_analystic-1.0.jar';
create function phone_platform as 'com.phone.analystic.hive.PlatformDimensionUdf' using jar 'hdfs://hadoop01:9000/phone/udfjars/phone_analystic-1.0.jar';


创键元数据对应的临时表：
create external table if not exists phone_tmp(
ver string,
s_time string,
en string,
u_ud string,
u_mid string,
u_sd string,
c_time string,
l string,
b_iev string,
b_rst string,
p_url string,
p_ref string,
tt string,
pl string,
ip String,
oid String,
`on` String,
cua String,
cut String,
pt String,
ca String,
ac String,
kv_ String,
du String,
browserName String,
browserVersion String,
osName String,
osVersion String,
country String,
province String,
city string
)
partitioned by(month string,day string)
;

//加载数据 flume落地的时候可以指定成orc
//这里可以直接load
//load完了之后-会自动把INPATH下面的源数据删掉-其实就是将INPATH下面的进行数据移动
load data inpath '/ods/09/18' into table phone_tmp partition(month=09,day=18);

3、创建hive表：
create external table if not exists phone(
ver string,
s_time string,
en string,
u_ud string,
u_mid string,
u_sd string,
c_time string,
l string,
b_iev string,
b_rst string,
p_url string,
p_ref string,
tt string,
pl string,
ip String,
oid String,
`on` String,
cua String,
cut String,
pt String,
ca String,
ac String,
kv_ String,
du String,
browserName String,
browserVersion String,
osName String,
osVersion String,
country String,
province String,
city string
)
partitioned by(month string,day string)
stored as orc
;


//导入数据
set hive.exec.local.mode=true;
from phone_tmp
insert into phone partition(month=09,day=18)
select
ver,
s_time,
en,
u_ud,
u_mid,
u_sd,
c_time,
l,
b_iev,
b_rst,
p_url,
p_ref,
tt,
pl,
ip,
oid,
`on`,
cua,
cut,
pt,
ca,
ac,
kv_,
du,
browserName,
browserVersion,
osName,
osVersion,
country,
province,
city
where month = 9
and day = 18
;

在hive中创建和mysql最终结果便一样的临时表：
CREATE TABLE if not exists `stats_event` (
  `platform_dimension_id` int,
  `date_dimension_id` int,
  `event_dimension_id` int,
  `times` int,
  `created` String
)
;

from_unixtime(cast(p.s_time/1000 as bigint),"yyyy-MM-dd") as dt,
将时间戳转换成日期-单位是秒-所以要/1000
cast进行一下转换
dt传给dtudf得到id

如果出现数据倾斜-就会在groupby这里--设置优化set hive.groupby.skewindata=true;

语句：
set hive.exec.mode.local.auto=true;
set hive.groupby.skewindata=true;
from(
select
from_unixtime(cast(p.s_time/1000 as bigint),"yyyy-MM-dd") as dt,
p.pl as pl,
p.ca as ca,
p.ac as ac,
count(*) as ct
from phone p
where p.month = 9
and p.day = 18
and en = 'e_e'
group by from_unixtime(cast(p.s_time/1000 as bigint),"yyyy-MM-dd"),p.pl,p.ca,p.ac
) as tmp
insert overwrite table stats_event
select phone_platform(pl),phone_date(dt),phone_event(ca,ac),ct,dt
;

扩展维度（增加all维度-为了计算不同事件类型的事件的数量--不用级联查询)：
set hive.exec.mode.local.auto=true;
set hive.groupby.skewindata=true;
with tmp as(
select
from_unixtime(cast(l.s_time/1000 as bigint),"yyyy-MM-dd") as dt,
l.pl as pl,
l.ca as ca,
l.ac as ac
from phone l
where month = 9
and day = 18
and l.en = 'e_e'
and l.s_time <> 'null'
)
from (
select dt as dt,pl as pl,ca as ca,ac as ac,count(1) as ct from tmp group by dt,pl,ca,ac union all
select dt as dt,pl as pl,ca as ca,'all' as ac,count(1) as ct from tmp group by dt,pl,ca union all
select dt as dt,'all' as pl,ca as ca,ac as ac,count(1) as ct from tmp group by dt,ca,ac union all
select dt as dt,'all' as pl,ca as ca,'all' as ac,count(1) as ct from tmp group by dt,ca
) as tmp1
insert overwrite table stats_event
select phone_date(dt),phone_platform(pl),phone_event(ca,ac),sum(ct),'2018-09-18'
group by pl,dt,ca,ac
;




3	1	1
3	1	3
3	3	1
3	3	3

sqoop export --connect jdbc:mysql://hadoop01:3306/myresult \
--username root --password root -m 1 \
--table stats_event --export-dir hdfs://hadoop01:9000/hive/log_phone.db/stats_event/* \
--input-fields-terminated-by "\\01" --update-mode allowinsert \
--update-key date_dimension_id,platform_dimension_id,event_dimension_id \
;




1	3	订单事件|订单产生	1	2018-09-19
1	3	订单事件|订单支付	1	2018-09-19
1	3	点击|赞	200	2018-09-19