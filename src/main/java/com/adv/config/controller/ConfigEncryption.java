package com.adv.config.controller;

import com.adv.config.former.Response;
import com.adv.config.util.RASUtil;
import com.alibaba.fastjson.JSONObject;

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
public class ConfigEncryption {

    @RequestMapping(value = "/creatQRCodeJson", method = RequestMethod.POST)
    public ResponseEntity<String> encrypt(@RequestBody JSONObject configData){
        log.info(configData.toString());
        String publicKey = configData.getString("publickey");
        String configJson = configData.getJSONObject("configurations").toString();
        if(publicKey == null || configJson == null){
            return new ResponseEntity(Response.error("Data format error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String result = RASUtil.encrypt(configJson, publicKey);
        if(result != null){
            return new ResponseEntity(Response.success(result), HttpStatus.OK);
        }else{
            return new ResponseEntity(Response.error("Server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/getKeyPair", method = RequestMethod.GET)
    public ResponseEntity<String>getKeyPair(){
        JSONObject keyPairJsonObject = RASUtil.getKeyPair();
        if(keyPairJsonObject != null){
            return new ResponseEntity(Response.success(keyPairJsonObject.toString()), HttpStatus.OK);
        }else{
            return new ResponseEntity(Response.error("Server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
