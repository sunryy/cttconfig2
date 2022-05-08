package com.ctg.mes.config.console.service.zookeeper;


import com.google.gson.internal.LinkedTreeMap;

import java.util.HashMap;

public interface IZkParamService {

    /**
     * 获取参数列表信息
     */
    public LinkedTreeMap getParams(String instId);

    /**
     * 更新参数列表信息
     */
    public boolean updateParams(String instId, HashMap<String,String> params);
}
