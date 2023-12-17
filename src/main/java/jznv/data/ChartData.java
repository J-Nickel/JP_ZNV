package jznv.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChartData {
    private String title;
    private List<String> names;
    private List<Double> values;

    public ChartData(String title) {
        names = new ArrayList<>();
        values = new ArrayList<>();
    }

    public void add(String name, Double data) {
        names.add(name);
        values.add(data);
    }

    public ObservableList<PieChart.Data> getAsPieData() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        for (int i = 0; i < names.size(); i++)
            data.add(new PieChart.Data(names.get(i), values.get(i)));
        return data;
    }

    public XYChart.Series<String, Number> getAsBarData() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < names.size(); i++)
            series.getData().add(new XYChart.Data<>(names.get(i), values.get(i)));
        return series;
    }
}