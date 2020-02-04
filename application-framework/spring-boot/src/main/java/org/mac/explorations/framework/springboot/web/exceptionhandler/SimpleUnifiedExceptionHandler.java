/*
 *          (          (
 *          )\ )  (    )\   )  )     (
 *  (  (   (()/( ))\( ((_| /( /((   ))\
 *  )\ )\   ((_))((_)\ _ )(_)|_))\ /((_)
 * ((_|(_)  _| (_))((_) ((_)__)((_|_))
 * / _/ _ \/ _` / -_|_-< / _` \ V // -_)
 * \__\___/\__,_\___/__/_\__,_|\_/ \___|
 *
 * 东隅已逝，桑榆非晚。(The time has passed,it is not too late.)
 * 虽不能至，心向往之。(Although I can't, my heart is longing for it.)
 *
 */

package org.mac.explorations.framework.springboot.web.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @auther mac
 * @date 2020-02-03
 */
@ControllerAdvice
public class SimpleUnifiedExceptionHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleUnifiedExceptionHandler.class);
    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request,Exception e) {
        e.printStackTrace();
        LOGGER.error(Arrays.toString(e.getStackTrace()));
        Map<String,Object> ext = new LinkedHashMap<>();
        if(e instanceof RuntimeException) {
            ext.put("code", "0000");
        }

        /** @see SimpleCustomizedErrorAttributes#getErrorAttributes(WebRequest, boolean)  */
        request.setAttribute(SimpleCustomizedErrorAttributes.EXT_ERROR_ATTRIBUTES_NAME,ext);

        /**
         * 设置状态码
         * @see AbstractErrorController#getStatus(javax.servlet.http.HttpServletRequest)
         * SpringBoot 才能以状态码解析到对应错误的视图模板名或静态错误错误页
         */
        //没有自定义异常处理器 由WEB容器处理 WEB容器中是以javax.servlet.error.status_code为key设置对应状态码到request
        request.setAttribute("javax.servlet.error.status_code",HttpStatus.INTERNAL_SERVER_ERROR.value());
        // 转发给SpringBoot的错误处理流程处理,错误响应自适应
        return  "forward:/error";
    }
}