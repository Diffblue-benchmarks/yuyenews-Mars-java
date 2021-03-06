package com.mars.ioc.factory;

import com.mars.aop.proxy.CglibProxy;
import com.mars.core.annotation.MarsAop;
import com.mars.core.annotation.MarsAopType;
import com.mars.core.annotation.Traction;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.logger.MarsLogger;
import com.mars.core.model.AopModel;
import com.mars.core.model.MarsBeanModel;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * bean工厂
 * @author yuye
 *
 */
public class BeanFactory {
	
	private static MarsLogger log = MarsLogger.getLogger(BeanFactory.class);

	private static MarsSpace constants = MarsSpace.getEasySpace();
	
	/**
	 * 创建bean
	 * @param className lei
	 * @return duixiang
	 */
	public static Object createBean(Class<?> className) throws Exception {
		try {

			Object hasStart = constants.getAttr(MarsConstant.HAS_START);
			if(hasStart != null){
				throw new Exception("只有Mars才可以调用此方法，不可以手动显式调用");
			}
			
			Map<String, AopModel> list = new HashMap<>();
			
			/* 判断当前类中有没有方法有 aop注解 */
			getAopClass(className,list);
			
			/* 如果有aop注解，则通过动态代理来创建bean */
			if(list != null && list.size()>0) {
				CglibProxy cglibProxy = new CglibProxy();
				return cglibProxy.getProxy(className, list);
			} else {
				/* 如果没有aop注解，则直接new一个bean */
				return className.getDeclaredConstructor().newInstance();
			}
			
		} catch (Exception e) {
			throw new Exception("创建["+className.getName()+"]类型的bean对象出现错误",e);
		} 
	}

	/**
	 * 获取aop类
	 * @param className lei
	 * @param list jihe
	 * @throws Exception cuowu
	 */
	private static void getAopClass(Class<?> className,Map<String,AopModel> list) throws Exception {
		
		MarsAopType allEasyAop = className.getAnnotation(MarsAopType.class);
		
		Method[] methods = className.getMethods();
		for(Method method : methods) {
			MarsAop marsAop = method.getAnnotation(MarsAop.class);
			Traction traction = method.getAnnotation(Traction.class);
			
			/* 校验同一个方法上不能同时存在aop和traction注解 */
			if(marsAop != null && traction != null) {
				log.error(className.getName()+"类中的["+method.getName()+"]方法同时存在EasyAop和Traction注解");
				throw new Exception(className.getName()+"类中的["+method.getName()+"]方法同时存在EasyAop和Traction注解");
			}
			
			/* 如果类的AOP注解不为空，那么将注解中的监听类 添加到集合中 */
			if(allEasyAop != null) {
				AopModel aopModel = new AopModel();
				aopModel.setCls(allEasyAop.className());
				list.put(method.getName(),aopModel);
			}
			
			/* 如果方法上也有AOP注解，那么以方法上的为准 */
			if(marsAop != null) {
				AopModel aopModel = new AopModel();
				aopModel.setCls(marsAop.className());
				list.put(method.getName(), aopModel);
			} else if(traction != null) {
				Class<?> aopClass = Class.forName(traction.className());
				AopModel aopModel = new AopModel();
				aopModel.setCls(aopClass);
				aopModel.setTractionLevel(traction.level());
				aopModel.setExecutorType(traction.executorType());
				list.put(method.getName(),aopModel);
			}
		}
	}
	
	
	/**
	 * 获取bean
	 * @param name mingc
	 * @return duix
	 */
	public static <T> T getBean(String name,Class<T> cls) throws Exception {
		try {

			Object marsBeans = constants.getAttr(MarsConstant.MARS_BEAN_OBJECTS);
			Map<String, MarsBeanModel> marsBeanObjects = new HashMap<>();
			if(marsBeans != null) {
				marsBeanObjects = (Map<String, MarsBeanModel>)marsBeans;
			} 
			
			return (T)marsBeanObjects.get(name).getObj();
		} catch (Exception e) {
			throw new Exception("找不到name为["+name+"]的bean",e);
		}
	}
}
