package com.ownwear.app.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter1 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        /*
        * 토큰 : cos 이걸 만들어줘야함, id,pw 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 그걸 응답을 해준다.
        * 요청할 때 마다 header에 Authorization에 value값으로 토큰을 가지고 온다.
        * 그때 토큰을 확인하여 내가 만든 토큰인지에 대한 검증하면됨(RSA,HS256)
        *
        * */


        System.out.println(req.getMethod());
        if (req.getMethod().equals("POST")) {
            System.out.println("포스트 요청됨");
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);
            if (headerAuth.equals("cos")) {
                System.out.println("일치함");
                chain.doFilter(req, res);
            } else {
                System.out.println("안 일치함");
                PrintWriter outPrintWriter = res.getWriter();
                outPrintWriter.println("인증안됨");
                chain.doFilter(req, res);
            }
        }
        System.out.println("필터1");

    }
}
