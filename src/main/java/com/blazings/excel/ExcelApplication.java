package com.blazings.excel;

import com.blazings.excel.Model.OutData;
import com.blazings.excel.Model.templateData;
import com.blazings.excel.Service.ReadLogic;
import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ExcelApplication implements CommandLineRunner {
    final
    ReadLogic readLogic;

    public ExcelApplication(ReadLogic readLogic) {
        this.readLogic = readLogic;
    }

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
        List<templateData> distriDataOut = new ArrayList<>();
        //只有名字和电话
        List<templateData> distriDataOnlyNameAndPhone = new ArrayList<>();
        //名字和电话都是空
        List<templateData> distriDataNoMsg = new ArrayList<>();
        //只有红酒一瓶的
        List<templateData> onlyOneWineList = new ArrayList<>();
        OutData outData = new OutData();
        String sourceFileName="";
        String templateFileName="";
        String fillFileName="";
        String fillDestFileName="";
        String fillDestFileNameOnlyNameAndPhone="";
        String fillDestFileNameNoMsg="";
        String onlyOneWineName="";

        //home
        //work
        String switchAddr = "work";
//        String sheetNameInput = "Table";

        if ("work".equals(switchAddr)){
            //C:\blazings\同步\work\易拼\物流\发货
            sourceFileName = "C:\\blazings\\同步\\work\\易拼\\物流\\发货\\15号-----20号没有发货的(1).xlsx";
            //C:\blazings\同步\work\易拼\物流\打单
            templateFileName = "C:\\blazings\\同步\\work\\易拼\\物流\\打单\\德邦快递精简模板列表.xlsx";
            //C:\blazings\同步\work\易拼\物流\打单
            //C:\blazings\download
            fillFileName = "C:\\blazings\\同步\\work\\易拼\\物流\\打单\\null.xlsx";
            fillDestFileName = "C:\\blazings\\download\\德邦上传.xlsx";
            fillDestFileNameOnlyNameAndPhone = "C:\\blazings\\download\\德邦上传没有地址-只有电话和姓名.xlsx";
            fillDestFileNameNoMsg = "C:\\blazings\\download\\德邦上传缺少信息.xlsx";
            onlyOneWineName = "C:\\blazings\\download\\只有奇数瓶红酒的.xlsx";
        }
        if ("home".equals(switchAddr)){
            //D:\微云同步助手\328801898\同步\work\易拼\物流\打单    home
            sourceFileName = "D:\\download\\2020-09-17-10-40---2020-9-19-12-32-00.xlsx";
            //D:\微云同步助手\328801898\同步\work\易拼\物流\打单 home
            templateFileName = "D:\\微云同步助手\\328801898\\同步\\work\\易拼\\物流\\打单\\德邦快递精简模板列表.xlsx";
            //D:\微云同步助手\328801898\同步\work\易拼\物流\打单 home
            fillFileName = "D:\\微云同步助手\\328801898\\同步\\work\\易拼\\物流\\打单\\null.xlsx";
            //D:\download home
            fillDestFileName = "D:\\download\\德邦上传.xlsx";
            fillDestFileNameOnlyNameAndPhone = "D:\\download\\德邦上传没有地址-只有电话和姓名.xlsx";
            fillDestFileNameNoMsg = "D:\\download\\德邦上传缺少信息.xlsx";
            onlyOneWineName = "D:\\download\\只有奇数瓶红酒的.xlsx";
        }

        //清除文件
        FileUtil.del(fillDestFileName);
        FileUtil.del(fillDestFileNameOnlyNameAndPhone);
        FileUtil.del(fillDestFileNameNoMsg);
        FileUtil.del(onlyOneWineName);

        FileUtil.copy(fillFileName,fillDestFileName,true);

        ExcelDataListener excelDataListener = new ExcelDataListener();
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(sourceFileName, templateData.class, excelDataListener).sheet().doRead();

        readLogic.FiltAndAssemble(distriDataOut, distriDataOnlyNameAndPhone, distriDataNoMsg, excelDataListener);

        //打印空白表
        readLogic.WriteNullMsg(distriDataNoMsg, templateFileName, fillFileName, fillDestFileNameNoMsg);

        //打印名字和电话都是空
        readLogic.WriteNullMsg(distriDataOnlyNameAndPhone, templateFileName, fillFileName, fillDestFileNameOnlyNameAndPhone);

        //打印货物总数
        readLogic.PrintGoodsTotalNum(distriDataOut, distriDataOnlyNameAndPhone, distriDataNoMsg);

        //筛选出奇数--红酒
//        readLogic.FiltOddNumWine(distriDataOut, onlyOneWineList);

        //奇数的备注设置--红酒
//        readLogic.SetValueRemark(onlyOneWineList);

        //正常收货的备注设置
        readLogic.SetValueRemark(distriDataOut);

        //打印出奇数--红酒
//        EasyExcel.write(onlyOneWineName).withTemplate(templateFileName).sheet().doFill(onlyOneWineList);

        //货物件数恢复为1
        for (templateData distriData : distriDataOut) {
            distriData.setGoodsCount("1");
        }
        //打印正常地址单
        EasyExcel.write(fillDestFileName).withTemplate(templateFileName).sheet().doFill(distriDataOut);
    }

}
