package com.blazings.excel;

import Model.DistriData;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelDataListener extends AnalysisEventListener<DistriData> {
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    List<DistriData> list = new ArrayList<DistriData>();
    public ExcelDataListener() {
        // 这里是demo，所以随便new一个。实际使用如果到了spring,请使用下面的有参构造函数
    }

    @Override
    public void invoke(DistriData distriData, AnalysisContext analysisContext) {
        //临时唯一用, 最后处理
        distriData.setRemark(distriData.getGoodsName()+"   数量: "+distriData.getGoodsCount()+"   价格:"+Integer.valueOf(distriData.getGoodsCount())* 399);

        //固定
//        distriData.setTransportProperties("标准快递");
//        distriData.setPayType("月结");
//        distriData.setDeliveryType("送货上楼");
        list.add(distriData);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<DistriData> getData(){
        return list;
    }
}
