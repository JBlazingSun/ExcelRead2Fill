package com.blazings.excel;

import com.blazings.excel.Model.templateData;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExcelDataListener extends AnalysisEventListener<templateData> {

    public List<templateData> list = new ArrayList<templateData>();
    public ExcelDataListener() {
    }

    @Override
    public void invoke(templateData distriData, AnalysisContext analysisContext) {
        distriData.setGoodsCount("1");

        //固定
        distriData.setTransportProperties("标准快递");
        distriData.setPayType("月结");
        distriData.setDeliveryType("送货上楼");
        distriData.setYPSC("易拼商城");
        list.add(distriData);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
