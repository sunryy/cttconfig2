package com.ctg.mes.config.common.util;

/**
 * 单位换算
 */
public enum StorageUnit {

    BYTE {
        public double fromBytes(long storage) {
            return storage;
        }

        public String getUnit() {
            return "B";
        }
    },

    KB {
        public double fromBytes(long storage) {
            return keepPoint(storage * 1.0 / KB_BYTES);
        }

        public String getUnit() {
            return "KB";
        }
    },

    MB {
        public double fromBytes(long storage) {
            return keepPoint(storage * 1.0 / MB_BYTES);
        }

        public String getUnit() {
            return "MB";
        }
    },

    GB {
        public double fromBytes(long storage) {
            return keepPoint(storage * 1.0 / GB_BYTES);
        }

        public String getUnit() {
            return "GB";
        }
    },

    TB {
        public double fromBytes(long storage) {
            return keepPoint(storage * 1.0 / TB_BYTES);
        }

        public String getUnit() {
            return "TB";
        }
    };

    public static final long KB_BYTES = 1024;
    public static final long MB_BYTES = 1024 * 1024;
    public static final long GB_BYTES = 1024 * 1024 * 1024;
    public static final long TB_BYTES = 1024 * 1024 * 1024 * 1024L;

    public abstract double fromBytes(long bytes);

    public abstract String getUnit();

    /**
     * 保留2位小数
     */
    public static double keepPoint(double value) {
        return (double) Math.round(value * 100) / 100;
    }
}
