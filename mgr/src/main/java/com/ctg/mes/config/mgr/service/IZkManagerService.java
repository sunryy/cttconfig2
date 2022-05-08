package com.ctg.mes.config.mgr.service;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IZkManagerService {
    Map<String,String> getParams();

    void updateParams(HashMap<String,String> params);

    void startNode();

    void restartNode();

    void stopNode();

    Pair<List<String>, List<String>> getNodeStatus();
}
