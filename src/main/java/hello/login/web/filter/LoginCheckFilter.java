package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whiteList = {"/", "/members/add", "/login", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        try{
            log.info("인증 시작 request URI {}", requestURI);
            if(isLoginCheckPath(requestURI)) {

                log.info("인증 체크 로직 시작 request URI {}", requestURI);
                HttpSession session = httpServletRequest.getSession(false);

                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청 {}", requestURI);
                    httpServletResponse.sendRedirect("/login?redirectURL=" + requestURI); // 로그인시에 실패한 페이지로 갈 수 있도록 추가
                    return; // 필터가 더 이상 동작하지 않음(Controller로 가지 않음)
                }
                
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e; // 예외를 was에 던짐짐
        } finally {
            log.info("인증 필터 종료 {}", requestURI);
        }

    }

    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }

}
