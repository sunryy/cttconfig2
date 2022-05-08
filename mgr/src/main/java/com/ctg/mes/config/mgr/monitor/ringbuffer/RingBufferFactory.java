package com.ctg.mes.config.mgr.monitor.ringbuffer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RingBufferFactory {

    private static ConcurrentMap<String, MemRingBuffer> ringBufferMap = new ConcurrentHashMap<String, MemRingBuffer>();

    private static final String MONITOR_RING_BUFFER = "monitorRingBuffer";

    public static MemRingBuffer getMonitorRingBuffer() {
        return getMonitorRingBuffer(MemRingBuffer.defaultLength, MemRingBuffer.defaultResetMillis);
    }

    public static MemRingBuffer getMonitorRingBuffer(int length, long resetMillis) {
        MemRingBuffer memRingBuffer = ringBufferMap.get(MONITOR_RING_BUFFER);
        if (memRingBuffer != null) {
            return memRingBuffer;
        } else {
            ringBufferMap.put(MONITOR_RING_BUFFER, new MemRingBuffer<Map<String, Map<String, String>>>(length, resetMillis));
        }
        return ringBufferMap.get(MONITOR_RING_BUFFER);
    }

}
