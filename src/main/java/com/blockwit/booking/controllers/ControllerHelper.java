package com.blockwit.booking.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class ControllerHelper {

    public static boolean checkEditingPermission() {
        return false;
    }

    public static ModelAndView returnSuccess(RedirectAttributes redirectAttributes, String url, String msg) {
        redirectAttributes.addFlashAttribute("message_success", msg);
        return new ModelAndView(url);
    }

    public static ModelAndView returnError(RedirectAttributes redirectAttributes, String url, String msg) {
        redirectAttributes.addFlashAttribute("message_error", msg);
        return new ModelAndView(url);
    }

}
