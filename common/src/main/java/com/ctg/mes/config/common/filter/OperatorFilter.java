package com.ctg.mes.config.common.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenjj
 */
public class OperatorFilter implements Filter {

    private final static Logger operatorLog= LoggerFactory.getLogger("operatorLog");
    private final static String TAB_CHAR="\t";

    @Override
    public void init(FilterConfig filterConfig)  {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        long startTime=System.currentTimeMillis();
        chain.doFilter(request,response);
        long endTime=System.currentTimeMillis();
        writeOperatorLog(httpRequest,httpResponse,endTime-startTime);
    }

    @Override
    public void destroy() {

    }

    private static Map<String,String> getRequestMap(HttpServletRequest httpServletRequest) {
        Map<String,String> map= new HashMap<>(16);
        Enumeration<String> parameterNames=httpServletRequest.getParameterNames();
        String param;
        while(parameterNames.hasMoreElements()){
            param=parameterNames.nextElement();
            map.put(param, httpServletRequest.getParameter(param));
        }
        return map;
    }
    private void writeOperatorLog(HttpServletRequest httpRequest,HttpServletResponse httpResponse,long cost){
//        String uri=httpRequest.getRequestURI();
//
//        UserInfo userInfo = SecurityUtils.getUserInfo();
//
//        StringBuilder sb=new StringBuilder();
//        sb.append(DateUtils.format(LocalDateTime.now())).append(TAB_CHAR);
//        sb.append(uri).append(TAB_CHAR);
//        if(userInfo!=null) {
//            sb.append(userInfo.getUserId() == null ? 0 : userInfo.getUserId()).append(TAB_CHAR);
//            sb.append(userInfo.getTenantId() == null ? 0 : userInfo.getTenantId()).append(TAB_CHAR);
//        }
//        sb.append(JSONObject.toJSONString(getRequestMap(httpRequest))).append(TAB_CHAR);
//        sb.append(httpResponse.getStatus()).append(TAB_CHAR);
//        sb.append(cost).append(TAB_CHAR);
//        operatorLog.info(sb.toString());

    }
}
