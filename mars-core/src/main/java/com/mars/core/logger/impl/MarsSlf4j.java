package com.mars.core.logger.impl;


import com.mars.core.logger.MarsLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarsSlf4j extends MarsLogger {

    private Logger logger;

    public MarsSlf4j(Class cls){
        logger = LoggerFactory.getLogger(cls);
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
    public void warn(String info,Throwable e) {
        logger.warn(info,e);
    }
}
