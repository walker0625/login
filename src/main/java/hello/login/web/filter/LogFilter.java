package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("start log");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("dofilter log");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString(); // 같은 요청에 단일 식별자를 추가하고 싶으면 logback.mdc 내용 검색하여 추가

        try{
            log.info("request[{}] [{}]", uuid, requestURI);
            chain.doFilter(request, response); // 최종 호출 이 부분이 없으면 그대로 멈춤
        }catch (Exception e) {
            throw e;
        } finally {
            log.info("response [{}] [{}]", uuid, requestURI);
        }

    }

    @Override
    public void destroy() {
        log.info("destroy log");
    }

}
