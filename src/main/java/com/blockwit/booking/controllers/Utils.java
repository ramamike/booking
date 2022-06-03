package com.blockwit.booking.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

public class Utils {

	public static final String REFERER = "Referer";

	public static String getRefererURL(HttpServletRequest request) {
		String referer = request.getHeader(REFERER);
		if (referer == null || referer.isEmpty())
			return "/";
		return referer;
	}

	public static String getUsernameFromSecurityContext() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}

	public static String getRoleFromSecurityContext(){
		return null;
	}

}
