package com.blazings.excel.Service;

import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.blazings.excel.Model.OutData;
import com.blazings.excel.Model.templateData;
import com.blazings.excel.ExcelDataListener;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Service
public class ReadLogic {
    //筛选出奇数的红酒
    public void FiltOddNumWine(List<templateData> distriDataOut, List<templateData> onlyOneWineList) {
        Iterator<templateData> iterator = distriDataOut.iterator();
        while (iterator.hasNext()){
            templateData next = iterator.next();
            //红酒数量为1的
            if("紫薯红酒".equals(next.getGoodsName()) &&
            next.getGoodsCount().equals("1")){
                //红酒数量为1的添加到list
                onlyOneWineList.add(next);
                //红酒数量为1的从原表删除
                iterator.remove();
            }
            //红酒数量为大于1的奇数
            if ("紫薯红酒".equals(next.getGoodsName()) &&
                    !next.getGoodsCount().equals("1") &&
                    Integer.valueOf(next.getGoodsCount())%2!=0){
                //从outData中把奇数的减一
                next.setGoodsCount(String.valueOf((Integer.valueOf(next.getGoodsCount())-1)));
                templateData templateData = new templateData();
                templateData.setOrderID(next.getOrderID());
                templateData.setConsignee(next.getConsignee());
                templateData.setPhone(next.getPhone());
                templateData.setAddress(next.getAddress());
                templateData.setGoodsName(next.getGoodsName());
                templateData.setGoodsCount("1");
                templateData.setGoodsTotalValue(next.getGoodsTotalValue());
                templateData.setRemark(next.getRemark());
                templateData.setTransportProperties(next.getTransportProperties());
                templateData.setPayType(next.getPayType());
                templateData.setDeliveryType(next.getDeliveryType());
                templateData.setYPSC(next.getYPSC());
                onlyOneWineList.add(templateData);
            }
        }
    }

    //打印空白表   //打印名字和电话都是空
    public void WriteNullMsg(List<templateData> data, String templateFileName, String fillFileName, String fillDestFileNameNoMsg) {

        if (data.stream().count() > 0) {
            FileUtil.copy(fillFileName, fillDestFileNameNoMsg, true);
            for (templateData distriDataNoMsgAddOrderId : data) {
                distriDataNoMsgAddOrderId.setRemark(distriDataNoMsgAddOrderId.getGoodsName() + "   " + distriDataNoMsgAddOrderId.getGoodsCount()+"  "+distriDataNoMsgAddOrderId.getOrderID() );
            }
            EasyExcel.write(fillDestFileNameNoMsg).withTemplate(templateFileName).sheet().doFill(data);
        }
    }

    public void FiltAndAssemble(List<templateData> distriDataOut, List<templateData> distriDataOnlyNameAndPhone, List<templateData> distriDataNoMsg, ExcelDataListener excelDataListener) {
        OutData outData = new OutData();
        for (templateData excelSourceData : excelDataListener.list) {
            //如果名字和电话都是空
            if (excelSourceData.getConsignee()==null &&
                    excelSourceData.getPhone()==null){
                distriDataNoMsg.add(excelSourceData);
                continue;
            }
            //如果只有名字和电话
            if (excelSourceData.getConsignee()!=null &&
                    excelSourceData.getPhone()!=null &&
                    excelSourceData.getAddress()==null){
                distriDataOnlyNameAndPhone.add(excelSourceData);
                continue;
            }
            //正常收货
            if (distriDataOut.stream().count()>0){
                int findCount=0;
                int dataIndex = 0;
                //有姓名,电话, 地址的
                if (excelSourceData.getConsignee()!=null &&
                        excelSourceData.getPhone()!=null &&
                        excelSourceData.getAddress()!=null) {
                    for (int i = 0; i < distriDataOut.size(); i++) {
                        //有相同的人
                        if (excelSourceData.getConsignee().equals(distriDataOut.get(i).getConsignee()) &&
                                excelSourceData.getPhone().equals(distriDataOut.get(i).getPhone()) &&
                                excelSourceData.getGoodsName().equals(distriDataOut.get(i).getGoodsName()))
                        {
                            findCount++;
                            dataIndex = i;
                            break;
                        }
                    }
                }
                //说明唯一
                if (findCount == 0) {
                    distriDataOut.add(excelSourceData);
                }
                //+1货物
                if (findCount > 0) {
                    distriDataOut.get(dataIndex).setGoodsCount(String.valueOf(Integer.valueOf(distriDataOut.get(dataIndex).getGoodsCount())+1));
                }
            }
            //第一次对比
            else {
                if (excelSourceData.getConsignee()!=null &&
                        excelSourceData.getPhone()!=null &&
                        excelSourceData.getAddress()!=null){
                    distriDataOut.add(excelSourceData);
                }
            }
        }
    }
    //正常收货的备注设置
    public void SetValueRemark(List<templateData> distriDataOut) {
        for (templateData distriData : distriDataOut) {
            distriData.setRemark(distriData.getGoodsName()+
                    "   数量: "+
                    distriData.getGoodsCount()+
                    "   价格:"+
                    Integer.valueOf(distriData.getGoodsCount())* 399);
        }
    }
    //打印货物总数
    public void PrintGoodsTotalNum(List<templateData> distriDataOut, List<templateData> distriDataOnlyNameAndPhone, List<templateData> distriDataNoMsg) {
        int valueGoodsTotal=0;
        int distriDataNoMsgTotal =0;
        int distriDataOnlyNameAndPhoneTotal =0;
        for (templateData distriData : distriDataOut) {
            valueGoodsTotal+= Integer.valueOf(distriData.getGoodsCount());
        }
        System.out.println("有效收获地址货物总数为:" + valueGoodsTotal);
        for (templateData distriData : distriDataNoMsg) {
            distriDataNoMsgTotal+=Integer.valueOf(distriData.getGoodsCount());
        }
        System.out.println("没有地址货物总数为:" + distriDataNoMsgTotal);
        for (templateData distriData : distriDataOnlyNameAndPhone) {
            distriDataOnlyNameAndPhoneTotal+=Integer.valueOf(distriData.getGoodsCount());
        }
        System.out.println("没有地址货物总数为:" + distriDataOnlyNameAndPhoneTotal);
        System.out.println("总货物总数为:" + (valueGoodsTotal+distriDataNoMsgTotal+distriDataOnlyNameAndPhoneTotal));
    }
}
