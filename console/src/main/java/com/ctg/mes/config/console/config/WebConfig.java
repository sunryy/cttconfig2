package com.ctg.mes.config.console.config;

import com.ctg.cloud.paascommon.filter.PaaSCommonFilter;
import com.ctg.mes.config.common.constants.PaaSConstants;
import com.ctg.mes.config.common.filter.OperatorFilter;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 配置PaaS拦截器，实现校验请求合法性和解密JWT用户数据,
 * 为保证安全性，施工完成同步接口和提供给前端调用的接口都应该校验JWT合法性
 *
 * @author admin
 */
@Configuration
public class WebConfig {

   @Value("${debug}")
   private Boolean debug;

   @Bean
   public FilterRegistrationBean<PaaSCommonFilter> jwtFilterRegistration() {
      FilterRegistrationBean<PaaSCommonFilter> registration = new FilterRegistrationBean<>();
      registration.setFilter(new PaaSCommonFilter());

      //TODO 根据项目实际更改相关配置
      //设置过滤路径，/*所有路径.依据实际需要设置
      registration.addUrlPatterns("/*");
      //使用jwt校验的时候需要选择PaasCommon模式为jwt模式（必须设置）
      registration.addInitParameter(PaaSConstants.MODE, PaaSConstants.MODE_JWT);
      //如果是正常模式，需要设置jwt解密的秘钥。（debug模式不需设置，正常模式下缺少这一步启动启动会报错）
      //联调和生产环境密钥请与PaaS平台约定
      registration.addInitParameter(PaaSConstants.JWT_SECRET, "admin123456789");
      
      //是否debug调试模式。（只有设置为true认为是debug模式，缺省设置或设置其他值时，认为是非debug模式）
      registration.addInitParameter(PaaSConstants.IS_DEBUG,debug.toString());
    
      //如果是debug调试模式，userId等登录信息初始化filter时确定。（正常模式，这里不需要设置，设置也无效）
      registration.addInitParameter(PaaSConstants.DEBUG_PARAM_USER_ID, "288");
      registration.addInitParameter(PaaSConstants.DEBUG_PARAM_USER_NAME, "userName");
      registration.addInitParameter(PaaSConstants.DEBUG_PARAM_TENANT_ID, "3127");
      registration.addInitParameter(PaaSConstants.DEBUG_PARAM_TENANT_NAME, "tenantName");
      registration.addInitParameter(PaaSConstants.DEBUG_PARAM_ACCOUNT_TYPE, "0");
    
      //添加过滤白名单（可不设置）
      registration.addInitParameter(PaaSConstants.WHITELIST, "/MQTT/operOrder/**,/MQTT/console/check");

      
      registration.setName("paasCommonFilter");
      //设置优先级，数字越小越早
      registration.setOrder(0);
      return registration;
   }

   @Bean
   public FilterRegistrationBean<OperatorFilter> operatorFilterRegistration() {
      FilterRegistrationBean<OperatorFilter> registration = new FilterRegistrationBean<>();
      registration.setFilter(new OperatorFilter());

      //设置过滤路径，/*所有路径.依据实际需要设置
      registration.addUrlPatterns("/*");
      registration.setName("operatorFilter");
      //设置优先级，数字越小越早
      registration.setOrder(1);
      return registration;
   }

   private static final String TIME_FORMAT = "HH:mm:ss";
   private static final String DATE_FORMAT = "yyyy-MM-dd";
   private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

   @Bean
   public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
      return builder -> builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT)))
              .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT)))
              .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(TIME_FORMAT)))
              .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT)))
              .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_FORMAT)))
              .deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(TIME_FORMAT)));
   }

  

}
