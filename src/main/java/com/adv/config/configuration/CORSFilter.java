package com.adv.config.configuration;

import com.adv.config.former.Response;
import com.adv.config.util.AESUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.IOException;

//@Component
@WebFilter(filterName = "CORSFilter")
@Slf4j
public class CORSFilter implements Filter {
    public static final String SRPTOEKN = "X-Auth-SRPToken";
    public static final String SRPNAME = "secrecyConfig";
    private static final String SEPARATOR = "#";
    private static final long TIMEOUT = 60*3;//３min

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        log.info("enter");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type, accesstoken, X-Auth-SRPToken,timeout");
        if(request.getRequestURI().equals("/getKeyPair")){
            chain.doFilter(req, res);
            return;
        }else{
            String srpToken = request.getHeader(SRPTOEKN);
            if(srpToken != null && isSrpToken(srpToken)){
                chain.doFilter(req, res);
                return;
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().format(JSONObject.toJSONString(Response.error("illegal user"))).flush();
        return;
    }

    private boolean isSrpToken(String srpToken){
        try{
            String result = AESUtil.Decrypt(srpToken);
            log.info("result:"+result);
            long nowTime = System.currentTimeMillis()/1000;
            long otime = Long.valueOf(result.split(SEPARATOR)[0]).longValue();
            String srpname = result.split("#")[1];
            log.info("timeout:"+String.valueOf(nowTime-otime));
            if((nowTime-otime) < TIMEOUT && srpname.equals(SRPNAME)){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void init(FilterConfig filterConfig) {}

    public void destroy() {}
}
