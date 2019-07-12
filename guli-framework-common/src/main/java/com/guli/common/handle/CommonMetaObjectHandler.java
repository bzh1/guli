package com.guli.common.handle;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 设置自动填充
 */

@Slf4j
@Component
public class CommonMetaObjectHandler implements MetaObjectHandler {
    public static void main(String[] args) {

    }

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.setFieldValByName("gmtCreate", new Date(), metaObject);
        this.setFieldValByName("gmtModified", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start modified fill ....");
        this.setFieldValByName("gmtModified", new Date(), metaObject);
    }
}
