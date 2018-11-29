package com.ximai.savingsmore.save.modle;

import java.util.List;

public class SearchLocationModle {

    /**
     * IsSuccess : true
     * Message : sample string 2
     * Id : sample string 3
     * MainData : [{"CountryId":"7ecac729-90ab-4354-85da-b35d29060e0d","ProvinceId":"ba943f8c-e0e8-43a1-bcc7-977bea3858d5","CityId":"2492fe7c-f3e7-495a-bcec-d41b22b83c85","AreaId":"9782561b-6e7c-42ac-a8cb-3e9cdd09bedf","Address":"sample string 1"},{"CountryId":"7ecac729-90ab-4354-85da-b35d29060e0d","ProvinceId":"ba943f8c-e0e8-43a1-bcc7-977bea3858d5","CityId":"2492fe7c-f3e7-495a-bcec-d41b22b83c85","AreaId":"9782561b-6e7c-42ac-a8cb-3e9cdd09bedf","Address":"sample string 1"}]
     * ShowData : sample string 4
     * OtherData : sample string 5
     * TotalRecordCount : 6
     * TotalPageCount : 7
     * AllTotalRecordCount : 8
     */

    private boolean IsSuccess;
    private String Message;
    private String Id;
    private String ShowData;
    private String OtherData;
    private int TotalRecordCount;
    private int TotalPageCount;
    private int AllTotalRecordCount;
    private List<MainDataBean> MainData;

    public boolean isIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(boolean IsSuccess) {
        this.IsSuccess = IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getShowData() {
        return ShowData;
    }

    public void setShowData(String ShowData) {
        this.ShowData = ShowData;
    }

    public String getOtherData() {
        return OtherData;
    }

    public void setOtherData(String OtherData) {
        this.OtherData = OtherData;
    }

    public int getTotalRecordCount() {
        return TotalRecordCount;
    }

    public void setTotalRecordCount(int TotalRecordCount) {
        this.TotalRecordCount = TotalRecordCount;
    }

    public int getTotalPageCount() {
        return TotalPageCount;
    }

    public void setTotalPageCount(int TotalPageCount) {
        this.TotalPageCount = TotalPageCount;
    }

    public int getAllTotalRecordCount() {
        return AllTotalRecordCount;
    }

    public void setAllTotalRecordCount(int AllTotalRecordCount) {
        this.AllTotalRecordCount = AllTotalRecordCount;
    }

    public List<MainDataBean> getMainData() {
        return MainData;
    }

    public void setMainData(List<MainDataBean> MainData) {
        this.MainData = MainData;
    }

    public static class MainDataBean {
        /**
         * CountryId : 7ecac729-90ab-4354-85da-b35d29060e0d
         * ProvinceId : ba943f8c-e0e8-43a1-bcc7-977bea3858d5
         * CityId : 2492fe7c-f3e7-495a-bcec-d41b22b83c85
         * AreaId : 9782561b-6e7c-42ac-a8cb-3e9cdd09bedf
         * Address : sample string 1
         */

        private String CountryId;
        private String ProvinceId;
        private String CityId;
        private String AreaId;
        private String Address;

        public String getCountryId() {
            return CountryId;
        }

        public void setCountryId(String CountryId) {
            this.CountryId = CountryId;
        }

        public String getProvinceId() {
            return ProvinceId;
        }

        public void setProvinceId(String ProvinceId) {
            this.ProvinceId = ProvinceId;
        }

        public String getCityId() {
            return CityId;
        }

        public void setCityId(String CityId) {
            this.CityId = CityId;
        }

        public String getAreaId() {
            return AreaId;
        }

        public void setAreaId(String AreaId) {
            this.AreaId = AreaId;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }
    }
}
