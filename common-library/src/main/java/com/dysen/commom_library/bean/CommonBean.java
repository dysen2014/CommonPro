package com.dysen.commom_library.bean;

import java.io.Serializable;

/**
 * Created by dysen on 1/28/2018.
 */

public class CommonBean {
    /**
     * 身份证 bean
     */
    public static class IDCheckResult implements Serializable {
        public final boolean pass;

        public final String id;

        public final String name;

        public final String custNo;

        public final Integer applyType;

        public boolean isPass() {
            return pass;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCustNo() {
            return custNo;
        }

        public Integer getApplyType() {
            return applyType;
        }

        public IDCheckResult(boolean pass, String id, String name, String custNo, Integer applyType) {
            this.pass = pass;
            this.id = id;
            this.name = name;
            this.custNo = custNo;
            this.applyType = applyType;
        }
    }
    public static class CountryBean{
        String id, name;

        public String getId() {
            return id;
        }

        public CountryBean setId(String id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public CountryBean setName(String name) {
            this.name = name;
            return this;
        }
    }
}
