package com.blazings.excel;

import Model.DistriData;
import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class ExcelApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ExcelApplication.class, args);
    }
    @SuppressWarnings("unchecked")
    public static void disableAccessWarnings() {
        try {
            Class unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Object unsafe = field.get(null);

            Method putObjectVolatile = unsafeClass.getDeclaredMethod("putObjectVolatile", Object.class, long.class, Object.class);
            Method staticFieldOffset = unsafeClass.getDeclaredMethod("staticFieldOffset", Field.class);

            Class loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field loggerField = loggerClass.getDeclaredField("logger");
            Long offset = (Long) staticFieldOffset.invoke(unsafe, loggerField);
            putObjectVolatile.invoke(unsafe, loggerClass, offset, null);
        } catch (Exception ignored) {
        }
    }
    @Override
    public void run(String... args) throws Exception {
        disableAccessWarnings();
        //
        List<DistriData> distriDataOut = new ArrayList<>();
        //只有名字和电话
        List<DistriData> distriDataOnlyNameAndPhone = new ArrayList<>();
        //名字和电话都是空
        List<DistriData> distriDataNoMsg = new ArrayList<>();

        String sheetNameInput = "Table";

//        Scanner scan = new Scanner(System.in);
//        sheetNameInput = scan.nextLine();
//        scan.close();
        
        //C:\blazings\同步\work\易拼\物流\发货
        //D:\微云同步助手\328801898\同步\work\易拼\物流\打单    home
        String sourceFileName = "D:\\微云同步助手\\328801898\\同步\\work\\易拼\\物流\\发货\\物流单_20200919234001.xlsx";
        //C:\blazings\同步\work\易拼\物流\打单
        //D:\微云同步助手\328801898\同步\work\易拼\物流\打单 home
        String templateFileName = "D:\\微云同步助手\\328801898\\同步\\work\\易拼\\物流\\打单\\德邦快递精简模板列表.xlsx";
        //C:\blazings\同步\work\易拼\物流\打单
        //C:\blazings\download
        //D:\微云同步助手\328801898\同步\work\易拼\物流\打单 home
        String fillFileName = "D:\\微云同步助手\\328801898\\同步\\work\\易拼\\物流\\打单\\null.xlsx";
        //D:\download home
        String fillDestFileName = "D:\\download\\德邦上传.xlsx";
        String fillDestFileNameOnlyNameAndPhone = "D:\\download\\德邦上传没有地址-只有电话和姓名.xlsx";
        String fillDestFileNameNoMsg = "D:\\download\\德邦上传没有任何信息.xlsx";
        //清除文件
        FileUtil.del(fillDestFileName);
        FileUtil.del(fillDestFileNameOnlyNameAndPhone);
        FileUtil.del(fillDestFileNameNoMsg);

        FileUtil.copy(fillFileName,fillDestFileName,true);

        ExcelDataListener excelDataListener = new ExcelDataListener();
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(sourceFileName, DistriData.class, excelDataListener).sheet().doRead();

        for (DistriData excelSourceData : excelDataListener.list) {
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

        //打印空白表
        if (distriDataNoMsg.stream().count()>0){
            FileUtil.copy(fillFileName,fillDestFileNameNoMsg,true);
            for (DistriData distriDataNoMsgAddOrderId : distriDataNoMsg) {
                distriDataNoMsgAddOrderId.setRemark(distriDataNoMsgAddOrderId.getOrderID()+"  "+distriDataNoMsgAddOrderId.getGoodsName()+ "   "+distriDataNoMsgAddOrderId.getGoodsCount());
            }
            EasyExcel.write(fillDestFileNameNoMsg).withTemplate(templateFileName).sheet().doFill(distriDataNoMsg);

        }
        //打印名字和电话都是空
        if (distriDataOnlyNameAndPhone.stream().count()>0){
            FileUtil.copy(fillFileName,fillDestFileNameOnlyNameAndPhone,true);
            for (DistriData distriDataOnlyNameAndPhoneAddId : distriDataOnlyNameAndPhone) {
                distriDataOnlyNameAndPhoneAddId.setRemark(distriDataOnlyNameAndPhoneAddId.getOrderID()+"  "+distriDataOnlyNameAndPhoneAddId.getGoodsName()+ "   "+distriDataOnlyNameAndPhoneAddId.getGoodsCount());
            }
            EasyExcel.write(fillDestFileNameOnlyNameAndPhone).withTemplate(templateFileName).sheet().doFill(distriDataOnlyNameAndPhone);

        }

        //设置正常收货备注
        for (DistriData distriData : distriDataOut) {
            distriData.setRemark(distriData.getGoodsName()+
                    "   数量: "+
                    distriData.getGoodsCount()+
                    "   价格:"+
                    Integer.valueOf(distriData.getGoodsCount())* 399);
        }
        //打印货物总数
        int valueGoodsTotal=0;
        int distriDataNoMsgTotal =0;
        int distriDataOnlyNameAndPhoneTotal =0;
        for (DistriData distriData : distriDataOut) {
            valueGoodsTotal+= Integer.valueOf(distriData.getGoodsCount());
        }
        System.out.println("有效收获地址货物总数为:" + valueGoodsTotal);
        for (DistriData distriData : distriDataNoMsg) {
            distriDataNoMsgTotal+=Integer.valueOf(distriData.getGoodsCount());
        }
        System.out.println("没有地址货物总数为:" + distriDataNoMsgTotal);
        for (DistriData distriData : distriDataOnlyNameAndPhone) {
            distriDataOnlyNameAndPhoneTotal+=Integer.valueOf(distriData.getGoodsCount());
        }
        System.out.println("没有地址货物总数为:" + distriDataOnlyNameAndPhoneTotal);
        System.out.println("总货物总数为:" + (valueGoodsTotal+distriDataNoMsgTotal+distriDataOnlyNameAndPhoneTotal));
        //货物件数恢复为1
        for (DistriData distriData : distriDataOut) {
            distriData.setGoodsCount("1");
        }
        EasyExcel.write(fillDestFileName).withTemplate(templateFileName).sheet().doFill(distriDataOut);
    }
}
