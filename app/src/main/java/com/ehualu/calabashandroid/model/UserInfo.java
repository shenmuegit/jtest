package com.ehualu.calabashandroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserInfo {

    /**
     * ocs : {"meta":{"status":"ok","statuscode":100,"message":"OK","totalitems":"","itemsperpage":""},"data":{"enabled":true,"storageLocation":"/usr/share/nginx/html/ilake/data/18211185976","id":"18211185976","lastLogin":1575350983000,"backend":"Database","subadmin":["xin2","使用体验"],"quota":{"free":5240194868,"used":5497223372,"total":10737418240,"relative":51.2,"quota":10737418240,"freeGlacier":97038006437,"usedGlacier":10336175963,"totalGlacier":107374182400,"relativeGlacier":9.63,"quotaGlacier":107374182400},"email":"123@qq.com","phone":"18211185976","address":"wwwxsx","website":null,"twitter":null,"groups":["xin2","使用体验"],"language":"zh_CN","locale":"","backendCapabilities":{"setDisplayName":true,"setPassword":true},"display-name":"18211185976","is_cache":"false"}}
     */

    private OcsBean ocs;

    public OcsBean getOcs() {
        return ocs;
    }

    public void setOcs(OcsBean ocs) {
        this.ocs = ocs;
    }

    public static class OcsBean {
        /**
         * meta : {"status":"ok","statuscode":100,"message":"OK","totalitems":"","itemsperpage":""}
         * data : {"enabled":true,"storageLocation":"/usr/share/nginx/html/ilake/data/18211185976","id":"18211185976","lastLogin":1575350983000,"backend":"Database","subadmin":["xin2","使用体验"],"quota":{"free":5240194868,"used":5497223372,"total":10737418240,"relative":51.2,"quota":10737418240,"freeGlacier":97038006437,"usedGlacier":10336175963,"totalGlacier":107374182400,"relativeGlacier":9.63,"quotaGlacier":107374182400},"email":"123@qq.com","phone":"18211185976","address":"wwwxsx","website":null,"twitter":null,"groups":["xin2","使用体验"],"language":"zh_CN","locale":"","backendCapabilities":{"setDisplayName":true,"setPassword":true},"display-name":"18211185976","is_cache":"false"}
         */

        private MetaBean meta;
        private DataBean data;

        public MetaBean getMeta() {
            return meta;
        }

        public void setMeta(MetaBean meta) {
            this.meta = meta;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class MetaBean {
            /**
             * status : ok
             * statuscode : 100
             * message : OK
             * totalitems :
             * itemsperpage :
             */

            private String status;
            private int statuscode;
            private String message;
            private String totalitems;
            private String itemsperpage;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getStatuscode() {
                return statuscode;
            }

            public void setStatuscode(int statuscode) {
                this.statuscode = statuscode;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getTotalitems() {
                return totalitems;
            }

            public void setTotalitems(String totalitems) {
                this.totalitems = totalitems;
            }

            public String getItemsperpage() {
                return itemsperpage;
            }

            public void setItemsperpage(String itemsperpage) {
                this.itemsperpage = itemsperpage;
            }
        }

        public static class DataBean {
            /**
             * enabled : true
             * storageLocation : /usr/share/nginx/html/ilake/data/18211185976
             * id : 18211185976
             * lastLogin : 1575350983000
             * backend : Database
             * subadmin : ["xin2","使用体验"]
             * quota : {"free":5240194868,"used":5497223372,"total":10737418240,"relative":51.2,"quota":10737418240,"freeGlacier":97038006437,"usedGlacier":10336175963,"totalGlacier":107374182400,"relativeGlacier":9.63,"quotaGlacier":107374182400}
             * email : 123@qq.com
             * phone : 18211185976
             * address : wwwxsx
             * website : null
             * twitter : null
             * groups : ["xin2","使用体验"]
             * language : zh_CN
             * locale :
             * backendCapabilities : {"setDisplayName":true,"setPassword":true}
             * display-name : 18211185976
             * is_cache : false
             */

            private boolean enabled;
            private String storageLocation;
            private String id;
            private long lastLogin;
            private String backend;
            private QuotaBean quota;
            private String email;
            private String phone;
            private String address;
            private Object website;
            private Object twitter;
            private String language;
            private String locale;
            private BackendCapabilitiesBean backendCapabilities;
            @SerializedName("display-name")
            private String displayname;
            private String is_cache;
            private List<String> subadmin;
            private List<String> groups;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getStorageLocation() {
                return storageLocation;
            }

            public void setStorageLocation(String storageLocation) {
                this.storageLocation = storageLocation;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public long getLastLogin() {
                return lastLogin;
            }

            public void setLastLogin(long lastLogin) {
                this.lastLogin = lastLogin;
            }

            public String getBackend() {
                return backend;
            }

            public void setBackend(String backend) {
                this.backend = backend;
            }

            public QuotaBean getQuota() {
                return quota;
            }

            public void setQuota(QuotaBean quota) {
                this.quota = quota;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public Object getWebsite() {
                return website;
            }

            public void setWebsite(Object website) {
                this.website = website;
            }

            public Object getTwitter() {
                return twitter;
            }

            public void setTwitter(Object twitter) {
                this.twitter = twitter;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }

            public String getLocale() {
                return locale;
            }

            public void setLocale(String locale) {
                this.locale = locale;
            }

            public BackendCapabilitiesBean getBackendCapabilities() {
                return backendCapabilities;
            }

            public void setBackendCapabilities(BackendCapabilitiesBean backendCapabilities) {
                this.backendCapabilities = backendCapabilities;
            }

            public String getDisplayname() {
                return displayname;
            }

            public void setDisplayname(String displayname) {
                this.displayname = displayname;
            }

            public String getIs_cache() {
                return is_cache;
            }

            public void setIs_cache(String is_cache) {
                this.is_cache = is_cache;
            }

            public List<String> getSubadmin() {
                return subadmin;
            }

            public void setSubadmin(List<String> subadmin) {
                this.subadmin = subadmin;
            }

            public List<String> getGroups() {
                return groups;
            }

            public void setGroups(List<String> groups) {
                this.groups = groups;
            }

            public static class QuotaBean {
                /**
                 * free : 5240194868
                 * used : 5497223372
                 * total : 10737418240
                 * relative : 51.2
                 * quota : 10737418240
                 * freeGlacier : 97038006437
                 * usedGlacier : 10336175963
                 * totalGlacier : 107374182400
                 * relativeGlacier : 9.63
                 * quotaGlacier : 107374182400
                 */

                private long free;
                private long used;
                private long total;
                private double relative;
                private long quota;
                private long freeGlacier;
                private long usedGlacier;
                private long totalGlacier;
                private double relativeGlacier;
                private long quotaGlacier;

                public long getFree() {
                    return free;
                }

                public void setFree(long free) {
                    this.free = free;
                }

                public long getUsed() {
                    return used;
                }

                public void setUsed(long used) {
                    this.used = used;
                }

                public long getTotal() {
                    return total;
                }

                public void setTotal(long total) {
                    this.total = total;
                }

                public double getRelative() {
                    return relative;
                }

                public void setRelative(double relative) {
                    this.relative = relative;
                }

                public long getQuota() {
                    return quota;
                }

                public void setQuota(long quota) {
                    this.quota = quota;
                }

                public long getFreeGlacier() {
                    return freeGlacier;
                }

                public void setFreeGlacier(long freeGlacier) {
                    this.freeGlacier = freeGlacier;
                }

                public long getUsedGlacier() {
                    return usedGlacier;
                }

                public void setUsedGlacier(long usedGlacier) {
                    this.usedGlacier = usedGlacier;
                }

                public long getTotalGlacier() {
                    return totalGlacier;
                }

                public void setTotalGlacier(long totalGlacier) {
                    this.totalGlacier = totalGlacier;
                }

                public double getRelativeGlacier() {
                    return relativeGlacier;
                }

                public void setRelativeGlacier(double relativeGlacier) {
                    this.relativeGlacier = relativeGlacier;
                }

                public long getQuotaGlacier() {
                    return quotaGlacier;
                }

                public void setQuotaGlacier(long quotaGlacier) {
                    this.quotaGlacier = quotaGlacier;
                }
            }

            public static class BackendCapabilitiesBean {
                /**
                 * setDisplayName : true
                 * setPassword : true
                 */

                private boolean setDisplayName;
                private boolean setPassword;

                public boolean isSetDisplayName() {
                    return setDisplayName;
                }

                public void setSetDisplayName(boolean setDisplayName) {
                    this.setDisplayName = setDisplayName;
                }

                public boolean isSetPassword() {
                    return setPassword;
                }

                public void setSetPassword(boolean setPassword) {
                    this.setPassword = setPassword;
                }
            }
        }
    }
}
