package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //@GetMapping("/")
    public String home() {
        return "home";
    }

    //@GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
        if(memberId == null) {
            return "home";
        }

        Member member = memberRepository.findById(memberId);
        if(member == null) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLogin2(HttpServletRequest request, Model model) {
        Member member = (Member)sessionManager.getSession(request);

        if(member == null) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLogin3(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false); // 처음 들어온 사용자는 session을 생성할 필요가 없으므로 false > 메모리 관리차원

        if(session == null) {
            return "home";
        }

        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        if(member == null) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLogin3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member, // session 체크 및 반환까지 처리(새션을 생성하지는 않음)
            Model model) {
        
        if(member == null) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

}