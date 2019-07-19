package com.adv.config.controller;

import com.adv.config.former.Response;
import com.adv.config.util.RASUtil;
import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



/**
 * @date ：Created in 7/11/19 11:42 AM
 * @description：encryption config  json
 */
@Slf4j
@RestController
@Api(description = "Get the key pair for Non-pair encryption api")
public class EncryptionInfo {
    @ApiOperation(value="Generate a pair of public and private keys" ,notes = "Generate a pair of public and private keys for encryption and decryption")
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
