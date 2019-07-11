package com.adv.config.configuration;

import com.adv.config.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitRun implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String result = EncryptUtil.encrypt("adminadmin");
        System.out.println(result);
        System.out.println(EncryptUtil.decrypt(result));
    }
}
