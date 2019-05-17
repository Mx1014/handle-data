package com.zhsl.test;

import com.zhsl.data.entity.WFVo;
import com.zhsl.data.jdbc.mysql.JDBCTools;
import com.zhsl.data.jdbc.pg.PgJdbc;
import com.zhsl.data.util.ExcelUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiangjg
 * @date 2019/5/13 15:27
 */
public class MyWFExcel {

    @Test
    public void inDB(){
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        try {
            con = PgJdbc.getConnection();
            sta = con.createStatement();

            ExcelUtil eu = new ExcelUtil();
            eu.setExcelPath("D:\\云岩区市监局资产5.16.xlsx");
            eu.setSelectedSheetIdx(1);
            List<Row> list = eu.readExcel();
            int type = 1;
            for (int i = 0; i < list.size(); i++) {
                if (i > 5) {
                    String codes = ExcelUtil.getCellValueForCell(eu.wb, list.get(i).getCell(2));
                    String name = ExcelUtil.getCellValueForCell(eu.wb, list.get(i).getCell(4));
                    String price = ExcelUtil.getCellValueForCell(eu.wb, list.get(i).getCell(10));
                    if(StringUtils.isEmpty(price.trim()))
                        price = "0";
                    String[] strs = codes.split("\n");
                    for (String codet:strs
                         ) {
                        String[] strs2 = codet.split("-");
                        if(strs2.length==2&&strs2[1].length()<=4){
                            String sql = String.format("insert into wf_table(num,code,name,tb,price) values(%s,'%s','%s',%s,%s)",
                                    i, strs2[0]+"-"+strs2[1], name, type, price);
                            System.out.println(sql);
                            JDBCTools.execute(sql);
                        }else{
                            for (String code:strs2
                                    ) {
                                if (!StringUtils.isEmpty(code)) {
                                    String sql = String.format("insert into wf_table(num,code,name,tb,price) values(%s,'%s','%s',%s,%s)",
                                            i, code, name, type, price);
                                    System.out.println(sql);
                                    JDBCTools.execute(sql);
                                }
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (sta != null)
                    sta.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    @Test
    public void test() {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        try {
            con = PgJdbc.getConnection();
            sta = con.createStatement();

            int tb = 2;
            File file = new File("D:\\云岩区市监局资产5.16.xlsx");
            FileInputStream inputStream = new FileInputStream(file);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
            Sheet sheet = xssfWorkbook.getSheetAt(0);
            XSSFCellStyle style = xssfWorkbook.createCellStyle();
            style.setFillForegroundColor(new XSSFColor(java.awt.Color.RED));
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            //myList = new ArrayList<>();
           // List<Map<String, String>> list = JDBCTools.query("select t1.* from wf_table t1 JOIN wf_table t2 on t1.code=t2.code where t1.tb=0 and t2.tb="+tb+"");// t1.price=t2.price
            List<Map<String, String>> list = JDBCTools.query("select t1.* from wf_table t1 where t1.tb=0 and t1.code not in (select t2.code from wf_table t2 where t2.tb=1)");
            System.out.println("表1中编码不在表2的记录有："+list.size());
            for (Map<String, String> obj:list
                 ) {
                int index = Integer.valueOf(obj.get("num"));
                for (int j = 0; j < 15; j++) {
                    sheet.getRow(index).getCell(j).setCellStyle(style);
                }
            }
//            XSSFCellStyle redStyle = xssfWorkbook.createCellStyle();
//            redStyle.setFillForegroundColor(new XSSFColor(java.awt.Color.RED));
//            redStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//            list = JDBCTools.query("select t1.* from wf_table t1 JOIN wf_table t2 on t1.code=t2.code where t1.tb=0 and t2.tb="+tb+" and t1.price<>t2.price");
//            for (Map<String, String> obj:list
//                    ) {
//                int index = Integer.valueOf(obj.get("num"));
//                for (int j = 10; j < 16; j++) {
//                    sheet.getRow(index).getCell(j).setCellStyle(redStyle);
//                }
//            }
            //对修改后的Excel进行保存
            FileOutputStream excelFileOutPutStream = new FileOutputStream(file.getAbsolutePath());
            xssfWorkbook.write(excelFileOutPutStream);
            excelFileOutPutStream.flush();
            excelFileOutPutStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (sta != null)
                    sta.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
