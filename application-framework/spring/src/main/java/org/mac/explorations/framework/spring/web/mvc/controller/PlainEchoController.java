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

package org.mac.explorations.framework.spring.web.mvc.controller;

import org.mac.explorations.framework.spring.web.mvc.WebMVCApplicationContextConfiguration;
import org.mac.explorations.framework.spring.web.mvc.controller.bean.User;
import org.mac.explorations.framework.spring.web.mvc.service.PlainEchoService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @auther mac
 * @date 2020-01-16
 */
@Controller
public class PlainEchoController implements ApplicationContextAware {

    private PlainEchoService echoService;

    public PlainEchoController(@Autowired PlainEchoService echoService) {
        this.echoService = echoService;
    }

    /**
     * Ant 风格资源地址支持 3 种匹配符：
     *
     * – ?：匹配文件名中的一个字符
     * – *：匹配文件名中的任意字符
     * – **：** 匹配多层路径
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping("/echo/{message}")
    public String echo(@PathVariable("message") String message) {
        return echoService.echo(message);
    }

    /**
     * @see org.springframework.web.filter.HiddenHttpMethodFilter
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/echo/{message}",method = RequestMethod.PUT)
    public String putTypeEcho(@PathVariable("message") String message){
        System.err.println("--->/echo/{message} method = RequestMethod.PUT");
        return echoService.echo(message);
    }

    @ResponseBody
    @RequestMapping(value = "/echo/pojo")
    public User echoPojo(@Validated User user){
        return user;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("BeanDefinitionCount:"+applicationContext.getBeanDefinitionCount());
        DelegatingWebMvcConfiguration bean = applicationContext.getBean(DelegatingWebMvcConfiguration.class);
        System.out.println(bean);
        WebMVCApplicationContextConfiguration customConfigurationBean =  applicationContext.getBean(WebMVCApplicationContextConfiguration.class);
        System.out.println(customConfigurationBean);
        List<String> beanNames = Arrays.asList(
                "mvcContentNegotiationManager",
                "mvcConversionService",
                "mvcResourceUrlProvider",
                "requestMappingHandlerMapping",
                "mvcPathMatcher",
                "mvcUrlPathHelper",
                "viewControllerHandlerMapping",
                "beanNameHandlerMapping",
                "routerFunctionMapping",
                "resourceHandlerMapping",
                "defaultServletHandlerMapping",
                "mvcValidator",
                "requestMappingHandlerAdapter",
                "handlerFunctionAdapter",
                "mvcUriComponentsContributor",
                "httpRequestHandlerAdapter",
                "simpleControllerHandlerAdapter",
                "handlerExceptionResolver",
                "mvcViewResolver"
        );
        for (String beanName:beanNames) {
            if("mvcConversionService".endsWith(beanName)){
                System.out.println(applicationContext.getBean(beanName).getClass().getName());
            }
            else {
                System.out.printf("%35s->:%s\n", beanName, applicationContext.getBean(beanName));
            }
        }
    }
}
