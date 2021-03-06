package com.mars.mvc.load;

import com.mars.core.annotation.MarsInterceptor;
import com.mars.core.constant.MarsConstant;
import com.mars.core.constant.MarsSpace;
import com.mars.core.logger.MarsLogger;
import com.mars.mvc.model.MarsInterModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 加载拦截器
 */
public class LoadInters {

    private static MarsLogger log = MarsLogger.getLogger(LoadController.class);

    /**
     * 获取全局存储空间
     */
    private static MarsSpace constants = MarsSpace.getEasySpace();

    /**
     * 创建所有拦截器对象
     */
    public static void loadIntersList(){
        try {
            List<MarsInterModel> list = new ArrayList<>();

            Object objs = constants.getAttr(MarsConstant.INTERCEPTORS);

            if(objs != null) {
                List<Map<String,Object>> interceptors = (List<Map<String,Object>>)objs;

                for(Map<String,Object> map : interceptors) {

                    MarsInterceptor marsInterceptor = (MarsInterceptor)map.get("annotation");
                    String pattern = marsInterceptor.pattern();
                    Class cls = (Class)map.get("className");

                    MarsInterModel marsInterModel = new MarsInterModel();
                    marsInterModel.setCls(cls);
                    marsInterModel.setObj(cls.getDeclaredConstructor().newInstance());
                    marsInterModel.setPattern(pattern);
                    list.add(marsInterModel);
                }
                constants.setAttr(MarsConstant.INTERCEPTOR_OBJECTS,list);
            }
        } catch (Exception e) {
            log.error("读取拦截器报错",e);
        }
    }
}
