package edu.monash.smile.charting;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import edu.monash.smile.data.safeheartsModel.observation.QuantitativeObservation;

public class LineChartHelper {
    private LineChart lineChart;
    private ArrayList<LineDataSet> lineDataSets = new ArrayList<>();

    public LineChartHelper(LineChart lineChart){
        this.lineChart = lineChart;
    }

    private static void sortEntriesOnXValue(List<Entry> entries){
        Collections.sort(entries, (o1, o2) -> Math.round(o1.getX() - o2.getX()));
    }

    public void plot(){
        LineData lineData = new LineData(this.lineDataSets.toArray(new LineDataSet[lineDataSets.size()]));
        lineChart.setData(lineData);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(false);
        lineChart.invalidate();
    }

    public void createLineDataSet(
            List<QuantitativeObservation> quantitativeObservations,
            String label,
            int colour
    ){
        List<Entry> entries = new ArrayList<>();
        for (QuantitativeObservation quantitativeObservation : quantitativeObservations){
            BigDecimal value = quantitativeObservation.getValue();
            Date dateObserved = quantitativeObservation.getDateObserved();
            Entry entry = new Entry(dateObserved.getTime(), value.floatValue());
            entries.add(entry);
        }
        // Sort the entries based on timestamp
        sortEntriesOnXValue(entries);

        LineDataSet lineDataSet = new LineDataSet(entries, label);
        lineDataSet.setColor(colour);

        this.lineDataSets.add(lineDataSet);
    }
}
