package com.sparkchain.aries.chain;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.gson.Gson;
import com.sparkchain.framework.Constants;
import com.sparkchain.framework.RetData;
import com.sparkchain.framework.exception.BaseException;
import com.sparkchain.framework.utils.StringUtils;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.alibaba.fastjson.support.config.FastJsonConfig;
//import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
//import com.mps.employeeveh.core.CentralException;
//import com.mps.employeeveh.core.Result;
//import com.mps.employeeveh.core.ResultCode;

@Configuration
public class WebMvcConfigs extends WebMvcConfigurerAdapter {
	private final Logger logger = LoggerFactory.getLogger(WebMvcConfigs.class);

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

		// GsonHttpMessageConverter gsonHttpMessageConverter = new
		// GsonHttpMessageConverter();
		// gsonHttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));
		// converters.add(gsonHttpMessageConverter);
		//
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		FastJsonConfig config = new FastJsonConfig();
		config.setSerializerFeatures(SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.WriteNullNumberAsZero);
		converter.setFastJsonConfig(config);
		converter.setDefaultCharset(Charset.forName("UTF-8"));
		converters.add(converter);
	}

	/**
	 * 统一异常处理
	 *
	 * @param exceptionResolvers
	 */
	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		exceptionResolvers
				.add((HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) -> {
					RetData result = new RetData();
					if (handler instanceof HandlerMethod) {
						HandlerMethod handlerMethod = (HandlerMethod) handler;
						if (e instanceof BaseException) {
							result.setCode(Constants.RETDATA_FAIL + "")
									.setMessage(e.getMessage());
//									.setMessage(StringUtils.exception(e));
							logger.info(e.getMessage());
							responseResult(response, result);
							return new ModelAndView();
						} else if (e instanceof MissingServletRequestParameterException) {
							result.setCode(Constants.RETDATA_INTERNAL_SERVER_ERROR + "")
									.setMessage("接口 [" + request.getRequestURI()
											+ "] 内部错误，请联系管理员,异常摘要：要求的参数没有传入或传入的参数名称不对");
							String message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：要求的参数没有传入或传入的参数名称不对",
									request.getRequestURI(),
									handlerMethod.getBean().getClass().getName(),
									handlerMethod.getMethod().getName(),
									e.getMessage());
							logger.error(message, e);
							responseResult(response, result);
							return new ModelAndView();

						} else {
							result.setCode(Constants.RETDATA_INTERNAL_SERVER_ERROR + "")
									.setMessage(
											"接口 [" + request.getRequestURI() + "] 内部错误，请联系管理员,错误内容为:"
													+ StringUtils.exception(e));
							String message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
									request.getRequestURI(),
									handlerMethod.getBean().getClass().getName(),
									handlerMethod.getMethod().getName(),
									e.getMessage());
							logger.error(message, e);
							responseResult(response, result);
							return new ModelAndView();
						}
						// responseResult(response, result);
						// return new ModelAndView();
					} else {
						if (e instanceof NoHandlerFoundException) {

							result.setCode(Constants.RETDATA_NOT_FOUND + "")
									.setMessage("接口 [" + request.getRequestURI() + "] 不存在");
						} else {
							result.setCode(Constants.RETDATA_INTERNAL_SERVER_ERROR + "")
									.setMessage(StringUtils.exception(e));
							logger.error(e.getMessage(), e);
						}
					}
					responseResult(response, result);
					return new ModelAndView();
				});
	}

	/**
	 * 统一处理响应
	 *
	 * @param response
	 * @param result
	 */
	private void responseResult(HttpServletResponse response, RetData result) {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type", "application/json;charset=UTF-8");
		response.setStatus(200);
		try {
			response.getWriter().write(new Gson().toJson(result));
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
	}

	/**
	 * @param request
	 *            HTTP请求
	 * @return 获取到的IP
	 */
	private String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		String IPPRE = "unknown";
		if (ip == null || ip.length() == 0 || IPPRE.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || IPPRE.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || IPPRE.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || IPPRE.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || IPPRE.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		// 如果是多级代理，那么取第一个ip为客户ip
		if (ip != null && ip.contains(",")) {
			ip = ip.substring(0, ip.indexOf(",")).trim();
		}
		return ip;
	}
}
