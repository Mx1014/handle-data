package com.zhsl.test;

import com.zhsl.data.entity.WFVo;
import com.zhsl.data.jdbc.mysql.JDBCTools;
import com.zhsl.data.util.ExcelUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiangjg
 * @date 2019/5/13 15:27
 */
public class MyWFExcel {

    @Test
    public void test(){
        try {
            ExcelUtil eu = new ExcelUtil();
            eu.setExcelPath("D:\\查重复值.xlsx");
            eu.setSelectedSheetIdx(0);
            List<Row> list0 = eu.readExcel();
            List<List<String>> myList = new ArrayList<>();
            for (int i = 0; i < list0.size(); i++) {
                if (i > 0) {
                    List<String> tmp = new ArrayList<>();
                    tmp.add(String.valueOf(i));
                    for (int j = 2; j < 4; j++) {
                        String value = ExcelUtil.getCellValueForCell(eu.wb, list0.get(i).getCell(j));
                        tmp.add(value.trim());
                    }
                    myList.add(tmp);
                }
            }
            //System.out.println(myList.toString());

            List<WFVo> allList = getList(myList);


            eu.setSelectedSheetIdx(1);
            List<Row> list1 = eu.readExcel();
            myList = new ArrayList<>();
            for (int i = 0; i < list1.size(); i++) {
                if (i > 0) {
                    List<String> tmp = new ArrayList<>();
                    tmp.add(String.valueOf(i));
                    for (int j = 0; j < 2; j++) {
                        String value = ExcelUtil.getCellValueForCell(eu.wb, list1.get(i).getCell(j));
                        tmp.add(value.trim());
                    }
                    myList.add(tmp);
                }
            }
            //System.out.println(myList.toString());

            List<WFVo> oneList = getList(myList);


            CellStyle style = eu.wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);


            List<Integer> ret = new ArrayList<>();
            List<WFVo> tmpList = getList(myList);
            for (WFVo obj1:oneList
                 ) {
                for (WFVo obj2:allList
                        ) {
                    if(obj1.getCode().equals(obj2.getCode())){
                        tmpList.add(obj1);
                        ret.add(obj1.getNum());
                        for (int i = 0; i < 16; i++) {
                            list0.get(obj1.getNum()-1).getCell(i).setCellStyle(style);
                        }

                    }
                }
            }
            System.out.println("对比编码后，总表中存在表1的记录数:"+ret.size());
            System.out.println("其中重复：");
            for (WFVo obj1:tmpList
                    ) {
                for (WFVo obj2:tmpList
                        ) {
                    if(obj1.getNum()!=obj2.getNum()&&obj1.getCode().equals(obj2.getCode())){
                        System.out.println("第 "+obj1.getNum()+"行编码：["+obj1.getCode()+"]  和  第 "+obj2.getNum()+ " 行编码：["+obj1.getCode()+"] 重复");
                    }
                }
            }
            System.out.println(ret.toString());
//
//            ret = new ArrayList<>();
//
//            for (WFVo obj1:oneList
//                    ) {
//                for (WFVo obj2:allList
//                        ) {
//                    if(obj1.getCode().equals(obj2.getCode())&&obj1.getName().equals(obj2.getName())){
//                        ret.add(obj1.getNum());
//                    }
//                }
//            }
//
//            System.out.println("对比编码及名称后，总表中存在表1的记录数:"+ret.size());
//            System.out.println(ret.toString());


        }catch (Exception e){

        }
    }

    private List<WFVo> getList(List<List<String>> myList){
        List<WFVo> list = new ArrayList<>();
        for (List<String> l:myList
                ) {
            WFVo vo = new WFVo();
            vo.setNum(Integer.valueOf(l.get(0))+1);
            vo.setCode(l.get(1));
            vo.setName(l.get(2));
            if(!StringUtils.isEmpty(l.get(1)))
                list.add(vo);
        }
        return list;
    }
}
