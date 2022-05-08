package com.ctg.mes.config.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于执行本地命令
 */
public class CmdUtil {
    private static final Logger log = LoggerFactory.getLogger(CmdUtil.class);

    public static Pair<List<String>, List<String>> exec(String cmd) throws IOException {
        return exec(cmd, true);
    }

    public static Pair<List<String>, List<String>> exec(String cmd, boolean write2log) {
        if (write2log) {
            log.info("Execute local command: {}", cmd);
        }
        String[] cmds = {"/bin/sh", "-c", cmd}; //这种方式才能解决本地执行的管道问题
        try {
            Process process = Runtime.getRuntime().exec(cmds);
            List<String> response = getResponse(process.getInputStream());
            List<String> errorResponse = getResponse(process.getErrorStream());
            if (log.isDebugEnabled()) {
                log.debug("std response : {}", StringUtils.join(response, "\n"));
                log.debug("error response : {}", StringUtils.join(errorResponse, "\n"));
            }

            return Pair.of(response,errorResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> getResponse(InputStream input) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        List<String> list = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
        return list;
    }
}
