package edu.monash.smile.charting;

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

public class ObservationLineChart {
    private com.github.mikephil.charting.charts.LineChart lineChart;
    private ArrayList<LineDataSet> lineDataSets = new ArrayList<>();

    /**
     * A convenience class which allows the plotting of time-series observations graphs.
     * @param lineChart LineChart view to be updated
     */
    public ObservationLineChart(com.github.mikephil.charting.charts.LineChart lineChart){
        this.lineChart = lineChart;
    }

    /**
     * Sorts all entries by their x-values to ensure logical plot by time.
     * @param entries the list of graphing entries for a dataset
     */
    private static void sortEntriesOnXValue(List<Entry> entries){
        Collections.sort(entries, (o1, o2) -> Math.round(o1.getX() - o2.getX()));
    }

    /**
     * Creates the line chart based on the data provided.
     */
    public void plot(){
        LineData lineData = new LineData(this.lineDataSets.toArray(new LineDataSet[lineDataSets.size()]));
        lineChart.setData(lineData);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(false);
        lineChart.invalidate();
    }

    /**
     * Creates one dataset object and stores in an array of datasets. Each dataset represents one
     * line in the final chart.
     * @param quantitativeObservations a list of QuantitativeObservation to be plotted
     * @param label the label for the dataset
     * @param colour colour of the dataset's line
     */
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
