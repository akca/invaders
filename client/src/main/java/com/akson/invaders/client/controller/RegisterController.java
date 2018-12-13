package com.akson.invaders.client.controller;

import com.akson.invaders.client.AbstractScreenController;
import com.akson.invaders.client.InvadersApplication;
import com.akson.invaders.client.ScreenEnum;
import com.akson.invaders.common.entity.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Screen controller for register screen.
 */
@Component
public class RegisterController extends AbstractScreenController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @FXML
    public Button registerButton;

    @FXML
    public TextField regUserNameField;

    @FXML
    public PasswordField regPasswordField;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void onDisplay() {

    }

    @Override
    public void onExit() {

    }

    @FXML
    private void goToLogin(ActionEvent event) {
        screenManager.setScreen(ScreenEnum.LOGIN);
    }

    @FXML
    private void doRegister(ActionEvent event) {

        registerButton.setDisable(true);

        new Thread(() -> {
            restTemplate.getInterceptors().clear();

            try {

                User user = new User();

                user.setUsername(regUserNameField.getText());
                user.setPassword(regPasswordField.getText());

                HttpEntity<User> request = new HttpEntity<>(user);

                User savedUser = restTemplate.postForObject(InvadersApplication.serverURL + "/register", request, User.class);

                if (savedUser != null) {
                    LoginController.currentUser = savedUser;
                    logger.info("Register successful!");
                    restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(),
                            user.getPassword()));

                    Platform.runLater(() -> screenManager.setScreen(ScreenEnum.MAIN));
                }

            } catch (RestClientException e) {
                logger.info("Cannot register with this credentials!");
                registerButton.setDisable(false);
            }
        }).start();

    }

}
