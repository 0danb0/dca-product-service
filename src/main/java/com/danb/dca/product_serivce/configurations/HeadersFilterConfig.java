package com.danb.dca.product_serivce.configurations;

import com.danb.dca.product_serivce.properties.SecurityProperties;
import com.danb.dca.product_serivce.utils.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class HeadersFilterConfig extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String appKey = request.getHeader(Constants.HEADER_APP_KEY_NAME.getValue());

        if (appKey == null || !securityProperties.getLicensedApps().contains(appKey)) {
            log.error("Internal filter - Header retrieved is invalid -> {}", appKey);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        log.debug("Internal filter - Header retrieved from request -> {}", appKey);
        filterChain.doFilter(request, response);
    }
}
