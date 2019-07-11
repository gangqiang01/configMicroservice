package com.adv.config.controller;

import com.adv.config.former.Response;
import com.adv.config.util.EncryptUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * @date ：Created in 7/11/19 11:42 AM
 * @description：encryption config  json
 */

@RestController
public class Encryption {

    @RequestMapping(value = "/creatQRCodeJson", method = RequestMethod.POST)
    public ResponseEntity<String> encrypt(@RequestBody ObjectNode body){
        String value = body.toString();
        String result = EncryptUtil.encrypt(value);
        return new ResponseEntity(Response.success(result), HttpStatus.OK);
    }
}
