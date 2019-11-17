package com.fulinlin.filter;

import com.alibaba.fastjson.JSON;
import com.fulinlin.jwt.JwtTokenUtil;
import com.fulinlin.properties.SecurityProperties;
import com.fulinlin.service.UserService;
import com.fulinlin.util.GenerateModelMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * JWT过滤器
 * <p>
 * OncePerRequestFilter，顾名思义，
 * 它能够确保在一次请求中只通过一次filter，而需要重复的执行。
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final String HEADER_NAME = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private UserService userService;


    private AntPathMatcher antPathMatcher = new AntPathMatcher();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("请求路径：{}，请求方式为：{}", request.getRequestURI(), request.getMethod());
        if (antPathMatcher.match("/favicon.ico", request.getRequestURI())) {
            log.info("jwt不拦截此路径：{}，请求方式为：{}", request.getRequestURI(), request.getMethod());
            filterChain.doFilter(request, response);
            return;
        }
        /*
         * get请求是否需要进行Authentication请求头校验，true：默认校验；false：不拦截GET请求
         * 因为get请求比较特殊
         */
        if (!securityProperties.getJwt().isPreventsGetMethod()) {
            if (Objects.equals(RequestMethod.GET.toString(), request.getMethod())) {
                log.info("jwt不拦截此路径因为开启了不拦截GET请求：{}，请求方式为：{}", request.getRequestURI(), request.getMethod());
                filterChain.doFilter(request, response);
                return;
            }
        }
        /*
         * 排除路径，并且如果是options请求是cors跨域预请求，设置allow对应头信息
         * permitUrls可以自定义不需要验证的url
         */
        String[] permitUrls = {"/authentication"};
        for (String permitUrl : permitUrls) {
            if (antPathMatcher.match(permitUrl, request.getRequestURI())
                    || Objects.equals(RequestMethod.OPTIONS.toString(), request.getMethod())) {
                log.info("jwt不拦截此路径：{}，请求方式为：{}", request.getRequestURI(), request.getMethod());
                filterChain.doFilter(request, response);
                return;
            }
        }
        // 获取请求头Authorization
        String authHeader = request.getHeader(HEADER_NAME);
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith(TOKEN_PREFIX)) {
            log.error("Authorization的开头不是Bearer，Authorization===>{}", authHeader);
            responseEntity(response, HttpStatus.UNAUTHORIZED.value(), "暂无权限！");
            return;
        }
        // 截取token
        String authToken = authHeader.substring(TOKEN_PREFIX.length());
        //判断token是否失效
        if (jwtTokenUtil.isTokenExpired(authToken)) {
            log.info("token已过期！");
            responseEntity(response, HttpStatus.UNAUTHORIZED.value(), "token已过期！");
            return;
        }
        String randomKey = jwtTokenUtil.getMd5KeyFromToken(authToken);
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        //如果访问的是刷新Token的请求
        if (antPathMatcher.match("/refreshToken", request.getRequestURI()) && Objects.equals(RequestMethod.POST.toString(), request.getMethod())) {
            final String getRandomKey = jwtTokenUtil.getRandomKey();
            refreshEntity(response, HttpStatus.OK.value(), jwtTokenUtil.generateToken(username, getRandomKey), jwtTokenUtil.refreshToken(authToken, jwtTokenUtil.getRandomKey()));
            return;
        }
        /*
         * 验证token是否合法
         */
        if (StringUtils.isBlank(username) || StringUtils.isBlank(randomKey)) {
            log.info("username{}或randomKey{} 可能为null！", username, randomKey);
            responseEntity(response, HttpStatus.UNAUTHORIZED.value(), "暂无权限！");
            return;
        }
        //获得用户名信息放入上下文中
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                    request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // token过期时间
        long tokenExpireTime = jwtTokenUtil.getExpirationDateFromToken(authToken).getTime();

        // token还剩余多少时间过期
        long surplusExpireTime = (tokenExpireTime - System.currentTimeMillis()) / 1000;
        log.info("Token剩余时间:" + surplusExpireTime);

        filterChain.doFilter(request, response);

    }

    private void responseEntity(HttpServletResponse response, Integer status, String message) {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        ModelMap modelMap = GenerateModelMap.generateMap(status, message);
        try {
            response.getWriter().write(JSON.toJSONString(modelMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshEntity(HttpServletResponse response, Integer status, String token, String refreshToken) {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        ModelMap modelMap = new ModelMap();
        modelMap.put("token", JwtAuthenticationTokenFilter.TOKEN_PREFIX + token);
        modelMap.put("refreshToken", JwtAuthenticationTokenFilter.TOKEN_PREFIX + refreshToken);
        try {
            response.getWriter().write(JSON.toJSONString(modelMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
