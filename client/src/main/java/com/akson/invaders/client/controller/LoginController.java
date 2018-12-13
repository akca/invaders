package com.akson.invaders.client.controller;

import com.akson.invaders.client.AbstractScreenController;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Screen controller for login screen.
 */
@Component
public class LoginController extends AbstractScreenController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    public static User currentUser = null;

    @FXML
    public TextField userNameField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public Button loginButton;

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
    private void goToRegister(ActionEvent event) {
        screenManager.setScreen(ScreenEnum.REGISTER);
    }

    @FXML
    public void doLogin(ActionEvent actionEvent) {

        loginButton.setDisable(true);

        new Thread(() -> {
            restTemplate.getInterceptors().clear();
            restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userNameField.getText(),
                    passwordField.getText()));

            ResponseEntity<User> response1 = null;
            try {
                response1 = restTemplate.getForEntity("http://localhost:8080/user", User.class);

                if (response1.getStatusCode() == HttpStatus.OK) {
                    currentUser = response1.getBody();
                    logger.info("Login successful!");
                    Platform.runLater(() -> screenManager.setScreen(ScreenEnum.MAIN));
                }

            } catch (RestClientException e) {
                logger.info("Cannot login with this credentials!");
                loginButton.setDisable(false);
            }
        }).start();

    }
}
