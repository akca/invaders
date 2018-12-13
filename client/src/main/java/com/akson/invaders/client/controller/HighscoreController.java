package com.akson.invaders.client.controller;

import com.akson.invaders.client.AbstractScreenController;
import com.akson.invaders.client.ScreenEnum;
import com.akson.invaders.client.util.HighScoreModel;
import com.akson.invaders.common.entity.Score;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

@Component
public class HighscoreController extends AbstractScreenController {

    @FXML
    public TableView<HighScoreModel> allTimeTableView;

    @FXML
    public TableView<HighScoreModel> monthlyTableView;

    @FXML
    public TableView<HighScoreModel> weeklyTableView;

    @FXML
    public TableView<HighScoreModel> dailyTableView;

    public TableColumn<HighScoreModel, String> dailyTableUsername;
    public TableColumn<HighScoreModel, Long> dailyTableScore;

    public TableColumn<HighScoreModel, String> weeklyTableUsername;
    public TableColumn<HighScoreModel, Long> weeklyTableScore;

    public TableColumn<HighScoreModel, String> monthlyTableUsername;
    public TableColumn<HighScoreModel, Long> monthlyTableScore;

    public TableColumn<HighScoreModel, String> allTimeTableUsername;
    public TableColumn<HighScoreModel, Long> allTimeTableScore;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void onDisplay() {

        // Do queries in other thread to prevent UI hang.
        new Thread(() -> {

            ResponseEntity<PagedResources<Score>> response;

            response = restTemplate.exchange("http://localhost:8080/score/all",
                    HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Score>>() {
                    });
            try {

                allTimeTableView.setItems(mapDataToTableModel(response.getBody().getContent()));
            } catch (NullPointerException ignore) {

            }

            response = restTemplate.exchange("http://localhost:8080/score/day",
                    HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Score>>() {
                    });
            try {
                dailyTableView.setItems(mapDataToTableModel(response.getBody().getContent()));
            } catch (NullPointerException ignore) {

            }

            response = restTemplate.exchange("http://localhost:8080/score/week",
                    HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Score>>() {
                    });
            try {
                weeklyTableView.setItems(mapDataToTableModel(response.getBody().getContent()));
            } catch (NullPointerException ignore) {

            }

            response = restTemplate.exchange("http://localhost:8080/score/month",
                    HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Score>>() {
                    });
            try {
                monthlyTableView.setItems(mapDataToTableModel(response.getBody().getContent()));
            } catch (NullPointerException ignore) {

            }
        }).start();
    }

    @Override
    public void onExit() {

    }

    /**
     * Sets CellValueFactory for each cell.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allTimeTableUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        allTimeTableScore.setCellValueFactory(new PropertyValueFactory<>("score"));

        monthlyTableUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        monthlyTableScore.setCellValueFactory(new PropertyValueFactory<>("score"));

        weeklyTableUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        weeklyTableScore.setCellValueFactory(new PropertyValueFactory<>("score"));

        dailyTableUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        dailyTableScore.setCellValueFactory(new PropertyValueFactory<>("score"));
    }

    @FXML
    private void goToMainScreen(ActionEvent event) {
        screenManager.setScreen(ScreenEnum.MAIN);
    }


    /**
     * Converts Collection<Score> to ObservableList<HighScoreModel> to be displayed in a JavaFX TableView.
     *
     * @param scores Collection of Score objects
     * @return list of HighScoreModel
     */
    private ObservableList<HighScoreModel> mapDataToTableModel(Collection<Score> scores) {
        ObservableList<HighScoreModel> result = FXCollections.observableArrayList();

        for (Score score : scores) {
            HighScoreModel model = new HighScoreModel(score.getUser().getUsername(), score.getScore());
            result.add(model);
        }

        return result;
    }
}
