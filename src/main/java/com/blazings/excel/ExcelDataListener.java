package com.blazings.excel;

import Model.DistriData;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelDataListener extends AnalysisEventListener<DistriData> {

    List<DistriData> list = new ArrayList<DistriData>();
    public ExcelDataListener() {
    }

    @Override
    public void invoke(DistriData distriData, AnalysisContext analysisContext) {
        //临时唯一用, 最后处理
//        distriData.setRemark(distriData.getGoodsName()+"   数量: "+distriData.getGoodsCount()+"   价格:"+Integer.valueOf(distriData.getGoodsCount())* 399);
        distriData.setGoodsCount("1");

        //固定
        distriData.setTransportProperties("标准快递");
        distriData.setPayType("月结");
        distriData.setDeliveryType("送货上楼");
        list.add(distriData);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<DistriData> getData(){
        return list;
    }
}
