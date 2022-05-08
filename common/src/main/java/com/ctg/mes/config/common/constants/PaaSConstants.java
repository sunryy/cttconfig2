package com.ctg.mes.config.common.constants;


import com.ctg.cloud.paascommon.common.StringConstant;


/**
 * @author chenjj
 */
public class PaaSConstants extends StringConstant {
    public static final String WHITELIST ="whitelist";
    public static final String JWT_SECRET ="jwtSecret";
    public static final String DEBUG_PARAM_USER_ID ="userId";
    public static final String DEBUG_PARAM_USER_NAME = "userName";
    public static final String DEBUG_PARAM_TENANT_ID = "tenantId";
    public static final String DEBUG_PARAM_TENANT_NAME = "tenantName";
    public static final String DEBUG_PARAM_ACCOUNT_TYPE = "accountType";

    /**
     * 推广
     */
    public static final String NATIONAL_PLATFORM = "national";
    /**
     * 上云
     */
    public static final String CTYUN_PLATFORM = "ctyun";

    //工单类型
    /** 开通 */
    public static final String OPER_TYPE_CREATE="01";
    /** 暂停 */
    public static final String OPER_TYPE_PAUSE="11";
    public static final String OPER_TYPE_PAUSE_PAAS="20";
    /** 恢复 */
    public static final String OPER_TYPE_RESUME="12";
    /** 注销 */
    public static final String OPER_TYPE_CANCEL="13";
    /** 属性变更 */
    public static final String OPER_TYPE_MODIFY="14";

    /** 扩容预处理*/
    public static final String OPER_TYPE_PRE_EXPAND="15";

    /** 扩容恢复*/
    public static final String OPER_TYPE_EXPAND="16";



    public enum IPTypeEnum {

        /**
         * ipv4
         */
        IPV4(1),
        /**
         * ipv6
         */
        IPV6(2);

        private final int type;

        IPTypeEnum(int type) {
            this.type=type;
        }

        public int getType(){
            return this.type;
        }
    }

    public enum IPUsingTypeEnum {

        /**
         * 管理域IP
         */
        MANAGER(1),
        /**
         * 集群内部通讯IP
         */
        INNER(2),

        /**
         * 用户访问IP
         */

        ACCESS(3);

        private final int type;

        IPUsingTypeEnum(int type) {
            this.type=type;
        }

        public int getType(){
            return this.type;
        }
    }


    public enum OrderStatusEnum {

        /**
         * 通知状态
         */
        NOTICING(-1),

        NOTICE_CANCEL(-2),
        /**
         * 创建中
         */
        CREATING(0),
        /**
         * 运行中
         */
        RUNNING(1),
        /**
         * 已过期
         */
        EXPIRED(2),
        /**
         *  注销
         */
        CANCELED(3),

        /**
         * 施工失败
         */

        FAILED(4),
        /**
         * 扩容中
         */

        EXPANDING(5);

        private final int status;

        OrderStatusEnum(int status) {
            this.status=status;
        }

        public int status(){
            return this.status;
        }
    }



    /**
     * 订单报竣状态吗
     */
    public static class EcloudStatusCode {
        public static final int OK = 200;
        public static final int SUCCESS = 800;
        public static final int FAIL = 900;
    }

}
