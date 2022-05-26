/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.booking.validator;

import com.blockwit.booking.model.NewAccount;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class NewAccountValidator extends CommonValidator {

    public NewAccountValidator(javax.validation.Validator javaxValidator) {
        super(javaxValidator, NewAccount.class);
    }

    @Override
    public void performValidate(Object o, Errors errors) {
        NewAccount newAccount = (NewAccount) o;

        if (!newAccount.getPassword().equals(newAccount.getRepassword()))
            errors.rejectValue("repassword", "Passwords not equals", "Passwords not equals");
    }

}
