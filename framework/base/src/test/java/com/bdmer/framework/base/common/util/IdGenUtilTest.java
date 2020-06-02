package com.bdmer.framework.base.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class IdGenUtilTest {
    @Test
    public void getId() {
        log.info("id:" + IdGenUtil.getId());
    }
}