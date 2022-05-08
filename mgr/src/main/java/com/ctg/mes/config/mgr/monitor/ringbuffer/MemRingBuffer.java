package com.ctg.mes.config.mgr.monitor.ringbuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MemRingBuffer<T> {
    private static final Logger logger = LoggerFactory.getLogger(MemRingBuffer.class);
    /**
     环形数组缓存监控数据，初始化时定长数组
     监控数据将map<string Map<string,string>> 结构对象写入数组内
     写缓存，新内容写入当前下标的位置，读下标+1. 如果达到数组最后，则跳到数组开头
     读缓存，读取全部数据, 从读下标读到写下标
     写缓存，若写满则重置读下标，若超时未读取，则重置读下票
     */

    public static final int defaultLength  = 10000;
    public static final long defaultResetMillis = 10*60*1000L;  //10分钟未读取重置

    private final int length;        //最大长度,数组容量
    private T[] data;                //环形数组
    private int readPos;             //读指针,下一次读操作执行的位置 值为递增,使用时求余得到数组下标
    private int writePos;            //写指针,下一次写操作执行的位置 值为递增,使用时求余得到数组下标
    private long recentReadMillis;   //最近一次读的时间豪秒
    private long resetMillis;        //长时间未读取重置数据

    public MemRingBuffer(){
        this.length = defaultLength;
        this.resetMillis = defaultResetMillis;
        this.recentReadMillis = 0;
        data = (T[])new Object[length];
        readPos = writePos = 0;
    }


    public MemRingBuffer(int length, long resetMillis) {
        this.length = length;
        this.resetMillis = resetMillis;
        this.recentReadMillis  = 0;
        data = (T[])new Object[length];
        readPos = writePos = 0;
    }

    public synchronized void clear(){
        data = (T[])new Object[length];
        readPos = writePos = 0;
    }

    /**
     * 获取未读记录数
     * @return 未读记录数
     */
    public int size(){
        return writePos - readPos;
    }

    /**
     * 写入数据
     * @param object 写入对象
     */
    public synchronized void add(T object){
        int write = writePos % length;
        int read = readPos % length;
        long currentMillis = System.currentTimeMillis();

        if(write == read  && writePos != readPos){
            //已写满, 重置读指针, 相当于读取清空
            logger.info("reset readPos. writePos:{}, readPos:{}, length:{}", writePos, readPos, length);
            readPos = writePos;
            recentReadMillis = currentMillis;

        }

        if(recentReadMillis > 0 && currentMillis - recentReadMillis > resetMillis){
            //超时未读取, 重置读指针, 相当于读取清空
            logger.info("reset readPos. currentMillis:{}, recentReadMillis:{}, resetMillis:{}", currentMillis, recentReadMillis, resetMillis);
            readPos = writePos;
            recentReadMillis = currentMillis;
        }

        data[write] = null;
        data[write] = object;
        writePos++;
        logger.debug("write to buffer ok, writePos:{}, readPos:{}", writePos, readPos);
    }

    /**
     * 取未读全部数据
     * @return  未读数据集
     */
    public List<T> getBuffer(){
        int writeCurrVal = writePos;
        int readCurrVal = readPos;
        int write =  writeCurrVal % length;
        int read = readCurrVal % length;

        List<T> buffer = new ArrayList<>();
        if(write == read){
            //没有新的数据
            return buffer;
        }

        if(read < write ){
            //读取  read -> write 部分
            for(int i=read; i<write; i++){
                buffer.add(data[i]);
            }
        }else{
            //读取  read -> 末尾  及  0 -> write  两部分
            for(int i=read; i<length; i++){
                buffer.add(data[i]);
            }
            for(int j=0; j<write; j++){
                buffer.add(data[j]);
            }
        }

        //重置读指针
        setReadPosAfterGetData(writeCurrVal);
        return buffer;
    }

    /**
     * 读取数据后，重置读指针
     * @param newReadPos 新的读指针位置
     */
    private synchronized void setReadPosAfterGetData(int newReadPos){
        recentReadMillis =  System.currentTimeMillis();   //设置最近读取
        if(readPos >= newReadPos){
            return;
        }
        readPos = newReadPos;
        if(readPos > length*10000){
            readPos -= length*10000;
            writePos -= length*10000;
            logger.info("readPos more than length*10000, reset readPost={}, writePos={}", readPos, writePos);
        }
    }

}
