package com.zhsl.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhsl.data.entity.HeaderVO;
import com.zhsl.data.entity.StationVo;
import com.zhsl.data.util.ExcelExportUtil;
import com.zhsl.data.util.HttpUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xiangjg
 * @date 2018/9/3 15:08
 */
public class ExpRiverTest {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void expRiver() {
        String param = "{arguments:[\"10000003,10000004,10000005\",\"2018-08-21 00:00:00\",\"2018-08-25 23:59:59\"],method:\"getRiverProcessByStcds\"}";
        List<StationVo> stationVoList = new ArrayList<>();
        stationVoList.add(new StationVo("10000003", "坝下厂房"));
        stationVoList.add(new StationVo("10000004", "洪渡河干流半坡处"));
        stationVoList.add(new StationVo("10000005", "支流黄都河八里溪"));
        JSONArray expData = new JSONArray();
        try {
            String ret = HttpUtil.doPost("http://42.123.116.200:8083/com.zhsl.waterrain.service.RiverService", param);
            System.out.println("接口调用完成");
            JSONObject jsonObject = JSON.parseObject(ret);
            JSONArray list = jsonObject.getJSONArray("data");
            for (int i = list.size() - 1; i >= 0; i--) {
                JSONObject j = list.getJSONObject(i);
                Map<String, Object> map = new HashMap<>();
                String time = sdf.format(new Date(j.getLongValue("tm")));
                map.put("tm", time);
                JSONArray jsonArray = j.getJSONArray("data");
                for (StationVo s : stationVoList
                        ) {
                    JSONObject jsonObject1 = getData(jsonArray, s.getStcd());
                    if (jsonObject1 != null) {
                        map.put("z" + s.getStcd(), jsonObject1.getBigDecimal("z").setScale(2, BigDecimal.ROUND_HALF_UP));
                        map.put("q" + s.getStcd(), jsonObject1.getDoubleValue("q"));
                    } else {
                        map.put("z" + s.getStcd(), "");
                        map.put("q" + s.getStcd(), "");
                    }

                }
                expData.add(map);
            }
            System.out.println("数据组装完成");
            List<HeaderVO> head = new ArrayList<>();
            head.add(new HeaderVO("tm", "时间"));
            for (StationVo s : stationVoList
                    ) {
                head.add(new HeaderVO("z" + s.getStcd(), s.getStnm() + "水位"));
                head.add(new HeaderVO("q" + s.getStcd(), s.getStnm() + "流量"));
            }
            ExcelExportUtil.downloadExcelFile("水位站数据汇总", head, expData, "E:\\水位站数据汇总.xlsx");
            System.out.println("数据导出文件已生成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject getData(JSONArray jsonArray, String stcd) {
        JSONObject jsonObject = null;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject j = jsonArray.getJSONObject(i);
            if (stcd.equals(j.getString("stcd"))) {
                jsonObject = j;
                break;
            }
        }
        return jsonObject;
    }

    @Test
    public void ExpRain() {
        String param = "{arguments:[\"10000007,10000008,10000009,10000010,10000011,10000012\",\"2018-08-20 00:00:00\",\"2018-08-25 23:59:59\"],method:\"getRainProcessByStcds\"}";
        List<StationVo> stationVoList = new ArrayList<>();
        stationVoList.add(new StationVo("10000007", "西河(湄潭)"));
        stationVoList.add(new StationVo("10000008", "谢坝(正安)"));
        stationVoList.add(new StationVo("10000009", "永安(凤岗)"));
        stationVoList.add(new StationVo("10000010", "新建(凤岗)"));
        stationVoList.add(new StationVo("10000011", "土溪(凤岗)"));
        stationVoList.add(new StationVo("10000012", "黄都(务川)"));
        JSONArray expData = new JSONArray();
        try {
            String ret = HttpUtil.doPost("http://42.123.116.200:8083/com.zhsl.waterrain.service.RainService", param);
            System.out.println("接口调用完成");
            JSONObject jsonObject = JSON.parseObject(ret);
            JSONArray list = jsonObject.getJSONArray("data");
            for (int i = list.size() - 1; i >= 0; i--) {
                JSONObject j = list.getJSONObject(i);
                Map<String, Object> map = new HashMap<>();
                String time = sdf.format(new Date(j.getLongValue("tm")));
                map.put("tm", time);

                JSONArray jsonArray = j.getJSONArray("data");
                for (StationVo s : stationVoList
                        ) {
                    JSONObject jsonObject1 = getData(jsonArray, s.getStcd());
                    if (jsonObject1 != null) {
                        map.put("drp" + s.getStcd(), jsonObject1.getBigDecimal("drp").setScale(1, BigDecimal.ROUND_DOWN));
                    } else {
                        map.put("drp" + s.getStcd(), "0.0");
                    }
                }
                expData.add(map);
            }
            System.out.println("数据组装完成");
            List<HeaderVO> head = new ArrayList<>();
            head.add(new HeaderVO("tm", "时间"));
            for (StationVo s : stationVoList
                    ) {
                head.add(new HeaderVO("drp" + s.getStcd(), s.getStnm()));
            }
            ExcelExportUtil.downloadExcelFile("雨量站数据汇总", head, expData, "E:\\雨量站数据汇总.xlsx");
            System.out.println("数据导出文件已生成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
