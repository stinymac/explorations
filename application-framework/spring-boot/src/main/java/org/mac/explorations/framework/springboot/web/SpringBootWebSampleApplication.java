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

package org.mac.explorations.framework.springboot.web;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * SpringBoot对静态资源的映射
 *
 * @see org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry)
 * <pre>
 *     if (!registry.hasMappingForPattern("/webjars/**")) {
 *         customizeResourceHandlerRegistration(
 *             registry.addResourceHandler("/webjars/**")
 * 					   .addResourceLocations("classpath:/META-INF/resources/webjars/")
 * 					   .setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
 *     }
 *     // 默认是 /**
 *     String staticPathPattern = this.mvcProperties.getStaticPathPattern();
 * 	   if (!registry.hasMappingForPattern(staticPathPattern)) {
 * 	       customizeResourceHandlerRegistration(
 * 	           registry.addResourceHandler(staticPathPattern)
 * 	                   // @see {@link org.springframework.boot.autoconfigure.web.ResourceProperties#CLASSPATH_RESOURCE_LOCATIONS}
 * 	                   // "classpath:/META-INF/resources/","classpath:/resources/", "classpath:/static/", "classpath:/public/"
 * 					   .addResourceLocations(getResourceLocations(this.resourceProperties.getStaticLocations()))
 * 				       .setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
 *    }
 *
 *    即以jar包引入的静态资源(如 compile group: 'org.webjars', name: 'jquery')默认路径为classpath:/META-INF/resources/webjars/
 *    其访问路径为/webjars/** 例如当前应用中访问jquery.js的URL为 http://localhost:8080/webjars/jquery/3.4.1/jquery.js
 *
 *    任意/**匹配的静态资源默认从以下4个路径中查找
 *    "classpath:/META-INF/resources/","classpath:/resources/", "classpath:/static/", "classpath:/public/"
 *    例如当前应用中访问静态Chart.min.js文件的URL为http://localhost:8080/asserts/js/Chart.min.js
 * </pre>
 *
 * SpringBoot 欢迎页
 *
 * @see org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration#welcomePageHandlerMapping(org.springframework.context.ApplicationContext, org.springframework.format.support.FormattingConversionService, org.springframework.web.servlet.resource.ResourceUrlProvider)
 * @see org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration#getIndexHtml(java.lang.String)
 * <pre>
 *     return this.resourceLoader.getResource(location + "index.html");
 * </pre>
 * 从静态资源路径下查找index.html
 *
 * SpringBoot 使用Thymeleaf模板引擎
 *
 * 1.引入 compile group: 'org.springframework.boot', name: 'spring-boot-starter-thymeleaf'
 * 2.Thymeleaf自动配置
 * @see org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
 * @see org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties
 * <pre>
 *     public static final String DEFAULT_PREFIX = "classpath:/templates/";
 * 	   public static final String DEFAULT_SUFFIX = ".html";
 * </pre>
 * 将Thymeleaf模板默认放置到classpath:/templates/下 文件默认类型为.html
 *
 * 3.Thymeleaf语法
 * html文件导入thymeleaf的名称空间 用于语法提示
 * <html lang="en" xmlns:th="http://www.thymeleaf.org">
 *
 * 1)、th:text；改变当前元素里面的文本内容； th：任意html属性(th:id,th:class)；来替换原生属性的值
 * 例如<div th:text="${hello}">这是显示欢迎信息</div>
 * @see /explorations/application-framework/spring-boot/src/main/resources/static/images/thymeleaf-th.png
 *
 * 2)、表达式
 * ${...}：获取变量值
 *     1）、获取对象的属性、调用方法
 *     2）、使用内置的基本对象
 *         #ctx : the context object.
 *         #vars: the context variables.
 *         #locale : the context locale.
 *         #request : (only in Web Contexts) the HttpServletRequest object.
 *         #response : (only in Web Contexts) the HttpServletResponse object.
 *         #session : (only in Web Contexts) the HttpSession object.
 *         #servletContext : (only in Web Contexts) the ServletContext object.
 *     3）、内置的一些工具对象
 *         #numbers : methods for formatting numeric objects.
 *         #strings : methods for String objects: contains, startsWith, prepending/appending, etc.
 *         #objects : methods for objects in general.
 *         #bools : methods for boolean evaluation.
 *         #arrays : methods for arrays.
 *         #lists : methods for lists.
 *         #sets : methods for sets.
 *         #maps : methods for maps.
 *         ......
 *
 * *{...}：选择表达式：和${}在功能上是一样
 * 配合 th:object="${}使用 例如
 * <div th:object="${session.user}">
 *     <p>Name: <span th:text="*{firstName}">Sebastian</span>.</p>
 *     <p>Surname: <span th:text="*{lastName}">Pepper</span>.</p>
 * </div>
 *
 * #{...}：获取国际化内容
 *
 * @{...}：定义URL；
 * @{/order/process(execId=${execId},execType='FAST')}
 *
 * ~{...}：片段引用表达式
 * <div th:insert="~{commons :: main}">...</div>
 *
 * 3)、Literals（字面量）
 * Text literals: 'one text' ,
 * 'Another one!' ,…
 * Number literals: 0 , 34 , 3.0 , 12.3 ,…
 * Boolean literals: true , false
 * Null literal: null
 * Literal tokens: one , sometext , main ,…
 *
 * 4)、Text operations:（文本操作）
 * String concatenation: +
 * Literal substitutions: |The name is ${name}|
 *
 * 5)、Arithmetic operations:（数学运算）
 * Binary operators: + , ‐ , * , / , %
 *
 * SpringMVC自动装配
 * @see org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
 * @see org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties
 *
 * 自动配置了ViewResolver
 *     ContentNegotiatingViewResolver：组合所有的视图解析器(内容协商)；
 *     @see org.springframework.web.servlet.view.ContentNegotiatingViewResolver#resolveViewName(java.lang.String, java.util.Locale)
 *     <pre>
 *         List<View> candidateViews = getCandidateViews(viewName, locale, requestedMediaTypes);
 * 		   View bestView = getBestView(candidateViews, requestedMediaTypes, attrs);
 *     </pre>
 *     因此要自定义视图解析器 只需要向容器中注册一个自定义视图解析器
 *
 *     BeanNameViewResolver
 *
 * 自动配置静态资源文件夹路径,webjars
 *
 * 自动配置静态首页访问
 *
 * 自动注册了Converter , GenericConverter , Formatter
 * @see org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#addFormatters(org.springframework.format.FormatterRegistry)
 *
 * 自动注册了HttpMessageConverters
 * @see org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#configureMessageConverters(java.util.List)
 *
 * @see org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration#getConfigurableWebBindingInitializer(org.springframework.format.support.FormattingConversionService, org.springframework.validation.Validator)
 * @see org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration#createExceptionHandlerExceptionResolver()
 * ......
 *
 * 自定义SpringMVC配置 (补充扩展SpringBoot自动配置) 例如
 * <pre>
 *     @Configuration
 *     public class CustomizedSpringMvcConfiguration implements WebMvcConfigurer {
 *
 *         public void addViewControllers(ViewControllerRegistry registry) {
 *             registry.addViewController("/index").setViewName("index");
 *         }
 *     }
 * </pre>
 * 既有SpringBoot的自动配置 又有自定义配置
 * @see org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration#setConfigurers(java.util.List)
 * <pre>
 *     @Autowired(required = false)
 * 	   public void setConfigurers(List<WebMvcConfigurer> configurers) {
 * 		   if (!CollectionUtils.isEmpty(configurers)) {
 * 			   this.configurers.addWebMvcConfigurers(configurers);
 *         }
 *     }
 * </pre>
 *
 * 完全自定义SpringMVC配置
 *
 * 在自定义配置类上标记@EnableWebMvc即可
 *
 * 原理
 * <pre>
 *     @Import(DelegatingWebMvcConfiguration.class)
 *     public @interface EnableWebMvc {
 *     }
 * </pre>
 * 注解@EnableWebMvc导入了DelegatingWebMvcConfiguration 他是WebMvcConfigurationSupport的子类
 * 而自动配置类上标注有条件 @ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
 * 即没有WebMvcConfigurationSupport类才生效
 *
 * 国际化
 *
 * 1)编写国际化配置文件，抽取页面需要显示的国际化消息
 * 2)SpringBoot自动配置好了管理国际化资源文件的组件
 * @see org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration
 * @see org.springframework.boot.autoconfigure.context.MessageSourceProperties
 * 默认的资源文件为类路径下的message,若有通过spring.messages.basename指定文件
 * 则加载指定文件，默认或指定文件都没有加载成功,返回条件结果不匹配。
 *
 * 3)页面获取国际化的值
 *
 * 国际化原理简析
 * Spring自动配置了localeResolver
 * @see org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#localeResolver()
 * <pre>
 *     if (this.mvcProperties
 *					.getLocaleResolver() == WebMvcProperties.LocaleResolver.FIXED) {
 * 			return new FixedLocaleResolver(this.mvcProperties.getLocale());
 *	   }
 *	   AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
 *	   localeResolver.setDefaultLocale(this.mvcProperties.getLocale());
 *	   return localeResolver;
 * </pre>
 * 即默认使用AcceptHeaderLocaleResolver
 * @see  AcceptHeaderLocaleResolver#resolveLocale(HttpServletRequest)
 * 从请求头中 [Accept-Language: zh-CN,zh;q=0.9,en;q=0.8] 取值
 *
 * SpringBoot默认的错误处理机制
 *
 * 1) 浏览器返回错误夜
 * 2) 其他客户端返回json数据
 *
 * @see  org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
 * 给容器中添加了以下组件
 * ErrorPageCustomizer(可以自定义错页面) @see {@link org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration.ErrorPageCustomizer}
 * BasicErrorController
 * DefaultErrorAttributes
 * DefaultErrorViewResolver
 *
 * 404 异常处理流程分析
 * SpringBoot在创建WEB容器的时候会将错误处理页配置到容器中
 * @see org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory#configureContext(org.apache.catalina.Context, org.springframework.boot.web.servlet.ServletContextInitializer[])
 * <pre>
 *     for (ErrorPage errorPage : getErrorPages()) {
 * 			org.apache.tomcat.util.descriptor.web.ErrorPage tomcatErrorPage = new org.apache.tomcat.util.descriptor.web.ErrorPage();
 * 			tomcatErrorPage.setLocation(errorPage.getPath());
 * 			tomcatErrorPage.setErrorCode(errorPage.getStatusCode());
 * 			tomcatErrorPage.setExceptionType(errorPage.getExceptionName());
 * 			context.addErrorPage(tomcatErrorPage);
 *    }
 * </pre>
 * 无映射的请求使用ResourceHttpRequestHandler 作为静态资源处理
 * @see org.springframework.web.servlet.resource.ResourceHttpRequestHandler#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 * 静态资源也不存在返回404
 * <pre>
 *     Resource resource = getResource(request);
 * 	   if (resource == null) {
 * 			logger.debug("Resource not found");
 * 		    //	将response的错误状态设置为1 状态码设为404
 * 			response.sendError(HttpServletResponse.SC_NOT_FOUND);
 * 			return;
 *     }
 * </pre>
 * web容器(这里是tomcat) 处理响应时 调用对应方法
 * @see org.apache.catalina.core.StandardHostValve#status(org.apache.catalina.connector.Request, org.apache.catalina.connector.Response)
 * 查找到错误页面后做相应的属性设置
 * <pre>
 *      request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE,Integer.valueOf(statusCode));
 *      String message = response.getMessage();
 *      if (message == null) {message = "";}
 *      request.setAttribute(RequestDispatcher.ERROR_MESSAGE, message);
 *      request.setAttribute(Globals.DISPATCHER_REQUEST_PATH_ATTR,errorPage.getLocation());
 *      request.setAttribute(Globals.DISPATCHER_TYPE_ATTR,DispatcherType.ERROR);
 *      ......
 * </pre>
 * 然后转发请求到错误处理
 * @see org.apache.catalina.core.StandardHostValve#custom(org.apache.catalina.connector.Request, org.apache.catalina.connector.Response, org.apache.tomcat.util.descriptor.web.ErrorPage)
 * @see org.apache.catalina.core.ApplicationFilterChain#internalDoFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
 * <pre>
 *     servlet.service(request, response);
 * </pre>
 * SpringBoot自动配置的错误处理控制器为BasicErrorController
 * @see org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
 * <pre>
 *     @RequestMapping("${server.error.path:${error.path:/error}}")
 *     public class BasicErrorController extends AbstractErrorController {
 *         ......
 *         @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)//   匹配浏览器请求(text/html)
 *         public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
 *             ......
 *         }
 *         @RequestMapping // 其他客户端访问返回json格式的错误信息
 *         public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
 *             ......
 *         }
 *         ......
 *     }
 * </pre>
 * BasicErrorController#errorHtml方法对错误页面的处理逻辑为
 * <pre>
 *      // 获取错误状态码
 *      HttpStatus status = getStatus(request);
 *      // 获取全部的错误属性信息,从自动配置的DefaultErrorAttributes中获取(默认的属性有 timestamp、 error、 status、exception、message、path)
 *      // @see {@link org.springframework.boot.web.servlet.error.DefaultErrorAttributes#getErrorAttributes(org.springframework.web.context.request.WebRequest, boolean)}
 * 		Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
 * 		response.setStatus(status.value());
 * 	    // 解析错误视图 即使用自动配置的DefaultErrorViewResolver
 * 	    // @see {@link org.springframework.boot.autoconfigure.web.servlet.error.DefaultErrorViewResolver#resolveErrorView(HttpServletRequest, HttpStatus, Map)}
 * 	    // @see {@link org.springframework.boot.autoconfigure.web.servlet.error.DefaultErrorViewResolver#resolve(java.lang.String, java.util.Map)}
 * 	    // 即默认使用error/statusCode作为视图名
 * 	    <pre>
 * 	        TemplateAvailabilityProvider provider = this.templateAvailabilityProviders.getProvider(errorViewName,this.applicationContext);
 * 		    if (provider != null) {
 * 		        return new ModelAndView(errorViewName, model);
 *          }
 * 		    return resolveResource(errorViewName, model);
 * 	    </pre>
 * 	    // 若模板引擎可以处理错误视图返回ModelAndView由模板引擎解析 不可用继续解析
 * 	    // 继续从静态资源路径下查找静态的statusCode.html文件(404.html)
 * 	    // @see {@link org.springframework.boot.autoconfigure.web.servlet.error.DefaultErrorViewResolver#resolveResource(java.lang.String, java.util.Map)}
 * 	    // 按状态码精确匹配没有查找到 则使用error/4xx查找对应视图
 * 		ModelAndView modelAndView = resolveErrorView(request, response, status, model);
 * 		return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);
 * </pre>
 * 如果以上过程都没有查找到错误视图 返回 new ModelAndView("error",model)
 * 即使用默认错误视图
 * @see org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration.StaticView
 *
 * 500 异常处理流程分析
 *
 * 处理器发生500异常 进入异常处理
 * @see org.springframework.web.servlet.DispatcherServlet#processHandlerException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
 * 使用HandlerExceptionResolverComposite解析异常 默认有3个ExceptionResolver
 * 0 = {ExceptionHandlerExceptionResolver@7155}
 * 1 = {ResponseStatusExceptionResolver@7156}
 * 2 = {DefaultHandlerExceptionResolver@7157}
 * @see org.springframework.web.servlet.handler.HandlerExceptionResolverComposite#resolveException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
 * 这三个都不能处理500异常 异常向上抛出给WEB容器 web容器会在请求对象上设置异常属性
 * @see org.apache.catalina.core.StandardWrapperValve#exception(org.apache.catalina.connector.Request, org.apache.catalina.connector.Response, java.lang.Throwable)
 * 即设置响应状态码和将错误状态标记为1(错误)等
 * 然后WEB容器处理开始这个异常
 * @see org.apache.catalina.core.StandardHostValve#throwable(org.apache.catalina.connector.Request, org.apache.catalina.connector.Response, java.lang.Throwable)
 * 和处理404一样转发请求到/error
 *
 *
 * 自定义异常处理器和扩展异常属性
 * @see ErrorMvcAutoConfiguration
 * <pre>
 *      @Bean
 *      // 容器中没有ErrorAttributes是才注册
 *      @ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
 * 	    public DefaultErrorAttributes errorAttributes() {
 * 		    return new DefaultErrorAttributes(this.serverProperties.getError().isIncludeException());
 *      }
 * </pre>
 *
 * @see BasicErrorController#errorHtml(HttpServletRequest, HttpServletResponse)
 * 操作的数据都取自ErrorAttributes,默认为DefaultErrorAttributes类的实例
 * <pre>
 *     Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
 * </pre>
 * @see org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController#getErrorAttributes(javax.servlet.http.HttpServletRequest, boolean)
 * 因此重写DefaultErrorAttributes并注册到容器(自动装配的条件将不满足)可以实现自定义的错误信息
 *
 * SpringBoot 嵌入式WEB容器(默认使用Tomcat)
 *
 * 嵌入式WEB容器自动配置
 * @see org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration
 * @see org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryConfiguration
 * @see org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
 * <pre>
 *      @Import({ ServletWebServerFactoryAutoConfiguration.BeanPostProcessorsRegistrar.class,
 * 		ServletWebServerFactoryConfiguration.EmbeddedTomcat.class,
 * 		ServletWebServerFactoryConfiguration.EmbeddedJetty.class,
 * 		ServletWebServerFactoryConfiguration.EmbeddedUndertow.class })
 *      public class ServletWebServerFactoryAutoConfiguration {
 *          .....
 *      }
 * </pre>
 * ServletWebServerFactoryAutoConfiguration.BeanPostProcessorsRegistrar 向容器中注册了 @see {@link org.springframework.boot.web.server.WebServerFactoryCustomizerBeanPostProcessor}
 * 负责调用WebServerFactory的定制器方法
 * @see org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration.BeanPostProcessorsRegistrar#registerBeanDefinitions(org.springframework.core.type.AnnotationMetadata, org.springframework.beans.factory.support.BeanDefinitionRegistry)
 * @see org.springframework.boot.web.server.WebServerFactoryCustomizerBeanPostProcessor#postProcessBeforeInitialization(org.springframework.boot.web.server.WebServerFactory)
 * 因此配置WEB容器除了在配置文件中设置对应的properties 还可以实现WebServerFactoryCustomizer接口
 * 并将自定义的定制器注册到容器中
 *
 *
 * 配置WEB容器
 * @see org.springframework.boot.autoconfigure.web.ServerProperties
 *
 * Servlet Filter Listener 组件注册到WEB容器
 * @see org.springframework.boot.web.servlet.ServletRegistrationBean
 * @see org.springframework.boot.web.servlet.FilterRegistrationBean
 * @see org.springframework.boot.web.servlet.ServletListenerRegistrationBean
 * // 参考SpringMVC的DispatcherServlet的注册
 * @see org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration.DispatcherServletRegistrationConfiguration#dispatcherServletRegistration(org.springframework.web.servlet.DispatcherServlet, org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties, org.springframework.beans.factory.ObjectProvider)
 *
 * SpringBoot 驱动嵌入式容器启动
 * Sping上下文容器启动时
 * @see org.springframework.context.support.AbstractApplicationContext#refresh()
 * 在完成配置解析和Bean注册以及Bean后置处理器注册等后 会调用特定上下文的onRefresh()方法
 * <pre>
 *     // Initialize other special beans in specific context subclasses.
 * 	   onRefresh();
 * </pre>
 * SpringBoot的Spring上下文容器是ServletWebServerApplicationContext 它实现了onRefresh
 * @see org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext#onRefresh()
 * 在这里开始创建WEB容器 并配置后启动
 * @see org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext#createWebServer()
 * 1) 获取WEB容器工厂
 * @see org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext#getWebServerFactory()
 * <pre>
 *     // 从Spring BeanFactory容器中获取注册的ServletWebServerFactory
 *     // 在Spring容器启动时自动装配了ServletWebServerFactory (默认为EmbeddedTomcat)
 *     @see org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryConfiguration.EmbeddedTomcat#tomcatServletWebServerFactory(ObjectProvider, ObjectProvider, ObjectProvider)
 *     // tomcatServletWebServerFactory 创建时为其注入了定制配置
 *     <pre>
 *         factory.getTomcatConnectorCustomizers()
 * 					.addAll(connectorCustomizers.orderedStream().collect(Collectors.toList()));
 * 			factory.getTomcatContextCustomizers()
 * 					.addAll(contextCustomizers.orderedStream().collect(Collectors.toList()));
 * 			factory.getTomcatProtocolHandlerCustomizers()
 * 					.addAll(protocolHandlerCustomizers.orderedStream().collect(Collectors.toList()));
 * 			return factory;
 *     </pre>
 *     String[] beanNames = getBeanFactory().getBeanNamesForType(ServletWebServerFactory.class);
 * </pre>
 * 2) 从WEB工厂创建WEB容器
 * @see org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory#getWebServer(org.springframework.boot.web.servlet.ServletContextInitializer...)
 * <pre>
 *     Tomcat tomcat = new Tomcat();
 *     File baseDir = (this.baseDirectory != null) ? this.baseDirectory : createTempDir("tomcat");
 * 	   tomcat.setBaseDir(baseDir.getAbsolutePath());
 * 	   Connector connector = new Connector(this.protocol);
 * 	   connector.setThrowOnFailure(true);
 * 	   tomcat.getService().addConnector(connector);
 * 	   customizeConnector(connector);
 * 	   tomcat.setConnector(connector);
 * 	   tomcat.getHost().setAutoDeploy(false);
 * 	   configureEngine(tomcat.getEngine());
 *
 * 	   prepareContext(tomcat.getHost(), initializers);
 * </pre>
 * @see org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory#prepareContext(org.apache.catalina.Host, org.springframework.boot.web.servlet.ServletContextInitializer[])
 * @see org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory#configureContext(org.apache.catalina.Context, org.springframework.boot.web.servlet.ServletContextInitializer[])
 *
 * 创建TomcatWebServer
 * @see org.springframework.boot.web.embedded.tomcat.TomcatWebServer#TomcatWebServer(org.apache.catalina.startup.Tomcat, boolean)
 * <pre>
 *      this.tomcat = tomcat;
 * 		this.autoStart = autoStart;
 * 		initialize();
 * </pre>
 * @see org.springframework.boot.web.embedded.tomcat.TomcatWebServer#initialize()
 * <pre>
 *     this.tomcat.start();
 * </pre>
 * 容器启动 启动时会进行初始化
 * @see org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext#selfInitialize(javax.servlet.ServletContext)
 * <pre>
 *      prepareWebApplicationContext(servletContext);
 * 		registerApplicationScope(servletContext);
 * 		WebApplicationContextUtils.registerEnvironmentBeans(getBeanFactory(), servletContext);
 * 	    // 这里进行Servlet组件(Servlet Filter Listener)的注册等
 * 	    // @see {@link org.springframework.boot.web.servlet.RegistrationBean#onStartup(javax.servlet.ServletContext)}
 * 		for (ServletContextInitializer beans : getServletContextInitializerBeans()) {
 * 			beans.onStartup(servletContext);
 *      }
 * </pre>
 *
 *
 * @auther mac
 * @date 2020-02-02 12:21
 */
@SpringBootApplication
public class SpringBootWebSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebSampleApplication.class,args);
    }
}
