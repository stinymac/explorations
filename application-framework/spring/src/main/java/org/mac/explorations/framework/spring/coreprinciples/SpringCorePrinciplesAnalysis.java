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

package org.mac.explorations.framework.spring.coreprinciples;

import com.zaxxer.hikari.HikariDataSource;
import org.mac.explorations.framework.spring.coreprinciples.component.Brand;
import org.mac.explorations.framework.spring.coreprinciples.component.ChineseRabbit;
import org.mac.explorations.framework.spring.coreprinciples.component.ChineseRabbitBeanPostProcessor;
import org.mac.explorations.framework.spring.coreprinciples.component.GlobalIdentifierBeanDefinitionRegistryPostProcessor;
import org.mac.explorations.framework.spring.coreprinciples.component.JdbcProperties;
import org.mac.explorations.framework.spring.coreprinciples.component.PrintBeanFactoryPostProcessor;
import org.mac.explorations.framework.spring.coreprinciples.component.SimpleContextRefreshedEventApplicationListener;
import org.mac.explorations.framework.spring.coreprinciples.component.SimpleImportSelector;
import org.mac.explorations.framework.spring.coreprinciples.component.SimpleLocaleChinaMatchingCondition;
import org.mac.explorations.framework.spring.coreprinciples.component.service.SimpleSampleService;
import org.springframework.beans.support.ResourceEditorRegistrar;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListenerMethodProcessor;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Spring 初始化流程分析
 *
 * 1.上下文对象(核心是BeanFactory)初始化
 * 从new AnnotationConfigApplicationContext()开始 类静态属性初始化
 * @see org.springframework.context.support.AbstractApplicationContext
 * 初始化的静态属性如下:
 *     MESSAGE_SOURCE_BEAN_NAME = "messageSource"
 *     LIFECYCLE_PROCESSOR_BEAN_NAME = "lifecycleProcessor"
 *     CLASSPATH_URL_PREFIX = "classpath:"
 *     APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster"
 *     CONFIG_LOCATION_DELIMITERS = ",; \t\n"
 *     CONVERSION_SERVICE_BEAN_NAME = "conversionService"
 *     LOAD_TIME_WEAVER_BEAN_NAME = "loadTimeWeaver"
 *     ENVIRONMENT_BEAN_NAME = "environment"
 *     SYSTEM_PROPERTIES_BEAN_NAME = "systemProperties"
 *     SYSTEM_ENVIRONMENT_BEAN_NAME = "systemEnvironment"
 *     SHUTDOWN_HOOK_THREAD_NAME = "SpringContextShutdownHook"
 *     FACTORY_BEAN_PREFIX = "&"
 *     CLASSPATH_ALL_URL_PREFIX = "classpath*:"
 *
 * 通过查看AbstractApplicationContext的继承层次，
 *
 * 可以发现以上静态属性分别来自于
 * @see org.springframework.context.support.AbstractApplicationContext
 * @see org.springframework.context.ConfigurableApplicationContext
 * @see org.springframework.beans.factory.BeanFactory
 * @see org.springframework.core.io.ResourceLoader
 * @see org.springframework.core.io.support.ResourcePatternResolver
 *
 * AnnotationConfigApplicationContext构造器执行
 * 依次执行父类构造器
 * @see org.springframework.core.io.DefaultResourceLoader#DefaultResourceLoader()
 * 类属性初始化
 * <pre>
 *     private final Set<ProtocolResolver> protocolResolvers = new LinkedHashSet<>(4);
 *
 * 	   private final Map<Class<?>, Map<Resource, ?>> resourceCaches = new ConcurrentHashMap<>(4);
 * </pre>
 * DefaultResourceLoader的构造器执行初始化属性classLoader的值
 * @see org.springframework.util.ClassUtils#getDefaultClassLoader()
 * <pre>
 *     this.classLoader = ClassUtils.getDefaultClassLoader();
 * </pre>
 *
 * @see org.springframework.context.support.AbstractApplicationContext#AbstractApplicationContext()
 * 类属性初始化
 * <pre>
 *     // 日志记录器初始化 @see {@link org.apache.commons.logging.LogAdapter#createLog(java.lang.String)}
 *     protected final Log logger = LogFactory.getLog(getClass());
 *     private String id = ObjectUtils.identityToString(this);
 *     private String displayName=ObjectUtils.identityToString(this);
 *     //BeanFactoryPostProcessor 容器
 *     private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();
 *     //容器激活标识
 *     private final AtomicBoolean active = new AtomicBoolean();
 *     //容器关闭标识
 *     private final AtomicBoolean closed = new AtomicBoolean();
 *     //容器激活、关闭锁对象
 *     private final Object startupShutdownMonitor = new Object();
 *     // 应用监听器容器
 *     private final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();
 * </pre>
 * AbstractApplicationContext构造器执行 初始化resourcePatternResolver
 * @see org.springframework.context.support.AbstractApplicationContext#getResourcePatternResolver()
 * <pre>
 *     this.resourcePatternResolver = getResourcePatternResolver();
 * </pre>
 * @see org.springframework.core.io.support.PathMatchingResourcePatternResolver
 * @see org.springframework.util.PathMatcher
 * @see org.springframework.util.AntPathMatcher
 *
 * @see org.springframework.context.support.GenericApplicationContext#GenericApplicationContext()
 * 类属性初始化
 * <pre>
 *     private boolean customClassLoader = false;
 *     private final AtomicBoolean refreshed = new AtomicBoolean();
 * </pre>
 * GenericApplicationContext构造器执行 初始化创建BeanFactory
 * <pre>
 *     this.beanFactory = new DefaultListableBeanFactory();
 *
 * </pre>
 * 首先静态初始化javaxInjectProviderClass(即用于处理@Inject)
 * private static final Map<String, Reference<DefaultListableBeanFactory>> serializableFactories = new ConcurrentHashMap<>(8);
 * 依次执行DefaultListableBeanFactory的父类构造器
 *
 * @see org.springframework.core.SimpleAliasRegistry
 *     属性初始化
 *     <pre>
 *         private final Map<String, String> aliasMap = new ConcurrentHashMap<>(16);
 *     </pre>
 * @see org.springframework.beans.factory.support.DefaultSingletonBeanRegistry
 *     属性初始化
 *     <pre>
 *         private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
 *         private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
 *         private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);
 *         private final Set<String> registeredSingletons = new LinkedHashSet<>(256);
 *         private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));
 *         private final Set<String> inCreationCheckExclusions = Collections.newSetFromMap(new ConcurrentHashMap<>(16));
 *         private boolean singletonsCurrentlyInDestruction = false;
 *         private final Map<String, Object> disposableBeans = new LinkedHashMap<>();
 *         private final Map<String, Set<String>> containedBeanMap = new ConcurrentHashMap<>(16);
 *         private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);
 *         private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);
 *     </pre>
 * @see org.springframework.beans.factory.support.FactoryBeanRegistrySupport
 *     属性初始化
 *     <pre>
 *         private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>(16);
 *     </pre>
 *
 * @see org.springframework.beans.factory.support.AbstractBeanFactory
 *     属性初始化
 *     <pre>
 *         private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
 *         private boolean cacheBeanMetadata = true;
 *         // bean 属性Editor容器
 *         private final Set<PropertyEditorRegistrar> propertyEditorRegistrars = new LinkedHashSet<>(4);
 *         private final Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap<>(4);
 *         private final List<StringValueResolver> embeddedValueResolvers = new CopyOnWriteArrayList<>();
 *         // BeanPostProcessor 容器
 *         private final List<BeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList<>();
 *         private final Map<String, Scope> scopes = new LinkedHashMap<>(8);
 *         // bean 定义 容器
 *         private final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap<>(256);
 *         private final Set<String> alreadyCreated = Collections.newSetFromMap(new ConcurrentHashMap<>(256));
 *         private final ThreadLocal<Object> prototypesCurrentlyInCreation = new NamedThreadLocal<>("Prototype beans currently in creation");
 *
 *     </pre>
 *
 * @see org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory
 *     属性初始化
 *     <pre>
 *         // 实例化策略@see {@link org.springframework.beans.factory.support.CglibSubclassingInstantiationStrategy}
 *         private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();
 *         //方法参数名获取@see {@link org.springframework.core.DefaultParameterNameDiscoverer}
 *         private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
 *         private boolean allowCircularReferences = true;
 *         private boolean allowRawInjectionDespiteWrapping = false;
 *         private final Set<Class<?>> ignoredDependencyTypes = new HashSet<>();
 *         private final Set<Class<?>> ignoredDependencyInterfaces = new HashSet<>();
 *         private final NamedThreadLocal<String> currentlyCreatedBean = new NamedThreadLocal<>("Currently created bean");
 *         private final ConcurrentMap<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();
 *         private final ConcurrentMap<Class<?>, Method[]> factoryMethodCandidateCache = new ConcurrentHashMap<>();
 *         private final ConcurrentMap<Class<?>, PropertyDescriptor[]> filteredPropertyDescriptorsCache = new ConcurrentHashMap<>();
 *     </pre>
 * AbstractAutowireCapableBeanFactory 构造器执行
 * <pre>
 *     ignoreDependencyInterface(BeanNameAware.class);
 * 	ignoreDependencyInterface(BeanFactoryAware.class);
 * 	ignoreDependencyInterface(BeanClassLoaderAware.class);
 * </pre>
 *
 * DefaultListableBeanFactory 属性初始化
 * <pre>
 *     private boolean allowBeanDefinitionOverriding = true;
 *     private boolean allowEagerClassLoading = true;
 *     //@see {@link org.springframework.beans.factory.support.SimpleAutowireCandidateResolver}
 *     private AutowireCandidateResolver autowireCandidateResolver = new SimpleAutowireCandidateResolver();
 *     private final Map<Class<?>, Object> resolvableDependencies = new ConcurrentHashMap<>(16);
 * 	   private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
 * 	   private final Map<Class<?>, String[]> allBeanNamesByType = new ConcurrentHashMap<>(64);
 * 	   private final Map<Class<?>, String[]> singletonBeanNamesByType = new ConcurrentHashMap<>(64);
 * 	   private volatile List<String> beanDefinitionNames = new ArrayList<>(256);
 * 	   private volatile Set<String> manualSingletonNames = new LinkedHashSet<>(16);
 *     private volatile boolean configurationFrozen = false;
 * </pre>
 * DefaultListableBeanFactory  构造器执行
 * GenericApplicationContext初始化构造执行完毕
 *
 * AnnotationConfigApplicationContext构造器执行
 * <pre>
 *     this.reader = new AnnotatedBeanDefinitionReader(this);
 * 	   this.scanner = new ClassPathBeanDefinitionScanner(this);
 * </pre>
 * @see org.springframework.context.annotation.AnnotatedBeanDefinitionReader 初始化
 * 属性初始化
 * <pre>
 *     private BeanNameGenerator beanNameGenerator = AnnotationBeanNameGenerator.INSTANCE;
 *     private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
 * </pre>
 * 构造器执行
 * <pre>
 *     @see org.springframework.core.env.StandardEnvironment
 *     this(registry, getOrCreateEnvironment(registry));
 * </pre>
 * <pre>
 *     this.registry = registry;
 *     //@see {@link org.springframework.context.annotation.ConditionEvaluator}
 *     //@see {@link org.springframework.context.annotation.ConditionEvaluator.ConditionContextImpl}
 * 	   this.conditionEvaluator = new ConditionEvaluator(registry, environment, null);
 * 	   // 注册注解配置处理器
 * 	   // @see {@link org.springframework.context.annotation.AnnotationConfigUtils#registerAnnotationConfigProcessors(org.springframework.beans.factory.support.BeanDefinitionRegistry, java.lang.Object)}
 * 	   // 这里AutowireCandidateResolver被替换为 @see {@link org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver}
 * 	   //注册的处理器是@see {@link org.springframework.context.annotation.ConfigurationClassPostProcessor} (是一个BeanDefinitionRegistryPostProcessor的实现)
 * 	   //和@see {@link org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor}(是一个InstantiationAwareBeanPostProcessorAdapter和MergedBeanDefinitionPostProcessor实现(这两个接口都是BeanPostProcessor子接口))
 * 	   // 如果需要指出jsr250 注册@see {@link org.springframework.context.annotation.CommonAnnotationBeanPostProcessor} (是一个InstantiationAwareBeanPostProcessor实现)
 * 	   // 如果需要支持JPA 加载@see {@link org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor}并注册(是一个InstantiationAwareBeanPostProcessor实现)
 * 	   // 注册@see {@link org.springframework.context.event.EventListenerMethodProcessor}(是一个SmartInitializingSingleton, ApplicationContextAware, BeanFactoryPostProcessor的实现)
 * 	   // 注册@see {@link org.springframework.context.event.DefaultEventListenerFactory}(是EventListenerFactory实现)
 * 	   AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
 * </pre>
 * @see org.springframework.context.annotation.ClassPathBeanDefinitionScanner 初始化
 * 父类属性初始化
 * @see org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
 * <pre>
 *     //@see {@link org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider#DEFAULT_RESOURCE_PATTERN }
 *     private String resourcePattern = DEFAULT_RESOURCE_PATTERN;
 * 	   private final List<TypeFilter> includeFilters = new LinkedList<>();
 * 	   private final List<TypeFilter> excludeFilters = new LinkedList<>();
 * </pre>
 * 属性初始化
 * <pre>
 *     @see org.springframework.beans.factory.support.BeanDefinitionDefaults
 *     private BeanDefinitionDefaults beanDefinitionDefaults = new BeanDefinitionDefaults();
 *    @see org.springframework.context.annotation.AnnotationBeanNameGenerator
 *    private BeanNameGenerator beanNameGenerator = AnnotationBeanNameGenerator.INSTANCE;
 *    @see org.springframework.context.annotation.AnnotationScopeMetadataResolver
 *    private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
 * 	  private boolean includeAnnotationConfig = true;
 * </pre>
 * 构造器执行
 * <pre>
 *     if (useDefaultFilters) {
 *          @see org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider#registerDefaultFilters()
 * 			registerDefaultFilters();
 *      }
 * 		setEnvironment(environment);
 * 		setResourceLoader(resourceLoader);
 * </pre>
 *
 * 上下文对象(核心是BeanFactory)初始化总结:
 *
 * 1).按AnnotationConfigApplicationContext的继承层次 依次初始化静态属性
 *    (如 CLASSPATH_ALL_URL_PREFIX 等),非静态属性(如 logger beanFactoryPostProcessors 容器
 *     applicationListeners resourcePatternResolver 等)
 *
 * 2).初始化BeanFactory对象(这里是DefaultListableBeanFactory) 主要初始化BeanFactory的各种容器
 *    (如
 *     Map<String, Object> singletonObjects
 *     Set<String> registeredSingletons
 *     Map<String, Set<String>> dependenciesForBeanMap
 *     Set<PropertyEditorRegistrar> propertyEditorRegistrars
 *     List<BeanPostProcessor> beanPostProcessors
 *     Map<String, RootBeanDefinition> mergedBeanDefinitions
 *     Map<String, BeanDefinition> beanDefinitionMap
 *     List<String> beanDefinitionNames
 *     ......
 *    )
 *
 * 3).为容器(上下文)初始化AnnotationBeanDefinitionReader和ClassPathBeanDefinitionScanner
 *    AnnotationBeanDefinitionReader用于读取解析注解定义的Bean信息 因此这里向容器注册多个BeanPostProcessor
 *    (如
 *     ConfigurationClassPostProcessor
 *     AutowiredAnnotationBeanPostProcessor
 *     CommonAnnotationBeanPostProcessor
 *     EventListenerMethodProcessor
 *     DefaultEventListenerFactory
 *     ContextAnnotationAutowireCandidateResolver
 *    )
 *    ClassPathBeanDefinitionScanner同AnnotationBeanDefinitionReader类似扫描并解析处理注解定义的Bean信息
 *
 * 2.向上下文容器中注册注解配置类的Class对象
 * 调用AnnotationBeanDefinitionReader的注册方法注册配置类Class对象
 * <pre>
 *     this.reader.register(componentClasses);
 * </pre>
 * @see org.springframework.context.annotation.AnnotatedBeanDefinitionReader#doRegisterBean(java.lang.Class, java.lang.String, java.lang.Class[], java.util.function.Supplier, org.springframework.beans.factory.config.BeanDefinitionCustomizer[])
 *
 * 配置类Class对象传入@see {@link org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition}
 * 这里会依次初始化
 * @see org.springframework.core.AttributeAccessorSupport
 * <pre>
 *     private final Map<String, Object> attributes = new LinkedHashMap<>();
 * </pre>
 * @see org.springframework.beans.BeanMetadataAttributeAccessor
 * @see org.springframework.beans.factory.support.AbstractBeanDefinition
 * <pre>
 *     private int autowireMode = AUTOWIRE_NO;
 * 	   private int dependencyCheck = DEPENDENCY_CHECK_NONE;
 *     private String[] dependsOn;
 *     private boolean autowireCandidate = true;
 *	   private boolean primary = false;
 *	   private final Map<String, AutowireCandidateQualifier> qualifiers = new LinkedHashMap<>();
 *    private Supplier<?> instanceSupplier;
 *    private boolean nonPublicAccessAllowed = true;
 *    private boolean lenientConstructorResolution = true;
 *    //@see {@link org.springframework.beans.factory.support.MethodOverrides}
 *    private MethodOverrides methodOverrides = new MethodOverrides();
 *    private boolean enforceInitMethod = true;
 * 	  private boolean enforceDestroyMethod = true;
 * 	  //synthetic - 综合的;合成的;
 * 	  private boolean synthetic = false;
 * 	  private int role = BeanDefinition.ROLE_APPLICATION;
 * 	  ......
 * </pre>
 * 然后反射获取配置类的注解元信息
 * @see org.springframework.core.type.StandardAnnotationMetadata#from(java.lang.Class)
 * <pre>
 *     static AnnotationMetadata from(Class<?> introspectedClass) {
 * 		   return new StandardAnnotationMetadata(introspectedClass, true);
 *     }
 * </pre>
 * @see org.springframework.core.type.StandardAnnotationMetadata#StandardAnnotationMetadata(java.lang.Class, boolean)
 * <pre>
 *     this.mergedAnnotations = MergedAnnotations.from(introspectedClass,
 * 				SearchStrategy.INHERITED_ANNOTATIONS, RepeatableContainers.none(),
 * 				AnnotationFilter.NONE);
 * </pre>
 * @see org.springframework.core.annotation.MergedAnnotations#from(java.lang.reflect.AnnotatedElement, org.springframework.core.annotation.MergedAnnotations.SearchStrategy, org.springframework.core.annotation.RepeatableContainers, org.springframework.core.annotation.AnnotationFilter)
 * @see org.springframework.core.annotation.TypeMappedAnnotations#TypeMappedAnnotations(java.lang.reflect.AnnotatedElement, org.springframework.core.annotation.MergedAnnotations.SearchStrategy, org.springframework.core.annotation.RepeatableContainers, org.springframework.core.annotation.AnnotationFilter)
 * 即配置类被作为一个Bean的Bean定义(AnnotatedGenericBeanDefinition)信息初始化完成
 *
 * 下面解析设置Bean定义信息
 * 判断是否应该跳过这个配置类
 * @see org.springframework.context.annotation.ConditionEvaluator#shouldSkip(org.springframework.core.type.AnnotatedTypeMetadata, org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase)
 * 解析Bean定义的ScopeMetadata(配置类 一般不配置就是singleton)
 * @see org.springframework.context.annotation.AnnotationScopeMetadataResolver#resolveScopeMetadata(org.springframework.beans.factory.config.BeanDefinition)
 * 生成BeanName(这里是 springCorePrinciplesAnalysis )
 * 处理公共定义注解(@lazy @Primary @DependsOn @Role @Description)
 * @see org.springframework.context.annotation.AnnotationConfigUtils#processCommonDefinitionAnnotations(org.springframework.beans.factory.annotation.AnnotatedBeanDefinition, org.springframework.core.type.AnnotatedTypeMetadata)
 * 处理@qualifiers注解
 * 添加这个Bean定义的自定义的BeanDefinition
 * 将Bean定义包装为BeanDefinitionHolder
 * @see org.springframework.beans.factory.config.BeanDefinitionHolder#BeanDefinitionHolder(org.springframework.beans.factory.config.BeanDefinition, java.lang.String)
 * 设置Bean代理模式(配置类这里是 ScopedProxyMode.NO)
 * 注册Bean定义
 * @see org.springframework.beans.factory.support.BeanDefinitionReaderUtils#registerBeanDefinition(org.springframework.beans.factory.config.BeanDefinitionHolder, org.springframework.beans.factory.support.BeanDefinitionRegistry)
 * 即放入BeanFactory的BeanDefinition容器中
 * @see org.springframework.beans.factory.support.DefaultListableBeanFactory#registerBeanDefinition(java.lang.String, org.springframework.beans.factory.config.BeanDefinition)
 * <pre>
 *     this.beanDefinitionMap.put(beanName, beanDefinition);
 * 	   this.beanDefinitionNames.add(beanName);
 * 	   removeManualSingletonName(beanName);
 * </pre>
 *
 * 总结以上过程即是:
 * 配置类本身的Class对向被提供给上下文容器的AnnotationBeanDefinitionReader
 * 它解析配置类的元信息 并将配置类本身作为一个Bean 为其解析并生成Bean定义然后注册到
 * 上下文容器中(BeanFactory的BeanDefinition容器中)
 *
 * 3.上下文容器刷新
 * @see org.springframework.context.support.AbstractApplicationContext#refresh()
 * 其主要的流程步骤为
 * <pre>
 *     // Prepare this context for refreshing.
 * 	   prepareRefresh();
 *
 * 	   // Tell the subclass to refresh the internal bean factory.
 * 	   // 刷新bean factory (使用CAS保证安全刷新和设置序列化ID)
 * 	   ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
 *
 * 	   // Prepare the bean factory for use in this context.
 * 	   prepareBeanFactory(beanFactory);
 *
 * 	   // Allows post-processing of the bean factory in context subclasses.
 * 	   postProcessBeanFactory(beanFactory);
 *
 * 	   // Invoke factory processors registered as beans in the context.
 * 	   invokeBeanFactoryPostProcessors(beanFactory);
 *
 * 	   // Register bean processors that intercept bean creation.
 * 	   registerBeanPostProcessors(beanFactory);
 *
 * 	   // Initialize message source for this context.
 * 	   initMessageSource();
 *
 * 	   // Initialize event multicaster for this context.
 * 	   initApplicationEventMulticaster();
 *
 * 	   // Initialize other special beans in specific context subclasses.
 * 	   onRefresh();
 *
 * 	   // Check for listener beans and register them.
 * 	   registerListeners();
 *
 * 	   // Instantiate all remaining (non-lazy-init) singletons.
 * 	   finishBeanFactoryInitialization(beanFactory);
 *
 * 	   // Last step: publish corresponding event.
 * 	   finishRefresh();
 * </pre>
 *
 * 1).prepareRefresh()
 * @see org.springframework.context.support.AbstractApplicationContext#prepareRefresh()
 * <pre>
 *      // 初始化上下文环境中的Property Sources 由子类实现
 *      initPropertySources();
 *      // 验证Required Properties
 * 		getEnvironment().validateRequiredProperties();
 *
 * 		if (this.earlyApplicationListeners == null) {
 * 			this.earlyApplicationListeners = new LinkedHashSet<>(this.applicationListeners);
 *      }
 * 		else {
 * 			this.applicationListeners.clear();
 * 			this.applicationListeners.addAll(this.earlyApplicationListeners);
 *      }
 * 		this.earlyApplicationEvents = new LinkedHashSet<>();
 * </pre>
 * 2).prepareBeanFactory(beanFactory)
 * @see org.springframework.context.support.AbstractApplicationContext#prepareBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
 * <pre>
 *     beanFactory.setBeanClassLoader(getClassLoader());
 *
 *    //@see {@link org.springframework.context.expression.StandardBeanExpressionResolver}
 *     //@see {@link org.springframework.expression.spel.standard.SpelExpressionParser}
 * 	   beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
 *
 * 	   //@see {@link ResourceEditorRegistrar#registerCustomEditors(org.springframework.beans.PropertyEditorRegistry)}
 * 	   beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));
 *
 *     //@see {@link org.springframework.context.support.ApplicationContextAwareProcessor}
 * 	   beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
 * 	   ......
 * 	   添加需要忽略的内置依赖接口 如EnvironmentAware等
 * 	   注册可解析的内置依赖接口 如BeanFactory等
 * 	   ......
 * 	   添加一个ApplicationListenerDetector的BeanPostProcessor用于检测内置的ApplicationListenner
 * 	   @see org.springframework.context.support.ApplicationListenerDetector
 *
 * 	   如果需要添加一个用于加载时织如的BeanPostProcessor
 * 	   @see org.springframework.context.weaving.LoadTimeWeaverAwareProcessor
 * 	   @see org.springframework.context.weaving.LoadTimeWeaverAwareProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
 *
 *     注册默认的环境Bean(StandardEnviroment ,SystemProperties, SystemEnvironment)
 *     @see org.springframework.core.env.StandardEnvironment
 *     @see org.springframework.core.env.AbstractEnvironment#getSystemProperties() // Map
 *     @see org.springframework.core.env.AbstractEnvironment#getSystemEnvironment()// Map
 *     @see org.springframework.beans.factory.support.DefaultSingletonBeanRegistry#addSingleton(java.lang.String, java.lang.Object)
 * </pre>
 * 3).invokeBeanFactoryPostProcessors(beanFactory)
 * @see org.springframework.context.support.AbstractApplicationContext#invokeBeanFactoryPostProcessors(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
 * @see org.springframework.context.support.PostProcessorRegistrationDelegate#invokeBeanFactoryPostProcessors(org.springframework.beans.factory.config.ConfigurableListableBeanFactory, java.util.List)
 * <pre>
 *     1.首先调用已经注册在BeanFactory中的所有BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry
 *     2.调用完成后从BeanFactory中获取全部的BeanDefinitionRegistryPostProcessor从中筛选出全部实现了
 *       PriorityOrdered节后的BeanDefinitionRegistryPostProcessor 后对其排序后初始化(getBean)，然后依次调用每个BeanDefinitionRegistryPostProcessor
 *       的PostProcessor的postProcessBeanDefinitionRegistry
 *    3.按2的方式再进行一遍 不同的是这次筛选实现了Ordered接口的
 *    4.按2的方式又进行一遍 不同的是这次筛选全部的普通的BeanDefinitionRegistryPostProcessor
 *    5.全部的BeanDefinitionRegistryPostProcessor(是BeanFactoryPostProcessor的子接口)调用postProcessBeanFactory方法
 *    6.从BeanFactory中取全部的BeanFactoryPostProcessor后按PriorityOrdered 、Ordered和普通的分类后
 *      排序并依次调用postProcessBeanFactory(BeanFactoryPostProcessor不用多次从BeanFactory中获取 因为这时不会再有机会通过
 *      BeanDefinitionRegistryPostProcessor向容器中注册新的Bean定义了)
 * </pre>
 * 在invokeBeanFactoryPostProcessors这一步中全部的配置的Bean的定义信息被解析出注册到容器中
 * 其处理器是在初始化容器的AnnotationBeanDefinitionReader的时候注册到容器中的ConfigurationClassPostProcessor
 * @see org.springframework.context.annotation.ConfigurationClassPostProcessor
 * 它是一个BeanDefinitionRegistryPostProcessor实现
 * @see org.springframework.context.annotation.ConfigurationClassPostProcessor#postProcessBeanDefinitionRegistry(org.springframework.beans.factory.support.BeanDefinitionRegistry)
 * @see org.springframework.context.annotation.ConfigurationClassPostProcessor#processConfigBeanDefinitions(org.springframework.beans.factory.support.BeanDefinitionRegistry)
 * <pre>
 *     1.从此时BeanFactory中的BeanDefinitions中筛选出配置类
 *     @see org.springframework.context.annotation.ConfigurationClassUtils#checkConfigurationClassCandidate(org.springframework.beans.factory.config.BeanDefinition, org.springframework.core.type.classreading.MetadataReaderFactory)
 *     2.对配置类排序
 *     3.创建配置解析器
 *     ConfigurationClassParser parser = new ConfigurationClassParser(
 * 				this.metadataReaderFactory, this.problemReporter, this.environment,
 * 				this.resourceLoader, this.componentScanBeanNameGenerator, registry);
 *     @see org.springframework.context.annotation.ConfigurationClassParser
 *     解析配置类
 *     @see org.springframework.context.annotation.ConfigurationClassParser#parse(java.util.Set)
 *     @see org.springframework.context.annotation.ConfigurationClassParser#processConfigurationClass(org.springframework.context.annotation.ConfigurationClass)
 *     @see org.springframework.context.annotation.ConfigurationClassParser#doProcessConfigurationClass(org.springframework.context.annotation.ConfigurationClass, org.springframework.context.annotation.ConfigurationClassParser.SourceClass)
 *         <pre>
 *             @see org.springframework.context.annotation.ConfigurationClassParser#processMemberClasses(org.springframework.context.annotation.ConfigurationClass, org.springframework.context.annotation.ConfigurationClassParser.SourceClass)
 *             //@PropertySource注解处理
 *             @see org.springframework.context.annotation.ConfigurationClassParser#processPropertySource(org.springframework.core.annotation.AnnotationAttributes)
 *             // 处理@ComponentScan注解
 *             @see org.springframework.context.annotation.ComponentScanAnnotationParser#parse(org.springframework.core.annotation.AnnotationAttributes, java.lang.String)
 *             @see org.springframework.context.annotation.ClassPathBeanDefinitionScanner#doScan(java.lang.String...)
 *             检查扫面出的类中是否有需要配置类 有则需要时进行递归解析(如这里的JdbcProperties LoggingAspect等)
 *             // 处理@Import注解
 *             @see org.springframework.context.annotation.ConfigurationClassParser#processImports(org.springframework.context.annotation.ConfigurationClass, org.springframework.context.annotation.ConfigurationClassParser.SourceClass, java.util.Collection, boolean)
 *             // 处理@ImportResource注解
 *             // 处理@Bean
 *
 *         </pre>
 *     @see org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader#loadBeanDefinitions(java.util.Set)
 *     4.配置类处理器的postProcessBeanFactory方法执行
 *     @see org.springframework.context.annotation.ConfigurationClassPostProcessor#postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
 *     <pre>
 *         // 配置类增强
 *         // @see {@link org.springframework.context.annotation.ConfigurationClassPostProcessor#enhanceConfigurationClasses(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)}
 *         enhanceConfigurationClasses(beanFactory);
 *         // @see {@link org.springframework.context.annotation.ConfigurationClassPostProcessor.ImportAwareBeanPostProcessor}
 * 		   beanFactory.addBeanPostProcessor(new ImportAwareBeanPostProcessor(beanFactory));
 *     </pre>
 *     @see EventListenerMethodProcessor#postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
 *
 *     *向容器中注册
 *     @see org.springframework.transaction.event.TransactionalEventListenerFactory
 * </pre>
 *
 * 4).registerBeanPostProcessors(beanFactory)
 * @see org.springframework.context.support.PostProcessorRegistrationDelegate#registerBeanPostProcessors(org.springframework.beans.factory.config.ConfigurableListableBeanFactory, org.springframework.context.support.AbstractApplicationContext)
 * <pr>
 *     1.先添加一个BeanPostProcessorChecker
 *     @see org.springframework.context.support.PostProcessorRegistrationDelegate.BeanPostProcessorChecker#postProcessAfterInitialization(java.lang.Object, java.lang.String)
 *     2.BeanPostProcessor分类
 *     priorityOrderedPostProcessors
 *         1 = {CommonAnnotationBeanPostProcessor}
 *         0 = {AutowiredAnnotationBeanPostProcessor}
 *    internalPostProcessors
 *        0 = {AutowiredAnnotationBeanPostProcessor}
 *        1 = {CommonAnnotationBeanPostProcessor}
 *    orderedPostProcessorNames
 *       0 = "org.springframework.aop.config.internalAutoProxyCreator"
 *    priorityOrderedPostProcessors排序并注册
 *    @see org.springframework.beans.factory.support.AbstractBeanFactory#addBeanPostProcessor(org.springframework.beans.factory.config.BeanPostProcessor)
 *    orderedPostProcessor实例化初始化(getBean),然后排序并注册
 *    ---------------------------------------------------
 *    在代理创建器AnnotationAwareAspectJAutoProxyCreator注册后
 *    其他的BeanPostProcessor调用getBean实例化的时候
 *    @see org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#createBean(java.lang.String, org.springframework.beans.factory.support.RootBeanDefinition, java.lang.Object[])
 *    <pre>
 *        // Give BeanPostProcessors a chance to return a proxy instead of the target bean instance.
 * 		  Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
 *    </pre>
 *    尝试创建代理
 *    @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#postProcessBeforeInstantiation(java.lang.Class, java.lang.String)
 *    @see org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator#shouldSkip(java.lang.Class, java.lang.String)
 *    @see org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator#findCandidateAdvisors()
 *    @see org.springframework.aop.framework.autoproxy.BeanFactoryAdvisorRetrievalHelper#findAdvisorBeans()
 *    会开始初始化AOP代理相关的组件
 *    @see org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration
 *    @see org.springframework.transaction.annotation.AnnotationTransactionAttributeSource
 *    @see org.springframework.transaction.interceptor.TransactionInterceptor
 *    @see org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor
 *    ---------------------------------------------------
 *    普通的PostProcessor实例化初始化(getBean),然后排序并注册
 *    internalPostProcessors排序并注册
 *    最后向容器中添加一个 ApplicationListenerDetector
 *    @see org.springframework.context.support.ApplicationListenerDetector
 * </pr>
 * 5).initMessageSource()
 * @see org.springframework.context.support.AbstractApplicationContext#initMessageSource()
 * 有指定的MessageSource初始化指定MessageSource
 * 没有则默认注册@see {@link org.springframework.context.support.DelegatingMessageSource}到容器
 * 6).initApplicationEventMulticaster()
 * @see org.springframework.context.support.AbstractApplicationContext#initApplicationEventMulticaster()
 * 有指定的ApplicationEventMulticaster初始化指定
 * 没有则默认注册@see {@link org.springframework.context.event.SimpleApplicationEventMulticaster}到容器
 * 7).onRefresh()
 * // For subclasses: do nothing by default.
 * @see org.springframework.context.support.AbstractApplicationContext#onRefresh()
 * 8).registerListeners()
 * @see org.springframework.context.support.AbstractApplicationContext#registerListeners()
 * 将已注册的ApplicationContextListeners注册到ApplicationEventMulticaster
 * 并发布早期事件
 * 9).finishBeanFactoryInitialization(beanFactory)
 * 初始换剩下的非懒加载的单例Bean
 * @see org.springframework.context.support.AbstractApplicationContext#finishBeanFactoryInitialization(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
 * 10).finishRefresh()
 * @see org.springframework.context.support.AbstractApplicationContext#finishRefresh()
 * <pre>
 *      // Clear context-level resource caches (such as ASM metadata from scanning).
 * 		clearResourceCaches();
 *
 * 		// Initialize lifecycle processor for this context.
 * 		initLifecycleProcessor();
 *
 * 		// Propagate refresh to lifecycle processor first.
 *   	@see org.springframework.context.support.DefaultLifecycleProcessor
 *      @see org.springframework.context.support.DefaultLifecycleProcessor#onRefresh()
 * 		getLifecycleProcessor().onRefresh();
 *
 * 		// Publish the final event.
 * 	    @see org.springframework.context.support.AbstractApplicationContext#publishEvent(java.lang.Object, org.springframework.core.ResolvableType)
 * 	    @see org.springframework.context.event.SimpleApplicationEventMulticaster#multicastEvent(org.springframework.context.ApplicationEvent, org.springframework.core.ResolvableType)
 * 		publishEvent(new ContextRefreshedEvent(this));
 *
 * 		// Participate in LiveBeansView MBean, if active.
 * 	    @see org.springframework.context.support.LiveBeansView#registerApplicationContext(org.springframework.context.ConfigurableApplicationContext)
 * 		LiveBeansView.registerApplicationContext(this);
 * </pre>
 *
 * @auther mac
 * @date: 2020-01-17 21:01
 */
@Configuration
@ComponentScan(
        basePackages = {"org.mac.explorations.framework.spring.coreprinciples.component"},
        includeFilters = {@ComponentScan.Filter (type = FilterType.ANNOTATION,classes = {Service.class})}
)
@Import({
        Brand.class,
        SimpleImportSelector.class,
        GlobalIdentifierBeanDefinitionRegistryPostProcessor.class,
        PrintBeanFactoryPostProcessor.class,
        ChineseRabbitBeanPostProcessor.class,
        SimpleContextRefreshedEventApplicationListener.class
})
@PropertySource({"classpath:/jdbc.properties"})
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class SpringCorePrinciplesAnalysis {
    @Bean
    @Conditional(SimpleLocaleChinaMatchingCondition.class)
    public ChineseRabbit chineseRabbit() {
        return new ChineseRabbit();
    }

    @Bean
    public DataSource dataSource(JdbcProperties jdbcProperties){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(jdbcProperties.getDriverClass());
        dataSource.setJdbcUrl(jdbcProperties.getUrl());
        dataSource.setUsername(jdbcProperties.getUsername());
        dataSource.setPassword(jdbcProperties.getPassword());
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) throws Exception{
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("dev");
        context.register(SpringCorePrinciplesAnalysis.class);
        context.refresh();

        SimpleSampleService simpleSampleService = context.getBean(SimpleSampleService.class);
        simpleSampleService.doService();
    }
}
