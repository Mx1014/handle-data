package com.zhsl.test;

import com.zhsl.data.util.UnicodeReader;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author xiangjg
 * @date 2018/7/12 10:02
 */
public class RemoveBom {
    @Test
    public void remove(){
        String filepath = "E:\\code\\java\\Test_DPSDK_Java_win64\\src\\com\\dh\\DpsdkCore";
        try {
            File file = new File(filepath);
            if (file.isDirectory()){
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++){
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (readfile.isDirectory())
                        break;
                    if (! readfile.exists()) {
                        throw new IOException( "文件不存在" );
                    }
                    trimBom(filepath + "\\" + filelist[i]);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void trimBom(String fileName) throws IOException {

        FileInputStream fin = new FileInputStream(fileName);
        // 开始写临时文件
        InputStream in = getInputStream(fin);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte b[] = new byte[4096];

        int len = 0;
        while (in.available() > 0) {
            len = in.read(b, 0, 4096);
            //out.write(b, 0, len);
            bos.write(b, 0, len);
        }

        in.close();
        fin.close();
        bos.close();

        //临时文件写完，开始将临时文件写回本文件。
        FileOutputStream out = new FileOutputStream(fileName);
        out.write(bos.toByteArray());
        out.close();
    }
    public InputStream getInputStream(InputStream in) throws IOException {

        PushbackInputStream testin = new PushbackInputStream(in);
        int ch = testin.read();
        if (ch != 0xEF) {
            testin.unread(ch);
        } else if ((ch = testin.read()) != 0xBB) {
            testin.unread(ch);
            testin.unread(0xef);
        } else if ((ch = testin.read()) != 0xBF) {
            throw new IOException("错误的UTF-8格式文件");
        } else {
            // 不需要做，这里是bom头被读完了
            // System.out.println("still exist bom");
        }
        return testin;

    }

}
