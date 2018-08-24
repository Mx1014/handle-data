package com.zhsl.test.file;

import org.junit.Test;

import java.io.File;

public class ReNameToFile {

    @Test
    public void test() {
        String path = "F:\\work\\水城智慧水务\\水城三维\\贵州水城\\以朵大道模型\\osgb\\3dtiles\\Data";
        File file = new File(path);
        String[] fileNames = file.list();
        File oldcfile,newcfile,oldfile,newfile;
        String[] cfileNames;
        for (String fileName : fileNames
                ) {
            file = new File(path + File.separator + fileName);
            cfileNames = file.list();
            for (String fName : cfileNames
                    ) {
                System.out.println(fName);
                oldcfile = new File(path + File.separator + fileName + File.separator + fName);
                newcfile = new File(path + File.separator + fileName + File.separator + fName.replace("_+", "_"));
                oldcfile.renameTo(newcfile);
            }
            oldfile = new File(path + File.separator + fileName);
            newfile = new File(path + File.separator + fileName.replace("_+", "_"));
            oldfile.renameTo(newfile);
        }
    }
}
