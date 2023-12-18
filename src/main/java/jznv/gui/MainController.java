package jznv.gui;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import jznv.data.Data;
import jznv.data.DataBuilder;
import jznv.data.DataSet;
import jznv.data.DataType;
import lombok.Setter;

import java.util.List;

public class MainController {
    @Setter
    private DataBuilder dataBuilder;

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
    public void update() {
        DataSet data = dataBuilder.get(((ExtendedToggle) data_toggle.getSelectedToggle()).getType());
        Toggle selected_toggle = chart_toggle.getSelectedToggle();
        if (selected_toggle.equals(type_pie))
            plot_pie(data);
        else if (selected_toggle.equals(type_bar))
            plot_bar(data);
        else if (selected_toggle.equals(type_line))
            plot_line(data);
    }

    private void plot_pie(DataSet dataSet) {
        List<Data> data = dataSet.getData();
        GridPane pane = new GridPane();
        pane.getColumnConstraints().clear();
        pane.getRowConstraints().clear();
        int w = 0, h = 0;
        switch (data.size()) {
            case 1 -> w = h = 1;
            case 2 -> {
                w = 2;
                h = 1;
            }
            case 3, 4 -> w = h = 2;
            case 5, 6 -> {
                w = 3;
                h = 2;
            }
            case 7, 8, 9 -> w = h = 3;
        }
        for (int i = 0; i < h; i++) {
            RowConstraints e = new RowConstraints();
            e.setVgrow(javafx.scene.layout.Priority.ALWAYS);
            pane.getRowConstraints().add(e);
        }
        for (int i = 0; i < w; i++) {
            ColumnConstraints e = new ColumnConstraints();
            e.setHgrow(javafx.scene.layout.Priority.ALWAYS);
            pane.getColumnConstraints().add(e);
        }

        for (int i = 0; i < data.size(); i++) {
            Data d = data.get(i);
            chart_pane.getChildren().clear();
            PieChart chart = new PieChart(d.getAsPieData());
            chart.setTitle(d.getDataName());
            GridPane.setHalignment(chart, HPos.CENTER);
            GridPane.setValignment(chart, VPos.CENTER);
            pane.add(chart, i % w, i / w);
        }
        add_plot(pane);
    }

    private void plot_bar(DataSet data) {
        chart_pane.getChildren().clear();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        for (Data d : data.getData()) chart.getData().add(d.getAsBarData());
        chart.setTitle(data.getName());
        add_plot(chart);
    }

    private void plot_line(DataSet data) {
        chart_pane.getChildren().clear();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        for (Data d : data.getData()) chart.getData().add(d.getAsBarData());
        chart.setTitle(data.getName());
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