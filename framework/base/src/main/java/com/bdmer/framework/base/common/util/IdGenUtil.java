package com.bdmer.framework.base.common.util;

/**
 * ID生成器
 * ****************************************************************************************************
 * Twitter_Snowflake SnowFlake的结构如下(每部分用-分开): 0 - 0000000000 0000000000 0000000000 0000000000 0 -
 * 00000 - 00000 - 000000000000 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T =
 * (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号 加起来刚好64位，为一个Long型。
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 *
 * ****************************************************************************************************
 * IdWorker的算法 0 - 0000000000 0000000000 0000000000 0000000000 - 0000000000 0000 - 000000000
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
 * 40位时间截(毫秒级)，注意，40位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。40位的时间截，可以使用34年，年T =
 * (1L << 40) / (1000L * 60 * 60 * 24 * 365) = 34 14位的服务位，可以部署在16384个服务,
 * 7位序列，毫秒内的计数，9位的计数顺序号支持每个节点每毫秒(同一服务，同一时间截)产生512个ID序号 加起来刚好64位，为一个Long型。
 * DifferIdWorker的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由服务)，并且效率较高。 <br>
 * <br>
 *
 * @author GongDeLang
 * @since 2020/6/1 19:01
 */
public class IdGenUtil {
    private long workerId;
    private long datacenterId;
    private long sequence = 0L;
    /**
     * Thu, 04 Nov 2010 01:42:54 GMT
     */
    private long twepoch = 1288834974657L;
    /**
     * 节点ID长度
     */
    private long workerIdBits = 5L;
    /**
     * 数据中心ID长度
     */
    private long datacenterIdBits = 5L;
    /**
     *  最大支持机器节点数0~31，一共32个
     */
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);
    /**
     * 最大支持数据中心节点数0~31，一共32个
     */
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    /**
     * 序列号12位
     */
    private long sequenceBits = 12L;
    /**
     * 机器节点左移12位
     */
    private long workerIdShift = sequenceBits;
    /**
     * 数据中心节点左移17位
     */
    private long datacenterIdShift = sequenceBits + workerIdBits;
    /**
     * 时间毫秒数左移22位
     */
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    /**
     * 4095
     */
    private long sequenceMask = -1L ^ (-1L << sequenceBits);
    private long lastTimestamp = -1L;

    private static class IdGenHolder {
        private static final IdGenUtil instance = new IdGenUtil();
    }

    public static IdGenUtil get() {
        return IdGenHolder.instance;
    }

    public IdGenUtil() {
        this(0L, 0L);
    }

    public IdGenUtil(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public static long getId(){
        return IdGenUtil.get().nextId();
    }

    public synchronized long nextId() {
        //获取当前毫秒数
        long timestamp = timeGen();
        //如果服务器时间有问题(时钟后退) 报错。
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        //如果上次生成时间和当前时间相同,在同一毫秒内
        if (lastTimestamp == timestamp) {
            //sequence自增，因为sequence只有12bit，所以和sequenceMask相与一下，去掉高位
            sequence = (sequence + 1) & sequenceMask;
            //判断是否溢出,也就是每毫秒内超过4095，当为4096时，与sequenceMask相与，sequence就等于0
            if (sequence == 0) {
                //自旋等待到下一毫秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        // 最后按照规则拼出ID。
        // 000000000000000000000000000000000000000000  00000            00000       000000000000
        // time                                       datacenterId   workerId    sequence
        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift) | sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }
}