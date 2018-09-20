package com.phone.analystic.modle;

import com.phone.analystic.modle.base.BaseDimension;
import com.phone.analystic.modle.base.BrowserDimension;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * @author axiao
 * @date Create 17:27 2018/9/20 0020
 * @description:
 *  可以用于用户模块和浏览器模块的map和reduce阶段输出的key的类型
 */
public class StatsUserDimension extends StatsCommonDimension{

    /**
     * 公共维度---时间、平台、kpi
     */
    private StatsCommonDimension statsCommonDimension = new StatsCommonDimension();
    /**
     * 浏览器维度
     */
    private BrowserDimension browserDimension = new BrowserDimension();


    public StatsUserDimension(){}

    public StatsUserDimension(StatsCommonDimension statsCommonDimension, BrowserDimension browserDimension) {
        this.statsCommonDimension = statsCommonDimension;
        this.browserDimension = browserDimension;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        this.statsCommonDimension.write(out);
        this.browserDimension.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.statsCommonDimension.readFields(in);
        this.browserDimension.readFields(in);
    }

    @Override
    public int compareTo(BaseDimension o) {
        if(this == o){
            return 0;
        }

        StatsUserDimension other = (StatsUserDimension) o;
        int tmp = this.statsCommonDimension.compareTo(other.statsCommonDimension);
        if(tmp != 0){
            return tmp;
        }
        return this.browserDimension.compareTo(browserDimension);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatsUserDimension that = (StatsUserDimension) o;
        return Objects.equals(statsCommonDimension, that.statsCommonDimension) &&
                Objects.equals(browserDimension, that.browserDimension);
    }

    @Override
    public int hashCode() {

        return Objects.hash(statsCommonDimension, browserDimension);
    }

    public StatsCommonDimension getStatsCommonDimension() {
        return statsCommonDimension;
    }

    public void setStatsCommonDimension(StatsCommonDimension statsCommonDimension) {
        this.statsCommonDimension = statsCommonDimension;
    }

    public BrowserDimension getBrowserDimension() {
        return browserDimension;
    }

    public void setBrowserDimension(BrowserDimension browserDimension) {
        this.browserDimension = browserDimension;
    }


}
