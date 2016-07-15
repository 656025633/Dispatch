package com.winsion.dispatch.bean;

import java.io.Serializable;

/**
 * Created by Mr.ZCM on 2016/7/12.
 * QQ:656025633
 * Company:winsion
 * Version:1.0
 * explain:
 */
public class RelateUserBean extends  OneKeyCallBean implements Serializable {

        private String organizationId;
        private String areaId;
        private String areaName;
        private String ssId;
        private boolean onLine;
        private String roleNames;


        public String getOrganizationId() {
            return organizationId;
        }

        public void setOrganizationId(String organizationId) {
            this.organizationId = organizationId;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getSsId() {
            return ssId;
        }

        public void setSsId(String ssId) {
            this.ssId = ssId;
        }

        public boolean isOnLine() {
            return onLine;
        }

        public void setOnLine(boolean onLine) {
            this.onLine = onLine;
        }

        public String getRoleNames() {
            return roleNames;
        }

        public void setRoleNames(String roleNames) {
            this.roleNames = roleNames;
        }
    }

