

from(
select
from_unixtime(cast(l.s_time/1000 as bigint),"yyyy-MM-dd") as dt,
l.pl as pl,
count(distinct l.u_sd) as session,
from phone l
where l.month = 09
and day = 18
and l.u_sd is not null
and l.u_sd <> 'null'
group by from_unixtime(cast(l.s_time/1000 as bigint),"yyyy-MM-dd"),pl
) as tmp
insert overwrite table stats_user
select session(session),date_dimension_id(dt),platform_dimension_id(pl)
group by pl
;

sqoop export --connect jdbc:mysql://hadoop01:3306/result \
--username root --password root \
--table stats_order --export-dir /hive/stats_user/* \
--input-fields-terminated-by "\\01" --update-mode allowinsert \
--update-key `date_dimension_id``platform_dimension_id`\
--columns `date_dimension_id``platform_dimension_id``sessions``sessions_length` \
;




