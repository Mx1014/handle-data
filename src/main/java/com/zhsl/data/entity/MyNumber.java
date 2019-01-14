package com.zhsl.data.entity;

import java.util.ArrayList;
import java.util.List;

public class MyNumber {
    private String num;
    private String rewardNum;
    private Integer group;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getRewardNum() {
        return rewardNum;
    }

    public void setRewardNum(String rewardNum) {
        this.rewardNum = rewardNum;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public MyNumber() {
    }

    public MyNumber(String num, String rewardNum) {
        rewardNum = rewardNum.trim().replace(" ","");
        this.num = num;
        this.rewardNum = rewardNum;
        initGroup(rewardNum);
    }

    @Override
    public String toString() {
        return "MyNumber{" +
                "num=" + num +
                ", rewardNum='" + rewardNum + '\'' +
                ", group=" + group +
                '}';
    }
    private void initGroup(String rewardNum){
        char[] chars = rewardNum.toCharArray();
        List<Integer> list = new ArrayList<>();
        for (char c:chars
             ) {
            try {
                list.add(Integer.parseInt(String.valueOf(c)));
            }catch (Exception e){
                System.out.println(c);
                e.printStackTrace();
            }
        }
        List<Integer> rs = new ArrayList<>();
        readNums(list,rs);
        int maxCount = 1,count=1,count_2=1;
        if(rs.size()>0){
            if(rs.size()>1){
                count = rs.get(0);
                count_2 = rs.get(1);
                maxCount = count;
                if(count_2>count)
                    maxCount = count_2;
            }else{
                maxCount = rs.get(0);
                count = rs.get(0);
            }
        }

        if(maxCount == 1)
            this.group = 120;
        else if(count == 2&&count_2 == 2)
            this.group = 30;
        else if(maxCount == 2&&(count_2 == 1||count==1))
            this.group = 60;
        else if(maxCount == 3&&(count==1||count_2==1))
            this.group = 20;
        else if(maxCount == 3&&count>1&&count_2>1)
            this.group = 10;
        else if(maxCount == 4)
            this.group = 5;
    }

    private void readNums(List<Integer> list,List<Integer> result){
        int count = 1;
        int one = list.get(0);
        list.remove(0);
        List<Object> dels = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(one == list.get(i)){
                count++;
                dels.add(list.get(i));
            }
        }
        if(count>1){
            result.add(count);
            for(int i=0;i<dels.size();i++){
                list.remove(dels.get(i));
            }
        }
        if(list.size()>1)
            readNums(list,result);
    }


}
