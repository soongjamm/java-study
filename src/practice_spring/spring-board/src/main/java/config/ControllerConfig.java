package config;

import controllers.user.RegisterController;
import domain.index.IndexController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import service.user.RegisterUserService;

@Configuration
public class ControllerConfig {

    @Autowired
    RegisterUserService registerUserService;

    @Bean
    public IndexController boardController() {
        return new IndexController();
    }

    @Bean
    public RegisterController registerController() {
        RegisterController registerController = new RegisterController();
        registerController.setUserService(registerUserService);
        return registerController;
    }

}