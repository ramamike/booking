package com.blockwit.booking.controllers;

import javax.servlet.http.HttpServletRequest;

public class Utils {

	public static final String REFERER = "Referer";

	public static String getRefererURL(HttpServletRequest request) {
		String referer = request.getHeader(REFERER);
		if (referer == null || referer.isEmpty())
			return "/";
		return referer;
	}

}
