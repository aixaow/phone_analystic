package com.phone.etl.ip;

import com.phone.etl.IpUtil;

import java.io.*;
import java.util.LinkedList;

/**
 * @author axiao
 * @date Create 15:25 2018/9/30 0030
 * @description: 二分查找
 */
public class IPsearch {

    public static long ipToLong(String strIp) {
        String[] ip = strIp.split("\\.");
        long res = (Long.valueOf(ip[0]) << 24) + (Long.valueOf(ip[1]) << 16)
                + (Long.valueOf(ip[2]) << 8) + Long.valueOf(ip[3]);
        return res;
    }

    public static IPBean[] getIpBeans() {
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            File file = new File(IpUtil.class.getResource("/iprule").getPath());
            inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String line;
            LinkedList<IPBean> ipBeanList = new LinkedList<IPBean>();
            while ((line = reader.readLine()) != null) {
                String[] tmp = line.split(",");
                ipBeanList.add(new IPBean(ipToLong(tmp[0]), ipToLong(tmp[1]), tmp[2]));
            }
            IPBean[] ipBeans = ipBeanList.toArray(new IPBean[] {});
            return ipBeans;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
                inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static IPBean getCodeByIp(String ip) {
        IPBean[] ipBeans = getIpBeans();
        if (ipBeans == null || ipBeans.length == 0) {
            return null;
        }
        long iplong = ipToLong(ip);
        if (iplong < ipBeans[0].getBegin() || iplong > ipBeans[ipBeans.length - 1].getEnd()) {
            return null;
        }
        int left = 0;
        int right = ipBeans.length - 1;
        int mid = (left + right) / 2;
        while (left <= right) {
            if (iplong < ipBeans[mid].getBegin()) {
                right = mid - 1;
            }
            if (iplong > ipBeans[mid].getBegin()) {
                left = mid + 1;
            }
            if (iplong >= ipBeans[mid].getBegin() && iplong <= ipBeans[mid].getEnd()) {
                return ipBeans[mid];
            }
            mid = (left + right) / 2;
        }
        return null;
    }
    public static class IPBean {
        private long begin;
        private long end;
        private String code;

        public IPBean() {
        }

        public long getBegin() {
            return begin;
        }

        public void setBegin(long begin) {
            this.begin = begin;
        }

        public long getEnd() {
            return end;
        }

        public void setEnd(long end) {
            this.end = end;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public IPBean(long begin, long end, String code) {
            this.begin = begin;
            this.end = end;
            this.code = code;
        }

        @Override
        public String toString() {
            return "IPBean [begin=" + begin + ", end=" + end + ", code=" + code + "]";
        }

    }

    public static void main(String[] args) {
        IPBean ipBean = getCodeByIp("20.1.1.255");
        System.out.println(ipBean);
    }
}
