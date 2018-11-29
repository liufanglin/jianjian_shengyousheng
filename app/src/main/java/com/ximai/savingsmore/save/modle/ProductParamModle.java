package com.ximai.savingsmore.save.modle;

public class ProductParamModle {

    /**
     * Keywords : sample string 1
     * ProductParam : {"PageParameter":{"PageNo":1,"PageSize":2},"Radius":1,"Longitude":1,"Latitude":1,"IsBag":true,"CountryId":"0d8c56ea-ba88-4ec6-a4e6-3beeb3cb559c","ProvinceId":"99424b8b-bd00-436a-8aae-f9a373c66eb9","CityId":"1e8bf43e-1499-4596-a6b7-2a05bb55e6c7","AreaId":"59128302-7866-40c8-b54e-3ddad6a7cd4b","FirstClassId":"dd6add5e-37b1-494f-8c12-5f43eed4d3f3","SecondClassId":"bee2a784-4565-4299-8aaf-777f2714d785","Brand":"sample string 1","SellerId":"61c61946-b84d-4880-ab40-7b927d9784de","IsPromotion":true,"PromotionType":0,"Score":1,"Keyword":"sample string 2","IsTop":true,"IsRebateDesc":true,"IsPriceDesc":true,"IsStartTimeDesc":true,"IsDistanceDesc":true,"IsSaleCountDesc":true,"SecurityStampParameter":{"SecurityStamp":"sample string 1"},"POI":{"CountryId":"b7fa2067-6bd9-4f95-9913-1231168b5285","ProvinceId":"9aad219c-4c74-42c9-b5be-109e1c6be6d3","CityId":"715ef4d3-60a9-4049-b130-f0d65b73d5b6","AreaId":"43ed6c03-a230-4dbb-84e8-677218cd9226","Address":"sample string 1"}}
     */

    private String Keywords;
    private ProductParamBean ProductParam;

    public String getKeywords() {
        return Keywords;
    }

    public void setKeywords(String Keywords) {
        this.Keywords = Keywords;
    }

    public ProductParamBean getProductParam() {
        return ProductParam;
    }

    public void setProductParam(ProductParamBean ProductParam) {
        this.ProductParam = ProductParam;
    }

    public static class ProductParamBean {
        /**
         * PageParameter : {"PageNo":1,"PageSize":2}
         * Radius : 1
         * Longitude : 1.0
         * Latitude : 1.0
         * IsBag : true
         * CountryId : 0d8c56ea-ba88-4ec6-a4e6-3beeb3cb559c
         * ProvinceId : 99424b8b-bd00-436a-8aae-f9a373c66eb9
         * CityId : 1e8bf43e-1499-4596-a6b7-2a05bb55e6c7
         * AreaId : 59128302-7866-40c8-b54e-3ddad6a7cd4b
         * FirstClassId : dd6add5e-37b1-494f-8c12-5f43eed4d3f3
         * SecondClassId : bee2a784-4565-4299-8aaf-777f2714d785
         * Brand : sample string 1
         * SellerId : 61c61946-b84d-4880-ab40-7b927d9784de
         * IsPromotion : true
         * PromotionType : 0
         * Score : 1
         * Keyword : sample string 2
         * IsTop : true
         * IsRebateDesc : true
         * IsPriceDesc : true
         * IsStartTimeDesc : true
         * IsDistanceDesc : true
         * IsSaleCountDesc : true
         * SecurityStampParameter : {"SecurityStamp":"sample string 1"}
         * POI : {"CountryId":"b7fa2067-6bd9-4f95-9913-1231168b5285","ProvinceId":"9aad219c-4c74-42c9-b5be-109e1c6be6d3","CityId":"715ef4d3-60a9-4049-b130-f0d65b73d5b6","AreaId":"43ed6c03-a230-4dbb-84e8-677218cd9226","Address":"sample string 1"}
         */

        private PageParameterBean PageParameter;
        private int Radius;
        private double Longitude;
        private double Latitude;
        private boolean IsBag;
        private String CountryId;
        private String ProvinceId;
        private String CityId;
        private String AreaId;
        private String FirstClassId;
        private String SecondClassId;
        private String Brand;
        private String SellerId;
        private boolean IsPromotion;
        private int PromotionType;
        private int Score;
        private String Keyword;
        private boolean IsTop;
        private boolean IsRebateDesc;
        private boolean IsPriceDesc;
        private boolean IsStartTimeDesc;
        private boolean IsDistanceDesc;
        private boolean IsSaleCountDesc;
        private SecurityStampParameterBean SecurityStampParameter;
        private POIBean POI;

        public PageParameterBean getPageParameter() {
            return PageParameter;
        }

        public void setPageParameter(PageParameterBean PageParameter) {
            this.PageParameter = PageParameter;
        }

        public int getRadius() {
            return Radius;
        }

        public void setRadius(int Radius) {
            this.Radius = Radius;
        }

        public double getLongitude() {
            return Longitude;
        }

        public void setLongitude(double Longitude) {
            this.Longitude = Longitude;
        }

        public double getLatitude() {
            return Latitude;
        }

        public void setLatitude(double Latitude) {
            this.Latitude = Latitude;
        }

        public boolean isIsBag() {
            return IsBag;
        }

        public void setIsBag(boolean IsBag) {
            this.IsBag = IsBag;
        }

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

        public String getFirstClassId() {
            return FirstClassId;
        }

        public void setFirstClassId(String FirstClassId) {
            this.FirstClassId = FirstClassId;
        }

        public String getSecondClassId() {
            return SecondClassId;
        }

        public void setSecondClassId(String SecondClassId) {
            this.SecondClassId = SecondClassId;
        }

        public String getBrand() {
            return Brand;
        }

        public void setBrand(String Brand) {
            this.Brand = Brand;
        }

        public String getSellerId() {
            return SellerId;
        }

        public void setSellerId(String SellerId) {
            this.SellerId = SellerId;
        }

        public boolean isIsPromotion() {
            return IsPromotion;
        }

        public void setIsPromotion(boolean IsPromotion) {
            this.IsPromotion = IsPromotion;
        }

        public int getPromotionType() {
            return PromotionType;
        }

        public void setPromotionType(int PromotionType) {
            this.PromotionType = PromotionType;
        }

        public int getScore() {
            return Score;
        }

        public void setScore(int Score) {
            this.Score = Score;
        }

        public String getKeyword() {
            return Keyword;
        }

        public void setKeyword(String Keyword) {
            this.Keyword = Keyword;
        }

        public boolean isIsTop() {
            return IsTop;
        }

        public void setIsTop(boolean IsTop) {
            this.IsTop = IsTop;
        }

        public boolean isIsRebateDesc() {
            return IsRebateDesc;
        }

        public void setIsRebateDesc(boolean IsRebateDesc) {
            this.IsRebateDesc = IsRebateDesc;
        }

        public boolean isIsPriceDesc() {
            return IsPriceDesc;
        }

        public void setIsPriceDesc(boolean IsPriceDesc) {
            this.IsPriceDesc = IsPriceDesc;
        }

        public boolean isIsStartTimeDesc() {
            return IsStartTimeDesc;
        }

        public void setIsStartTimeDesc(boolean IsStartTimeDesc) {
            this.IsStartTimeDesc = IsStartTimeDesc;
        }

        public boolean isIsDistanceDesc() {
            return IsDistanceDesc;
        }

        public void setIsDistanceDesc(boolean IsDistanceDesc) {
            this.IsDistanceDesc = IsDistanceDesc;
        }

        public boolean isIsSaleCountDesc() {
            return IsSaleCountDesc;
        }

        public void setIsSaleCountDesc(boolean IsSaleCountDesc) {
            this.IsSaleCountDesc = IsSaleCountDesc;
        }

        public SecurityStampParameterBean getSecurityStampParameter() {
            return SecurityStampParameter;
        }

        public void setSecurityStampParameter(SecurityStampParameterBean SecurityStampParameter) {
            this.SecurityStampParameter = SecurityStampParameter;
        }

        public POIBean getPOI() {
            return POI;
        }

        public void setPOI(POIBean POI) {
            this.POI = POI;
        }

        public static class PageParameterBean {
            /**
             * PageNo : 1
             * PageSize : 2
             */

            private int PageNo;
            private int PageSize;

            public int getPageNo() {
                return PageNo;
            }

            public void setPageNo(int PageNo) {
                this.PageNo = PageNo;
            }

            public int getPageSize() {
                return PageSize;
            }

            public void setPageSize(int PageSize) {
                this.PageSize = PageSize;
            }
        }

        public static class SecurityStampParameterBean {
            /**
             * SecurityStamp : sample string 1
             */

            private String SecurityStamp;

            public String getSecurityStamp() {
                return SecurityStamp;
            }

            public void setSecurityStamp(String SecurityStamp) {
                this.SecurityStamp = SecurityStamp;
            }
        }

        public static class POIBean {
            /**
             * CountryId : b7fa2067-6bd9-4f95-9913-1231168b5285
             * ProvinceId : 9aad219c-4c74-42c9-b5be-109e1c6be6d3
             * CityId : 715ef4d3-60a9-4049-b130-f0d65b73d5b6
             * AreaId : 43ed6c03-a230-4dbb-84e8-677218cd9226
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
}
