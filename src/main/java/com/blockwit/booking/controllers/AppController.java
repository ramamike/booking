package com.blockwit.booking.controllers;

import com.blockwit.booking.entity.User;
import com.blockwit.booking.exceptions.RoleNotFoundException;
import com.blockwit.booking.model.Error;
import com.blockwit.booking.model.NewAccount;
import com.blockwit.booking.service.UserService;
import com.blockwit.booking.validator.NewAccountValidator;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Controller
@Slf4j
@AllArgsConstructor
public class AppController {

    private NewAccountValidator newAccountValidator;

    private UserService userService;

    @GetMapping
    public String home() {
        return "front/home";
    }

    @GetMapping("app/login")
    public String login() {
        return "front/login";
    }

    @GetMapping("app/accounts/create")
    public ModelAndView createAccountGet() {
        return new ModelAndView("front/account-new", Map.of("newAccount", new NewAccount()));
    }

    @PostMapping("app/accounts/create")
    public ModelAndView createAccountPOST(
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("newAccount") @Valid NewAccount newAccount,
            BindingResult bindingResult
    ) {
        log.info("Perform new account form checks");

        newAccountValidator.validate(newAccount, bindingResult);
        if (bindingResult.hasErrors())
            return new ModelAndView("front/account-new",
                    bindingResult.getModel(), HttpStatus.BAD_REQUEST);

        log.info("Create account");
        Either<Error, User> accountEither = userService.createAccount(
                newAccount.getLogin(),
                newAccount.getEmail(),
                newAccount.getPassword());

        if (accountEither.isLeft()) {
            redirectAttributes.addFlashAttribute("message_error",
                    accountEither.getLeft().getDescr());
        } else {
            redirectAttributes.addFlashAttribute("message_success",
                    newAccount.getLogin() + " создан!");
        }

        return new ModelAndView("redirect:/app/accounts/create");
    }

}