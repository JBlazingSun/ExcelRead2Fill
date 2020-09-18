package Model;

public class DistriData {
    private String orderID;
    private String consignee;
    private String phone;
    private String address;
    private String goodsName;
    private String goodsCount;
    private String goodsTotalValue;
    private String YPSC;
    //运输性质
    String transportProperties;
    //运费付款方式
    String payType;
    //送货方式
    String deliveryType;
    private String remark;


    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(String goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getGoodsTotalValue() {
        return goodsTotalValue;
    }

    public void setGoodsTotalValue(String goodsTotalValue) {
        this.goodsTotalValue = goodsTotalValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTransportProperties() {
        return transportProperties;
    }

    public void setTransportProperties(String transportProperties) {
        this.transportProperties = transportProperties;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getYPSC() {
        return YPSC;
    }

    public void setYPSC(String YPSC) {
        this.YPSC = YPSC;
    }
}
