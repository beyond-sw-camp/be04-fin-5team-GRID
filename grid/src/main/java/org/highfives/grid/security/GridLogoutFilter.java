package org.highfives.grid.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.highfives.grid.user.command.aggregate.RefreshToken;
import org.highfives.grid.user.command.repository.TokenReissueRepository;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Optional;

public class GridLogoutFilter extends GenericFilterBean {

    private final TokenReissueRepository tokenReissueRepository;
    private final JwtUtil jwtUtil;

    public GridLogoutFilter(TokenReissueRepository tokenReissueRepository, JwtUtil jwtUtil) {
        this.tokenReissueRepository = tokenReissueRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (getRefreshToken(request) == null) {
            filterChain.doFilter(request, response);
            return ;

        }

        //요청 경로가 logout이고, 요청 메소드가 POST인 경우 (true)가 아니면, 필터체인을 실행하지 않고 다음 로그아웃 로직 실행
        if(!checkPathMethod(request)){
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = getRefreshToken(request);

        if(!validationCheck(refreshToken, response)) return;
        if(!isRefresh(refreshToken, response)) return;
        if(!isTokenSaved(refreshToken, response)) return;
        proceedLogout(refreshToken, response);

    }

    private boolean checkPathMethod(HttpServletRequest request) {

        // 요청 경로가 logout 경로가 아니면 false 반환
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) return false;

        // 요청 메소드가 POST가 아니면 false 반환, POST라면 true 반환
        String requestMethod = request.getMethod();
        return requestMethod.equals("POST");
    }

    private String getRefreshToken(HttpServletRequest request) {

        String refreshToken = null;

        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refreshToken = cookie.getValue();

                }
            }
        }
        return refreshToken;
    }

    private boolean validationCheck(String refreshToken, HttpServletResponse response) {
        if(!jwtUtil.validateToken(refreshToken)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("Validation check failed");
            return false;   // 잘못된 토큰
        }
        return true;        // 정상적인 토큰
    }

    private boolean isRefresh(String refreshToken, HttpServletResponse response) {

        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("Not a refresh token");
            return false;
        }
        return true;
    }

    private boolean isTokenSaved(String refreshToken, HttpServletResponse response) {

        int userId = jwtUtil.getUserId(refreshToken);

        boolean isExist = tokenReissueRepository.existsById(userId);

        if (!isExist) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("Token not saved in repository");
            return false;
        }
        return isExist;
    }

    private void proceedLogout(String refreshToken, HttpServletResponse response) {

        int id = jwtUtil.getUserId(refreshToken);
        //refresh 토큰이 있는지 조회
        Optional<RefreshToken> findToken = tokenReissueRepository.findById(id);
        System.out.println("id = " + id);
        System.out.println("findToken = " + findToken);

        if(findToken.isPresent()){
            tokenReissueRepository.deleteById(findToken.get().getId());
        }
        else System.out.println("empty");

        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setDomain("gridhr.site");
        
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
