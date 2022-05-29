package com.blockwit.booking.configuration.initializer;

import com.blockwit.booking.configuration.initializer.model.RoleDataInitializer;
import com.blockwit.booking.configuration.initializer.model.UserDataInitializer;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RunAfterStartup {

    private RoleDataInitializer roleDataInitializer;
    private UserDataInitializer userDataInitializer;

    @EventListener(ApplicationReadyEvent.class)
        public void runAfterStartup(){
        roleDataInitializer.initRoles();
        userDataInitializer.initAdmin();
    }

}
