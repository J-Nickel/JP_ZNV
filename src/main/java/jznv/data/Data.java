package jznv.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Data {
    private final String dataName;
    private final List<String> categories = new ArrayList<>();
    private final List<Double> values = new ArrayList<>();

    public Data(String dataName) {
        this.dataName = dataName;
    }

    public void add(String cat, Long val) {
        add(cat, Double.valueOf(val));
    }

    public void add(String cat, Double val) {
        categories.add(cat);
        values.add(val);
    }


    public ObservableList<PieChart.Data> getAsPieData() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        for (int i = 0; i < categories.size(); i++)
            data.add(new PieChart.Data(categories.get(i), values.get(i)));
        return data;
    }

    public XYChart.Series<String, Number> getAsBarData() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(dataName);
        for (int i = 0; i < categories.size(); i++)
            series.getData().add(new XYChart.Data<>(categories.get(i), values.get(i)));
        return series;
    }

    public void sortCatAsDouble() {
        for (int i = 0; i < categories.size() - 1; i++)
            for (int j = i; j < categories.size() - 1; j++)
                if (Double.parseDouble(categories.get(j)) > Double.parseDouble(categories.get(j + 1)))
                    swap(j, j + 1, categories, values);
    }

    private void swap(int i, int j, List<?>... lists) {
        for (List<?> list : lists) swap(list, i, j);
    }

    private <T> void swap(List<T> a, int i, int j) {
        T b = a.get(i);
        a.set(i, a.get(j));
        a.set(j, b);
    }
}