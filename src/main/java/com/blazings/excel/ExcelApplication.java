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
//        disableAccessWarnings();
        //
        List<DistriData> distriDataOut = new ArrayList<>();
        //只有名字和电话
        List<DistriData> distriDataOnlyNameAndPhone = new ArrayList<>();
        //名字和电话都是空
        List<DistriData> distriDataNoMsg = new ArrayList<>();

        String sheetNameInput = "t";
//        Scanner scan = new Scanner(System.in);
//        sheetNameInput = scan.nextLine();
//        scan.close();

        //C:\blazings\同步\work\易拼\物流\发货
        String sourceFileName = "C:\\blazings\\同步\\work\\易拼\\物流\\发货\\9-15到9-17-10.40多次辟谷丹.xlsx";
        //C:\blazings\同步\work\易拼\物流\打单
        String templateFileName = "C:\\blazings\\同步\\work\\易拼\\物流\\打单\\德邦快递精简模板列表.xlsx";
        //C:\blazings\同步\work\易拼\物流\打单
        //C:\blazings\download
        String fillFileName = "C:\\blazings\\同步\\work\\易拼\\物流\\打单\\null.xlsx";
        String fillDestFileName = "C:\\blazings\\download\\德邦上传.xlsx";
        String fillDestFileNameOnlyNameAndPhone = "C:\\blazings\\download\\德邦上传没有地址-只有电话和姓名.xlsx";
        String fillDestFileNameNoMsg = "C:\\blazings\\download\\德邦上传没有任何信息.xlsx";
        FileUtil.copy(fillFileName,fillDestFileName,true);

        ExcelDataListener excelDataListener = new ExcelDataListener();
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(sourceFileName, DistriData.class, excelDataListener).sheet(sheetNameInput).doRead();

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
            //
            if (distriDataOut.stream().count()>0){
                for (DistriData dataOut : distriDataOut) {
                    if (excelSourceData.getConsignee()!=null &&
                            excelSourceData.getPhone()!=null &&
                            excelSourceData.getAddress()!=null){
                        distriDataOut.add(excelSourceData);
                        break;
                    }
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
                distriDataNoMsgAddOrderId.setRemark(distriDataNoMsgAddOrderId.getOrderID());
            }
            EasyExcel.write(fillDestFileNameNoMsg).withTemplate(templateFileName).sheet().doFill(distriDataNoMsg);

        }
        //打印名字和电话都是空
        if (distriDataOnlyNameAndPhone.stream().count()>0){
            FileUtil.copy(fillFileName,fillDestFileNameOnlyNameAndPhone,true);
            for (DistriData distriDataOnlyNameAndPhoneAddId : distriDataOnlyNameAndPhone) {
                distriDataOnlyNameAndPhoneAddId.setRemark(distriDataOnlyNameAndPhoneAddId.getOrderID());
            }
            EasyExcel.write(fillDestFileNameOnlyNameAndPhone).withTemplate(templateFileName).sheet().doFill(distriDataOnlyNameAndPhone);

        }
        EasyExcel.write(fillDestFileName).withTemplate(templateFileName).sheet().doFill(distriDataOut);
    }
}
