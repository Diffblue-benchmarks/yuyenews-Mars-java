package com.mars.junit;

import com.mars.core.after.StartAfter;
import com.mars.core.annotation.Resource;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.load.LoadClass;
import com.mars.core.logger.MarsLogger;
import com.mars.core.util.ConfigUtil;
import com.mars.ioc.factory.BeanFactory;
import com.mars.ioc.load.LoadEasyBean;
import com.mars.jdbc.base.BaseInitJdbc;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 单测启动器
 */
public class MarsJunitStart {

    private static MarsLogger log = MarsLogger.getLogger(MarsJunitStart.class);

    /**
     * 获取全局存储空间
     */
    private static MarsSpace constants = MarsSpace.getEasySpace();

    /**
     * 启动Mars框架
     */
    public static void start(BaseInitJdbc baseInitJdbc, String packName, Object obj, List<StartList> list) {
        try {
            if(constants.getAttr(MarsConstant.HAS_TEST) == null){
                log.info("程序启动中......");

                /* 加载框架数据 */
                load(baseInitJdbc,packName);

                /* 标识createBean方法已经调用完毕 */
                constants.setAttr(MarsConstant.HAS_START,"yes");

                /* 加载单测所需数据 */
                if(list != null){
                    for(StartList item : list){
                        item.load();
                    }
                }

                /* 启动after方法 */
                StartAfter.after();

                /* 标记已经为单测创建过资源了 */
                constants.setAttr(MarsConstant.HAS_TEST,"yes");
            }

            /* 给单测注入属性 */
            autoWrite(obj);

            log.info("开始执行单测......");
        } catch (Exception e) {
            log.error("",e);
            System.exit(0);
        }
    }

    /**
     * 加载所需的资源
     */
    private static void load(BaseInitJdbc baseInitJdbc,String packName) throws Exception{

        /* 加载配置文件 */
        ConfigUtil.loadConfig();

        /* 获取此包下面的所有类（包括jar中的） */
        LoadClass.loadBeans(packName);

        /* 加载JDBC模块 */
        if(baseInitJdbc != null){
            baseInitJdbc.init();
        }

        /* 创建bean对象 */
        LoadEasyBean.loadBean();
    }

    /**
     * 给单测注入属性
     */
    public static void autoWrite(Object obj) {
        try {
            Class cls = obj.getClass();
            /* 获取对象属性，完成注入 */
            Field[] fields = cls.getDeclaredFields();
            for(Field f : fields){
                Resource resource = f.getAnnotation(Resource.class);
                if(resource!=null){
                    f.setAccessible(true);
                    f.set(obj, BeanFactory.getBean(resource.value(),f.getType()));
                }
            }
        } catch (Exception e){
            log.error("初始化单测出现异常",e);
        }
    }
}
