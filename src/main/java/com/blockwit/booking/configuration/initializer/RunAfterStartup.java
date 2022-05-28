package com.blockwit.booking.configuration.initializer;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RunAfterStartup {

    private RoleDataInitializer roleDataInitializer;

    @EventListener(ApplicationReadyEvent.class)
        public void runAfterStartup(){
        System.out.println("run after startup");
        roleDataInitializer.initRoles();
    }

}
