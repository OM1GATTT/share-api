package top.om1ga.share.common.util;

import cn.hutool.core.util.IdUtil;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 16:04
 * @description SnowUtil 封装 hutool 雪花算法
 */
public class SnowUtil {
    /**
     * 数据中心
     */
    private static final long DATA_CENTER_ID = 1;

    /**
     * 机器标识
     */
    private static final long WORKER_ID = 1;

    public static long getSnowflakeNextId() {
        return IdUtil.getSnowflake(WORKER_ID, DATA_CENTER_ID).nextId();
    }

    public static String getSnowflakeNextIdStr() {
        return IdUtil.getSnowflake(WORKER_ID, DATA_CENTER_ID).nextIdStr();
    }
}
