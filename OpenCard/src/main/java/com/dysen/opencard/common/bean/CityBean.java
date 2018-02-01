package com.dysen.opencard.common.bean;

import java.util.List;

/**
 * Created by dysen on 1/22/2018.
 */

public class CityBean {

    private List<CitylistBean> citylist;

    public List<CitylistBean> getCitylist() {
        return citylist;
    }

    public void setCitylist(List<CitylistBean> citylist) {
        this.citylist = citylist;
    }

    public static class CitylistBean {
        /**
         * p : 北京
         * c : [{"n":"东城区"},{"n":"西城区"},{"n":"崇文区"},{"n":"宣武区"},{"n":"朝阳区"},{"n":"丰台区"},{"n":"石景山区"},{"n":"海淀区"},{"n":"门头沟区"},{"n":"房山区"},{"n":"通州区"},{"n":"顺义区"},{"n":"昌平区"},{"n":"大兴区"},{"n":"平谷区"},{"n":"怀柔区"},{"n":"密云县"},{"n":"延庆县"}]
         */

        private String p;
        private List<CBean> c;

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public List<CBean> getC() {
            return c;
        }

        public void setC(List<CBean> c) {
            this.c = c;
        }

        public static class CBean {
            /**
             * n : 东城区
             */

            private String n;
            private List<ABean> a;

            public String getN() {
                return n;
            }

            public void setN(String n) {
                this.n = n;
            }
            public List<ABean> getA() {
                return a;
            }

            public CBean setA(List<ABean> a) {
                this.a = a;
                return this;
            }
            public class ABean{
                /**
                 * a : 红安县
                 */
                private String n;

                public String getN() {
                    return n;
                }

                public ABean setN(String n) {
                    this.n = n;
                    return this;
                }
            }
        }
    }
}
