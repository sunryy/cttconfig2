package com.ctg.mes.config.mgr.service.impl;

import com.ctg.mes.config.common.exception.BusinessException;
import com.ctg.mes.config.common.util.CmdUtil;
import com.ctg.mes.config.common.util.FileUtil;
import com.ctg.mes.config.mgr.service.IZkManagerService;
import com.google.common.base.Strings;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class ZkManagerServiceImpl implements IZkManagerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkManagerServiceImpl.class);
    private static final String CONF_FILE_PATH = "/conf/zoo.cfg";

    @Value("${zookeeper.install-path:}")
    private String zkInstallPath;

    @Override
    public Map<String,String> getParams() {
        try {
            checkZkInstallPathIsValid();
            String confPath = zkInstallPath + CONF_FILE_PATH;
            return getConfContent(confPath);
        }catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public void updateParams(HashMap<String,String> params) {
        try {
            checkZkInstallPathIsValid();
            String confPath = zkInstallPath + CONF_FILE_PATH;
            String content = replaceConfContent(confPath, params);
            updateConfContent(confPath, content);
        }catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public void startNode() {
        String scriptFile = "zkServer.sh";
        String[] args =  new String[]{"start"};
        executeScript(scriptFile, args);
        logStatusResult();
    }

    @Override
    public void restartNode() {
        String scriptFile = "zkServer.sh";
        String[] args =  new String[]{"restart"};
        executeScript(scriptFile, args);
        logStatusResult();
    }

    @Override
    public void stopNode() {
        String scriptFile = "zkServer.sh";
        String[] args =  new String[]{"stop"};
        executeScript(scriptFile, args);
        logStatusResult();
    }

    @Override
    public Pair<List<String>, List<String>> getNodeStatus() {
        String scriptFile = "zkServer.sh";
        String[] args =  new String[]{"status"};
        Pair<List<String>, List<String>> result = executeScript(scriptFile, args);
        return result;
    }

    /**
     * 检测zk的路径配置是否有效
     */
    private void checkZkInstallPathIsValid() {
        if (Strings.isNullOrEmpty(zkInstallPath)) {
            throw new RuntimeException("zookeeper install path is null or empty, Please check the configuration parameter {zookeeper.install-path}.");
        }
    }

    /**
     *  获取配置文件内容
     */
    private static Map<String,String> getConfContent(String confPath) throws IOException {
        Map<String,String> params = new HashMap<String,String>();
        FileInputStream fis = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(confPath);
            br = new BufferedReader( new InputStreamReader(fis) );
            String data = null;
            while ( ( data = br.readLine() ) != null ) {
                //排除已经注释的参数
                if (data.trim().startsWith("#")) {
                    continue;
                }
                String[] arr = data.split("=");
                if (arr.length == 2) {
                    params.put(arr[0], arr[1]);
                }
            }
        } catch ( IOException e ) {
            throw new IOException( "读取文件异常: " + e.getMessage() );
        } finally {
            try {
                fis.close();
                br.close();
            } catch ( IOException e ) {
            }
        }
        return params;
    }

    /**
     *  替换配置文件内容
     */
    private static String replaceConfContent(String confPath, HashMap<String,String> newParams) throws IOException {
        FileInputStream fis = null;
        BufferedReader br = null;
        StringBuilder content = new StringBuilder();
        try {
            fis = new FileInputStream(confPath);
            br = new BufferedReader( new InputStreamReader(fis) );
            String data = null;
            while ( ( data = br.readLine() ) != null ) {
                String[] arr = data.split("=");
                if (arr.length == 2) {
                    String val = newParams.put(arr[0], arr[1]);; //更新参数
                    data = arr[0] + "=" + val;
                }
                content.append( data ).append( "\n" );
            }
        } catch ( IOException e ) {
            throw new IOException( "读取文件异常: " + e.getMessage() );
        } finally {
            try {
                fis.close();
                br.close();
            } catch ( IOException e ) {
            }
        }
        return content.toString();
    }

    /**
     *  更新配置文件内容
     */
    private static void updateConfContent(String confPath, String content) throws IOException {
        FileUtil.write(confPath, content, false);
    }


    /**
     * 执行本地脚本
     * @param scriptFile
     * @param args
     * @return
     */
    public Pair<List<String>, List<String>> executeScript(String scriptFile, Object... args) {
        StringBuilder stringBuilder = new StringBuilder(scriptFile);
        for (Object arg : args) {
            stringBuilder.append(" ").append(arg);
        }
        String scriptPath = zkInstallPath + "/bin";
        Pair<List<String>, List<String>> resultPair = null;
        try {
            String cmd = String.format("sh %s/%s", scriptPath, stringBuilder.toString());
            LOGGER.info(">>>>>>>>>>>>>>>cmd:" + cmd);
            resultPair = CmdUtil.exec(cmd);
            return resultPair;
        } catch (Exception e) {
            LOGGER.error("Error executing file:[" + scriptFile + "] with args:" + Arrays.toString(args), e);
        }
        return resultPair;
    }

    /**
     *  打印出状态结果
     */
    private void logStatusResult() {
        Pair<List<String>, List<String>> nodeStatus = getNodeStatus();
        if (nodeStatus == null) {
            LOGGER.error("can not get node status.");
        }

        List<String> result = new ArrayList<>();
        result.addAll(nodeStatus.getLeft());
        result.addAll(nodeStatus.getRight());

        for (String r : result) {
            if (r.contains("It is probably not running")) {
                LOGGER.info("Node is already stopped.");
                return;
            } else if (r.contains("Mode:")) {
                LOGGER.info("Node is already started.");
                return;
            }
        }
        LOGGER.warn("Unknown state found. result = {}", Arrays.toString(result.toArray()));
    }

}
