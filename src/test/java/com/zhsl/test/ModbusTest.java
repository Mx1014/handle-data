package com.zhsl.test;

import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;
import com.zhsl.data.util.Modbus4jUtils;
import com.zhsl.data.util.ModbusHandler;
import org.junit.Test;

/**
 * @author xiangjg
 * @date 2019/5/23 9:53
 */
public class ModbusTest {

    @Test
    public void read() {
        try {
            BaseLocator<Boolean> loc = BaseLocator.coilStatus(1, 10);
            Boolean value = Modbus4jUtils.getMaster().getValue(loc);
            System.out.println(value);

            ByteQueue result = ModbusHandler.modbusRTCP("192.168.100.236", 502, 1,
                    0, 10);
            ModbusHandler.ansisByteQueue(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void write() {
        try {
            short[] shor = new short[2];
            shor[0] = 0x37;
            shor[1] = 0x30;
            ModbusHandler.modbusWTCP("192.168.100.236", 502, 1, 0, shor);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
