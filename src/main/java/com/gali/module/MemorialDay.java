package com.gali.module;

import com.gali.CommonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 纪念日
 *
 * @author 颜伟凡
 * @version 2022-8-8
 */
@Component
public class MemorialDay {
    private final static Logger logger = LoggerFactory.getLogger(MemorialDay.class);

    @Autowired
    private CommonConfig config;

    /**
     * 获取纪念日
     */

}
