package application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class MainController {

    private boolean isUpdating = false;
    private DBController database = new DBController();
    private ArrayList<Station> stations = new ArrayList<>();
    private Station selectedStation = null;

    @FXML
    private TextField number;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField departure;

    @FXML
    private TextField arrival;

    @FXML
    private TextField free;

    @FXML
    private TextField destiny;

    @FXML
    public ComboBox<String> genderComboBox = new ComboBox<>();
    @FXML
    public Button addButton;

    @FXML
    public Button updateButton;

    @FXML
    public Button deleteButton;

    @FXML
    public TableView<Station> employeeTableView = new TableView<>();

    @FXML
    public TableColumn<Station, Integer> IDColumn;

    @FXML
    public TableColumn<Station, String> firstNameColumn;

    @FXML
    public TableColumn<Station, String> surnameColumn;

    @FXML
    public TableColumn<Station, String> DOBColumn;

    @FXML
    public TableColumn<Station, String> genderColumn;

    @FXML
    public TableColumn<Station, Integer> ssnColumn;

    @FXML
    public TableColumn<Station, Integer> salaryColumn;

    @FXML
    protected void initialize() {
        CompletableFuture.runAsync(() -> {
            try {
                database.run();
                stations = database.getEmployees();
                mapToTable(stations);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void clickItem(MouseEvent event) {
        if (event.getClickCount() == 1) selectedStation = employeeTableView.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void updateEmployee() {
        if (selectedStation == null) {
            alertHelper("WARNING", "ERROR", "Please double click an employee from the table first!");
        } else {
            number.setText(Integer.toString(selectedStation.getNumber()));
            destiny.setText(selectedStation.getDestiny());
            departure.setText(selectedStation.getDepartureTime());
            arrival.setText(selectedStation.getArrivalTime());
            free.setText(Integer.toString(selectedStation.getAmount()));
            isUpdating = true;
            addButton.setText("CONFIRM");
        }
    }

    @FXML
    public void deleteEmployee() {
        if (selectedStation == null) {
            alertHelper("WARNING", "ERROR", "Please double click an employee from the table first!");
        } else {


            try {
                database.deleteEmployee(selectedStation.getID());
                stations = database.getEmployees();
                mapToTable(stations);
                selectedStation = null; // reset selected employee

            } catch (SQLException e) {
                alertHelper("WARNING", "DELETING ERROR", e.getMessage());
            }
        }
    }

    @FXML
    public void addEmployee() {

        if (checkValidation()) {
            alertHelper("WARNING", "MISSING FIELDS", "Please fill out all fields!");
        } else {
            int ID = stations.size() + 1; // assign Employee with new ID


            try {
                Station station = new Station(ID, Integer.parseInt(number.getText()),
                        destiny.getText(),
                        departure.getText(),
                        arrival.getText(),
                        Integer.parseInt(free.getText()));

                if (isUpdating) {
                    station.setID(selectedStation.getID()); // keep the ID of selected employee
                    database.updateEmployee(station);
                    selectedStation = null; // reset selected employee
                    isUpdating = false; // reset updating flag
                } else {
                    database.addEmployee(station);
                }

                try {
                    stations = database.getEmployees();
                    mapToTable(stations);
                    clearTextFields();
                    addButton.setText("ADD");

                } catch (SQLException e) {
                    alertHelper("ERROR", "ADDING EMPLOYEE ERROR", e.getMessage());
                }

            } catch (NumberFormatException | SQLException e) {
                alertHelper("ERROR", "ERROR", e.getMessage());
            }
        }
    }

    @FXML
    private void searchEmployee() {
        String surname = searchTextField.getText();

        try {
            ArrayList<Station> matchedStations = database.findEmployee(surname);
            mapToTable(matchedStations);

        } catch (SQLException e) {
            alertHelper("ERROR", "SEARCH EMPLOYEE ERROR", e.getMessage());
        }
    }

    private void mapToTable(ArrayList<Station> stations) {
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("Destiny"));
        DOBColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        ssnColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        employeeTableView.setItems(FXCollections.observableArrayList(stations));
        System.out.println(stations);
    }

    private void alertHelper(String type, String title, String message) {
        Alert alert;
        switch (type.toUpperCase()) {
            case "WARNING":
                alert = new Alert(Alert.AlertType.WARNING);
                break;
            case "ERROR":
                alert = new Alert(Alert.AlertType.ERROR);
                break;
            case "CONFIRMATION":
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                break;
            default:
                alert = new Alert(Alert.AlertType.NONE);
        }

        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearTextFields() {
        number.clear();
        destiny.clear();
        departure.clear();
        arrival.clear();
        free.clear();
    }

    private boolean checkValidation() {
        boolean isError;
        isError = Arrays.asList(
                number.getText(),
                destiny.getText(),
                departure.getText(),
                arrival.getText(),
                free.getText()).contains("");
        return isError;
    }
}
