package com.adv.config.controller;

import com.adv.config.entity.ConfigurationData;
import com.adv.config.former.Response;
import com.adv.config.util.RASUtil;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



/**
 * @date ：Created in 7/11/19 11:42 AM
 * @description：encryption config  json
 */
@Slf4j
@RestController
@Api(description = "Encryption of configuration information api")
public class ConfigEncryption {
    private String name = "WP.SRP";
    @ApiOperation(value="Encrypt configuration information" ,notes = "Encrypt related configuration information")
    @RequestMapping(value = "/creatQRCodeJson", method = RequestMethod.POST)
    public ResponseEntity<String> encrypt(@RequestBody ConfigurationData configData){
        log.info(configData.getConfigurations().toString());
        String publicKey = configData.getPublickey();
        String configJson = configData.getConfigurations().toString();
        if(publicKey == null || configJson == null){
            return new ResponseEntity(Response.error("Data format error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String result = RASUtil.encrypt(configJson, publicKey);

        JSONObject json = new JSONObject();
        json.put("id", name);
        json.put("data", result);
        if(result != null){
            return new ResponseEntity(Response.success(json), HttpStatus.OK);
        }else{
            return new ResponseEntity(Response.error("Server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
