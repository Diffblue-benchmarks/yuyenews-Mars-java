package com.mars.core.logger.impl;


import com.mars.core.logger.MarsLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MarsLog4j extends MarsLogger {

    private Logger logger;

    public MarsLog4j(Class cls){
        logger = LogManager.getLogger(cls);
    }

    @Override
    public void info(String info) {
        logger.info(info);
    }

    @Override
    public void error(String info, Throwable e) {
        logger.error(info,e);
    }

    @Override
    public void error(String info) {
        logger.error(info);
    }

    @Override
    public void warn(String info) {
        logger.warn(info);
    }

    @Override
    public void warn(String info, Throwable e) {
        logger.warn(info,e);
    }

}
