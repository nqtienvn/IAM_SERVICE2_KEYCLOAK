package com.tien.iam_service2_keycloak.config;


import com.tien.iam_service2_keycloak.service.JwtService;
import com.tien.iam_service2_keycloak.service.impl.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
//OncePerRequestFilter cho biết là bọo lọc này chỉ được thực hiẹn 1 lần
//tránh để ta xác thực, kiểm trả JWT nhiều lần trươc khi vào Filter khác, servlet
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //logic authentication
    JwtService jwtService;
    //lớp implements customerUserDetail service để triển khai UserDetailsService
    CustomUserDetailService customerUserDetailService;

    //đây là lớp filter
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        //lấy cái naỳ trên header của requets xem có gửi đi kèm cái gì đó không
        String authHeader = request.getHeader("Authorization");
        //tạo một jwt
        //BƯỚC TIẾP THEO CÓ 2 NHÁNH
        //VERYGY THÌ CẦN CÓ THÔNG TIN VỀ TOKEN
        String jwt;
        //LẤY USER DETAIL THÌ CẦN CÓ THÔNG TIN VỀ USER EMAIL ĐỂ LOAD TỪ DB LÊN
        String userEmail;

        //kiểm tra xem cái ấy nó phải là token không
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            //nêu k phải chuyển sang cái khác trong chuỗi filter chain hoặc là controller
            filterChain.doFilter(request, response);//k có xác thực thì 403

            return;
        }
        //nếu phải thì gián jwt này
        jwt = authHeader.substring(7);
        //mình sẽ lấy nó ra từ thăng jwt
        userEmail = jwtService.extractEmail(jwt);
        if (userEmail != null //nó có token
                && SecurityContextHolder
                .getContext()
                .getAuthentication() == null) {//nó chưa qua xác thực
            //can custom lai thi hay hon
            //lay ve user detail tu email
            UserDetails userDetails = customerUserDetailService.loadUserByUsername(userEmail);
            //validate token
            if (jwtService.validateToken(jwt, userDetails)) { //nếu mà verify được thì....

                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities()); //tạo token , k cần pass vì đây chỉ xác thực

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //thêm những thứ còn lại ở request
                //Gắn thêm chi tiết từ request (ví dụ: IP, session ID, User-Agent...).
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info(SecurityContextHolder.getContext().getAuthentication().getName());
            }
        }
        //nếu mà nó chưa có token thì sẽ 403 hoạc đã xác thực thì chuyển qua controller luôn
        filterChain.doFilter(request, response);
    }
}
