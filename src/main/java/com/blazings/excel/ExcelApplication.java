package com.blazings.excel;

import Model.DistriData;
import com.alibaba.excel.EasyExcel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        String fileName = "D:\\download\\s.xlsx";
        String templateFileName = "D:\\download\\出库打单模版填充.xlsx";
        String fillFileName = "D:\\download\\out.xlsx";

        ExcelDataListener dataListener = new ExcelDataListener();
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, DistriData.class, dataListener).sheet("总表").doRead();

        EasyExcel.write(fillFileName).withTemplate(templateFileName).sheet().doFill(dataListener.list.get(0));
    }
}
