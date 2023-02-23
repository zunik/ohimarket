package zunik.ohimarket.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import zunik.ohimarket.constant.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor {
    /**
     * 로그인이 되어있지 않으면, 로그인 페이지로 보냅니다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            response.sendRedirect("/signIn");
            return false;
        }

        return true;
    }
}
