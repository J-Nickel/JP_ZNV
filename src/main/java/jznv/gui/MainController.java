package jznv.gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jznv.data.ChartData;
import jznv.data.ChartDataBuilder;
import jznv.data.DataType;
import lombok.Setter;

public class MainController {
    @Setter
    private ChartDataBuilder dataBuilder;

    @FXML
    private ToggleButton type_pie;
    @FXML
    private ToggleButton type_bar;
    @FXML
    private ToggleButton type_line;
    @FXML
    private AnchorPane chart_pane;
    @FXML
    private VBox togglePane_type;

    private ToggleGroup data_toggle;
    private ToggleGroup chart_toggle;

    @FXML
    void initialize() {
        data_toggle = new ToggleGroup();
        for (DataType type : DataType.values()) {
            ExtendedToggle et = new ExtendedToggle(type);
            et.getStyleClass().add("flat-btn");
            et.setPrefWidth(200);
            et.setPrefHeight(30);
            et.setOnAction(event -> update());
            togglePane_type.getChildren().add(et);
            data_toggle.getToggles().add(et);
        }
        data_toggle.selectToggle(data_toggle.getToggles().get(0));

        chart_toggle = new ToggleGroup();
        chart_toggle.getToggles().addAll(
                type_pie,
                type_bar,
                type_line
        );
        chart_toggle.selectToggle(type_pie);
    }

    @FXML
    private void update() {
        ChartData data = dataBuilder.build(((ExtendedToggle) data_toggle.getSelectedToggle()).getType());
        Toggle selected_toggle = chart_toggle.getSelectedToggle();
        if (selected_toggle.equals(type_pie))
            plot_pie(data);
        else if (selected_toggle.equals(type_bar))
            plot_bar(data);
        else if (selected_toggle.equals(type_line))
            plot_line(data);
    }

    private void plot_pie(ChartData data) {
        chart_pane.getChildren().clear();
        PieChart chart = new PieChart(data.getAsPieData());
        chart.setTitle(data.getTitle());
        add_plot(chart);
    }

    private void plot_bar(ChartData data) {
        chart_pane.getChildren().clear();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.getData().add(data.getAsBarData());
        chart.setTitle(data.getTitle());
        add_plot(chart);
    }

    private void plot_line(ChartData data) {
        chart_pane.getChildren().clear();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.getData().add(data.getAsBarData());
        chart.setTitle(data.getTitle());
        add_plot(chart);
    }

    private void add_plot(Node node) {
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        chart_pane.getChildren().add(node);
    }
}
