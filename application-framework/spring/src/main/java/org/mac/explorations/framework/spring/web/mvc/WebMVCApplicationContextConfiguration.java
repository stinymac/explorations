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

package org.mac.explorations.framework.spring.web.mvc;

import org.hibernate.validator.HibernateValidator;
import org.mac.explorations.framework.spring.web.mvc.async.SimpleAsyncHandlerInterceptor;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.util.List;

/**
 * SpringMVC配置
 *
 * 1.开启@EnableWebMvc
 *   <pre>
 *       @Import(DelegatingWebMvcConfiguration.class)
 *       public @interface EnableWebMvc {
 *       }
 *   </pre>
 *   @see org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration
 *
 *   即注册了SpringMVC的默认配置 并自动注入已注册的自定义配置
 *   @see org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration#setConfigurers(java.util.List)
 *   <pr>
 *       @Autowired(required = false)
 *       public void setConfigurers(List<WebMvcConfigurer> configurers) {
 *           if (!CollectionUtils.isEmpty(configurers)) {
 *               this.configurers.addWebMvcConfigurers(configurers);
 *           }
 *       }
 *   </pr>
 *
 * 2.实现@see {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurer}接口
 *   自定义相关配置
 *
 * =====================================================================================================================
 * SpringMVC 初始化启动过程分析
 *
 * 1.Servlet容器启动调用DispatcherServlet的init方法
 * @see org.springframework.web.servlet.HttpServletBean#init()
 * @see org.springframework.web.servlet.FrameworkServlet#initServletBean()
 * <pre>
 *     this.webApplicationContext = initWebApplicationContext();
 * 	   initFrameworkServlet();
 * </pre>
 * 首先初始化SpringWEB容器上下文
 * @see org.springframework.web.servlet.FrameworkServlet#initWebApplicationContext()
 * <pre>
 *     为SpringWEB容器上下文设置父容器(即Spring Root Context)
 *     配置和刷新SpringWEB容器上下文
 *     @see org.springframework.web.servlet.FrameworkServlet#configureAndRefreshWebApplicationContext(org.springframework.web.context.ConfigurableWebApplicationContext)
 *     <pre>
 *         wac.setServletContext(getServletContext());
 * 		   wac.setServletConfig(getServletConfig());
 * 		   wac.setNamespace(getNamespace());
 * 		   wac.addApplicationListener(new SourceFilteringListener(wac, new ContextRefreshListener()));
 * 		   ConfigurableEnvironment env = wac.getEnvironment();
 * 		   if (env instanceof ConfigurableWebEnvironment) {
 * 			   ((ConfigurableWebEnvironment) env).initPropertySources(getServletContext(), getServletConfig());
 *         }
 *         // 这里没有实现 可以通过继承由子类实现
 *         postProcessWebApplicationContext(wac);
 *         // 可以通过实现ApplicationContextInitializer定义全局的上下文初始逻辑
 *         @see org.springframework.context.ApplicationContextInitializer
 *         @see org.springframework.web.servlet.FrameworkServlet#applyInitializers(org.springframework.context.ConfigurableApplicationContext)
 * 		   applyInitializers(wac);
 * 		   * Spring上下文容器(这里是WEB容器)的初始逻辑
 * 		   wac.refresh();
 *     </pre>
 *     SpringWEB上下文作为一个Attribute设置到Servlet上下文
 *     String attrName = getServletContextAttributeName();
 * 	   getServletContext().setAttribute(attrName, wac);
 * </pre>
 * 初始化Servlet
 * @see org.springframework.web.servlet.FrameworkServlet#initFrameworkServlet()
 * FrameworkServlet没有其本身的初始逻辑
 *
 * 以上过程中和SpringMVC初始化相关的是 配置文件解析Bean定义后注册Bean定义到BeanFactory
 * 配置文件上标注的@EnableWebMvc注解开启SpringMVC
 * 它向容器中导入了@see {@link org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration}
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerComposite
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
 * 其中WebMvcConfigurationSupport中注册了SpringMVC默认的组件
 * <pre>
 *        mvcContentNegotiationManager->:@see {@link org.springframework.web.accept.ContentNegotiationManager}
 *                // 默认的转换器添加       @see {@link org.springframework.core.convert.support.DefaultConversionService#addDefaultConverters(org.springframework.core.convert.converter.ConverterRegistry)}
 *                mvcConversionService->:@see {@link org.springframework.format.support.DefaultFormattingConversionService}
 *              mvcResourceUrlProvider->:@see {@link org.springframework.web.servlet.resource.ResourceUrlProvider}
 *        requestMappingHandlerMapping->:@see {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping}
 *                      mvcPathMatcher->:@see {@link org.springframework.util.AntPathMatcher}
 *                    mvcUrlPathHelper->:@see {@link org.springframework.web.util.UrlPathHelper}
 *        viewControllerHandlerMapping->:@see {@link org.springframework.web.servlet.handler.SimpleUrlHandlerMapping}
 *              beanNameHandlerMapping->:@see {@link org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping}
 *               routerFunctionMapping->:@see {@link org.springframework.web.servlet.function.support.RouterFunctionMapping}
 *              resourceHandlerMapping->:@see {@link null}
 *        defaultServletHandlerMapping->:@see {@link org.springframework.web.servlet.handler.SimpleUrlHandlerMapping}
 *                        mvcValidator->:@see {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport.NoOpValidator}
 *        requestMappingHandlerAdapter->:@see {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter}
 *              handlerFunctionAdapter->:@see {@link org.springframework.web.servlet.function.support.HandlerFunctionAdapter}
 *         mvcUriComponentsContributor->:@see {@link org.springframework.web.method.support.CompositeUriComponentsContributor}
 *           httpRequestHandlerAdapter->:@see {@link org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter}
 *      simpleControllerHandlerAdapter->:@see {@link org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter}
 *            handlerExceptionResolver->:@see {@link org.springframework.web.servlet.handler.HandlerExceptionResolverComposite}
 *                     mvcViewResolver->:@see {@link org.springframework.web.servlet.view.ViewResolverComposite}
 * </pre>
 * SpringMVC 容器初始化后发布ContextRefreshedEvent事件 DispatcherServlet通过监听这个事件执行初始化
 * @see org.springframework.web.servlet.DispatcherServlet#onRefresh(org.springframework.context.ApplicationContext)
 * @see org.springframework.web.servlet.DispatcherServlet#initStrategies(org.springframework.context.ApplicationContext)
 * <pre>
 *      ------------------------------------------静态加载的默认策略----------------------------------------------------------------------
 *      来自文件 /spring-webmvc-5.2.2.RELEASE.jar!/org/springframework/web/servlet/DispatcherServlet.properties
 *      @see org.springframework.web.servlet.DispatcherServlet#defaultStrategies
 *      "org.springframework.web.servlet.LocaleResolver" -> "org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver"
 *      "org.springframework.web.servlet.ThemeResolver" -> "org.springframework.web.servlet.theme.FixedThemeResolver"
 *      "org.springframework.web.servlet.HandlerMapping" -> "org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping,org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping,org.springframework.web.servlet.function.support.RouterFunctionMapping"
 *      "org.springframework.web.servlet.RequestToViewNameTranslator" -> "org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator"
 *      "org.springframework.web.servlet.HandlerAdapter" -> "org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter,org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter,org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter,org.springframework.web.servlet.function.support.HandlerFunctionAdapter"
 *      "org.springframework.web.servlet.ViewResolver" -> "org.springframework.web.servlet.view.InternalResourceViewResolver"
 *      "org.springframework.web.servlet.FlashMapManager" -> "org.springframework.web.servlet.support.SessionFlashMapManager"
 *      "org.springframework.web.servlet.HandlerExceptionResolver" -> "org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver,org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver,org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver"
 *      ----------------------------------------------------------------------------------------------------------------
 *      initMultipartResolver(context);
 * 		initLocaleResolver(context);
 * 		initThemeResolver(context);
 * 	     //	这些HandlerMapping是有序的(初始化时从Context中取出后会对其排序)
 * 	     // 4 = {SimpleUrlHandlerMapping}
 * 	     // 3 = {RouterFunctionMapping}
 * 	     // 2 = {BeanNameUrlHandlerMapping}
 * 	     // 1 = {SimpleUrlHandlerMapping}
 * 	     // 0 = {RequestMappingHandlerMapping}
 * 		initHandlerMappings(context);
 * 	    // 这些HandlerAdapter是有序的(初始化时从Context中取出后会对其排序)
 * 	    // 0 = {RequestMappingHandlerAdapter}
 * 	    // 1 = {HandlerFunctionAdapter}
 * 	    // 2 = {HttpRequestHandlerAdapter}
 * 	    // 3 = {SimpleControllerHandlerAdapter}
 * 		initHandlerAdapters(context);
 * 	    // 这些HandlerExceptionResolver是有序的(初始化时从Context中取出后会对其排序)
 * 	    // HandlerExceptionResolverComposite
 * 		initHandlerExceptionResolvers(context);
 * 	    //	这里使用默认策略(默认策略是通过静态初始化从文件中加载的)
 * 	    //  @see {@link org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator}
 * 		initRequestToViewNameTranslator(context);
 * 	    // ViewResolver是有序的
 * 	    // ViewResolverComposite
 * 		initViewResolvers(context);
 * 		initFlashMapManager(context);
 * </pre>
 *
 * SpringMVC一个请求流程分析
 * 对于SpringMVC来说请求首先进入
 * @see org.springframework.web.servlet.FrameworkServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 * <pre>
 *      HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
 * 		if (httpMethod == HttpMethod.PATCH || httpMethod == null) {
 * 	        processRequest(request, response);
 *      }
 * 		else {
 * 			super.service(request, response);
 *        }
 * </pre>
 * 然后根据请求方法类型进入相应的处理方法 但最终由方法FrameworkServlet#processRequest来处理
 * @see org.springframework.web.servlet.FrameworkServlet#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 * <pre>
 *      LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
 * 	    //@see {@link org.springframework.context.i18n.SimpleLocaleContext}
 * 		LocaleContext localeContext = buildLocaleContext(request);
 *
 *      //@see {@link org.springframework.web.context.request.RequestContextHolder}
 * 		RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
 * 		ServletRequestAttributes requestAttributes = buildRequestAttributes(request, response, previousAttributes);
 *
 *      //@see {@link org.springframework.web.context.request.async.WebAsyncManager}
 *      //@see {@link org.springframework.web.context.request.async.WebAsyncUtils#getAsyncManager(javax.servlet.ServletRequest)}
 * 		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
 * 	    //@see {@link org.springframework.web.servlet.FrameworkServlet.RequestBindingInterceptor}
 * 		asyncManager.registerCallableInterceptor(FrameworkServlet.class.getName(), new RequestBindingInterceptor());
 *
 *      // 初始化请求上下文(RequestContextHolder)和本地上下文(LocaleContextHolder)
 * 		initContextHolders(request, localeContext, requestAttributes);
 *
 * 		......
 * 	    // 这里进入DispatchServlet
 * 		doService(request, response);
 * 	    ......
 * 	    finally {
 * 	        // *对上下文重置清理
 * 			resetContextHolders(request, previousLocaleContext, previousAttributes);
 *
 * 		    if (requestAttributes != null) {
 * 		        //@see {@link org.springframework.web.context.request.AbstractRequestAttributes#requestCompleted()}
 * 				requestAttributes.requestCompleted();
 *          }
 * 		    logResult(request, response, failureCause, asyncManager);
 * 		    // 发布请求处理完成事件 通过SpringMVC上下文容器发布时间
 * 		    // @see {@link org.springframework.web.servlet.FrameworkServlet#publishRequestHandledEvent(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, long, java.lang.Throwable)}
 * 			publishRequestHandledEvent(request, response, startTime, failureCause);
 * 		}
 * </pre>
 * 通过上面的FrameworkServlet#processRequest调用DispatcherServlet#doService进入SpingMVC的主要处理流程中
 * @see org.springframework.web.servlet.DispatcherServlet#doService(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 * <pre>
 *      ......
 *      request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebApplicationContext());
 * 		request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver);
 * 		request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver);
 * 		request.setAttribute(THEME_SOURCE_ATTRIBUTE, getThemeSource());
 * 	    ......
 * 	    doDispatch(request, response);
 * 	    ......
 * </pre>
 * 进行请求分派
 * @see org.springframework.web.servlet.DispatcherServlet#doDispatch(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 * <pre>
 *     ......
 *     WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
 *     try {
 * 			ModelAndView mv = null;
 * 			Exception dispatchException = null;
 *          ......
 * 			try {
 * 				// Determine handler for the current request.
 * 				mappedHandler = getHandler(processedRequest);
 * 				if (mappedHandler == null) {
 * 					noHandlerFound(processedRequest, response);
 * 					return;
 *              }
 *
 * 				// Determine handler adapter for the current request.
 * 				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
 *
                ......
 *
 * 				if (!mappedHandler.applyPreHandle(processedRequest, response)) {
 * 					return;
 *              }
 *
 * 				// Actually invoke the handler.
 * 				mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
 *
 * 				if (asyncManager.isConcurrentHandlingStarted()) {
 * 					return;
 *              }
 *
 * 				applyDefaultViewName(processedRequest, mv);
 * 				mappedHandler.applyPostHandle(processedRequest, response, mv);            * 			}
 * 			catch (Exception ex) {
 * 				dispatchException = ex;
 * 			}
 * 			catch (Throwable err) {
 * 				// As of 4.3, we're processing Errors thrown from handler methods as well,
 * 				// making them available for @ExceptionHandler methods and other scenarios.
 * 				dispatchException = new NestedServletException("Handler dispatch failed", err);
 * 			}
 * 			processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchExcepti        ;
 * 		}
 * 		catch (Exception ex) {
 * 			triggerAfterCompletion(processedRequest, response, mappedHandler,         ;
 * 		}
 * 		catch (Throwable err) {
 * 			triggerAfterCompletion(processedRequest, response, mappedHandler,new NestedServletException("Handler processing failed", err));
 * 		}
 * 		finally {
 * 			if (asyncManager.isConcurrentHandlingStarted()) {
 * 				// Instead of postHandle and afterCompletion
 * 				if (mappedHandler != null) {
 * 					mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
 * 				}
 * 			}
 * 		    ......
 * 		}
 * </pre>
 * 1).获取请求的处理器
 * @see org.springframework.web.servlet.DispatcherServlet#getHandler(javax.servlet.http.HttpServletRequest)
 * 即是依次从已注册的HandlerMappings中查找请求的处理器 找到即返回
 * @see org.springframework.web.servlet.handler.AbstractHandlerMapping#getHandler(javax.servlet.http.HttpServletRequest)
 * 其返回的是一个处理器链对象 其中包含了处理器和处理器拦截器集合
 * @see org.springframework.web.servlet.HandlerExecutionChain
 * 一般的处理查找方法位于父类AbstractHandlerMethodMapping中
 * @see org.springframework.web.servlet.handler.AbstractHandlerMethodMapping#getHandlerInternal(javax.servlet.http.HttpServletRequest)
 * <pre>
 *     // mappingRegistry 中存放的是全部解析出的映射(解析自@RequestMapping或其子注解)
 *     this.mappingRegistry.acquireReadLock();
 * 	   try {
 * 			HandlerMethod handlerMethod = lookupHandlerMethod(lookupPath, request);
 * 			return (handlerMethod != null ? handlerMethod.createWithResolvedBean() : null);
 *     }
 * 	   finally {
 * 			this.mappingRegistry.releaseReadLock();
 *     }
 * </pre>
 * 查找处理方法
 * @see org.springframework.web.servlet.handler.AbstractHandlerMethodMapping#lookupHandlerMethod(java.lang.String, javax.servlet.http.HttpServletRequest)
 * @see org.springframework.web.servlet.handler.AbstractUrlHandlerMapping#lookupHandler(java.lang.String, javax.servlet.http.HttpServletRequest)
 * @see org.springframework.web.servlet.function.support.RouterFunctionMapping#getHandlerInternal(javax.servlet.http.HttpServletRequest)
 * 查找到映射的处理器后 处理器会被包装为HandlerExecutionChain并从拦截器中查找匹配的拦截器注册到HandlerExecutionChain中
 *
 * 2).查找获得处理器后，获取处理器的适配器
 * <pre>
 *     for (HandlerAdapter adapter : this.handlerAdapters) {
 * 			if (adapter.supports(handler)) {
 * 		    	return adapter;
 *          }
 *     }
 * </pre>
 * 3).执行拦截器前置拦截处理方法(从前往后执行interceptors)
 * <pre>
 *     for (int i = 0; i < interceptors.length; i++) {
 *         HandlerInterceptor interceptor = interceptors[i];
 * 		   if (!interceptor.preHandle(request, response, this.handler)) {
 * 					triggerAfterCompletion(request, response, null);
 * 					return false;
 *         }
 * 	       his.interceptorIndex = i;
 * 	   }
 * </pre>
 * 4).执行处理器方法
 * 即调用处理器适配器的handle方法 例如
 * @see org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter#handleInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.method.HandlerMethod)
 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter#invokeHandlerMethod(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.method.HandlerMethod)
 * <pre>
 *     1.取数据绑定工厂
 *     @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter#getDataBinderFactory(org.springframework.web.method.HandlerMethod)
 *     <pre>
 *         // 取被@InitBinder注解的方法
 *         Set<Method> methods = this.initBinderCache.get(handlerType);
 * 		   if (methods == null) {
 * 			   methods = MethodIntrospector.selectMethods(handlerType, INIT_BINDER_METHODS);
 * 			   this.initBinderCache.put(handlerType, methods);
 *         }
 *         //从initBinderAdviceCache查找数据绑定方法
 *         .....
 *         传入数据绑定方法和webBindingInitializer到数据绑定工厂构造器中创建数据绑定工厂
 *         new ServletRequestDataBinderFactory(binderMethods, getWebBindingInitializer());
 *         @see org.springframework.web.bind.support.ConfigurableWebBindingInitializer
 *         @see org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory
 *
 *     </pre>
 *     2.获取模型工厂
 *     @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter#getModelFactory(org.springframework.web.method.HandlerMethod, org.springframework.web.bind.support.WebDataBinderFactory)
 *     <pre>
 *         查找@ModelAttribute注解方法
 *         创建模型工厂
 *         @see org.springframework.web.method.annotation.ModelFactory
 *     </pre>
 *     3.将处理器方法对象包装为可调用的HandlerMethod
 *     @see org.springframework.web.method.support.InvocableHandlerMethod
 *     @see org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod
 *     4.设置可调用的HandlerMethod的方法参数解析器 返回值解析器 数据绑定工厂 参数名称解析器
 *     <pre>
 *         invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers)
 *         invocableMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
 *         invocableMethod.setDataBinderFactory(binderFactory);
 * 		   invocableMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
 *     </pre>
 *     5.创建ModelAndViewContainer
 *     @see org.springframework.web.method.support.ModelAndViewContainer
 *     <pre>
 *          ModelAndViewContainer mavContainer = new ModelAndViewContainer();
 * 			mavContainer.addAllAttributes(RequestContextUtils.getInputFlashMap(request));
 * 			modelFactory.initModel(webRequest, mavContainer, invocableMethod);
 * 			mavContainer.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);
 *     </pre>
 *     6.异步处理操作
 *     ......
 *     7.调用处理方法
 *     @see org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod#invokeAndHandle(org.springframework.web.context.request.ServletWebRequest, org.springframework.web.method.support.ModelAndViewContainer, java.lang.Object...)
 *     @see org.springframework.web.method.support.InvocableHandlerMethod#invokeForRequest(org.springframework.web.context.request.NativeWebRequest, org.springframework.web.method.support.ModelAndViewContainer, java.lang.Object...)
 *     <pre>
 *         取调用方法的参数值
 *         @see org.springframework.web.method.support.InvocableHandlerMethod#getMethodArgumentValues(org.springframework.web.context.request.NativeWebRequest, org.springframework.web.method.support.ModelAndViewContainer, java.lang.Object...)
 *         解析参数
 *         @see org.springframework.web.method.annotation.ModelAttributeMethodProcessor#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest, org.springframework.web.bind.support.WebDataBinderFactory)
 *         // 参数对象被实例化 (未给属性赋值)
 *         @see org.springframework.web.method.annotation.ModelAttributeMethodProcessor#createAttribute(java.lang.String, org.springframework.core.MethodParameter, org.springframework.web.bind.support.WebDataBinderFactory, org.springframework.web.context.request.NativeWebRequest)
 *         创建数据绑定器
 *         @see org.springframework.web.bind.support.DefaultDataBinderFactory#createBinder(org.springframework.web.context.request.NativeWebRequest, java.lang.Object, java.lang.String)
 *         初始化数据绑定器(这里如果有验证器会为数据绑定器设置验证器)
 *         @see org.springframework.web.bind.support.ConfigurableWebBindingInitializer#initBinder(org.springframework.web.bind.WebDataBinder)
 *         绑定请求参数到处理器方法参数上
 *         @see org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor#bindRequestParameters(org.springframework.web.bind.WebDataBinder, org.springframework.web.context.request.NativeWebRequest)
 *         @see org.springframework.web.bind.ServletRequestDataBinder#bind(javax.servlet.ServletRequest)
 *         @see org.springframework.validation.DataBinder#doBind(org.springframework.beans.MutablePropertyValues)
 *         <pre>
 *             checkAllowedFields(mpvs);
 * 		       checkRequiredFields(mpvs);
 * 		       applyPropertyValues(mpvs);
 *         </pre>
 *         @see org.springframework.validation.DataBinder#applyPropertyValues(org.springframework.beans.MutablePropertyValues)
 *         如果开启了严重 验证参数
 *         @see org.springframework.web.method.annotation.ModelAttributeMethodProcessor#validateIfApplicable(org.springframework.web.bind.WebDataBinder, org.springframework.core.MethodParameter)
 *
 *         参数绑定完成 调用目标方法
 *         @see org.springframework.web.method.support.InvocableHandlerMethod#doInvoke(java.lang.Object...)
 *
 *         处理调用结果值
 *         @see org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor#handleReturnValue(java.lang.Object, org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest)
 *         <pre>
 *             ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
 * 		       ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
 *
 * 		       // Try even with null return value. ResponseBodyAdvice could get involved.
 * 		       writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
 *         </pre>
 *     </pre>
 *     8.返回ModelAndView
 *
 * </pre>
 * 5).处理返回ModelAndView中如果不包含视图 尝试获取默认视图 设置到返回的ModelAndView中
 *
 * 6).执行处理器的拦截器的后置拦截方法(从后往前interceptors)
 *    <pre>
 *        for (int i = interceptors.length - 1; i >= 0; i--) {
 * 				HandlerInterceptor interceptor = interceptors[i];
 * 				interceptor.postHandle(request, response, this.handler, mv);
 *        }
 *    </pre>
 * 7).对处理器处理后的结果进行处理 即渲染视图
 * @see org.springframework.web.servlet.DispatcherServlet#processDispatchResult(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.HandlerExecutionChain, org.springframework.web.servlet.ModelAndView, java.lang.Exception)
 *
 * 8).执行拦截器完成拦截方法
 *
 *
 * @auther mac
 * @date 2020-01-16
 */
@ComponentScan(
        basePackages = {"org.mac.explorations.framework.spring.web.mvc"},
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class})}
)
@EnableWebMvc
public class WebMVCApplicationContextConfiguration  implements WebMvcConfigurer {

    //视图解析器
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    //静态资源访问
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    //不经过Handler 直接映射
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index");
    }

    //拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new SimpleAsyncHandlerInterceptor()).addPathPatterns("/**");
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setProviderClass(HibernateValidator.class);
        return validatorFactoryBean;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
