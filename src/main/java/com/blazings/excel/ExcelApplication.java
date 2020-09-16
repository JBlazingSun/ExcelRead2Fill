package com.blazings.excel;

import Model.DemoData;
import com.alibaba.excel.EasyExcel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExcelApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ExcelApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 写法1：
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭

        String fileName = "C:\\blazings\\同步\\work\\易拼\\物流\\2020916 94256\\2020916 94256.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet().doRead();
    }
}
