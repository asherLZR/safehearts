package edu.monash.smile.charting;

import android.graphics.Color;
import android.util.Log;

import androidx.core.content.ContextCompat;

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
import java.util.stream.Collectors;

import edu.monash.smile.R;
import edu.monash.smile.data.safeheartsModel.observation.BloodPressureObservation;
import edu.monash.smile.data.safeheartsModel.observation.DiastolicObservation;
import edu.monash.smile.data.safeheartsModel.observation.SystolicObservation;

public class ChartHelper {
    public static void createBloodPressureChart(List<BloodPressureObservation> o, LineChart lineChart){
        // Filter out systolic observations
        List<SystolicObservation> systolicObservations = o.stream()
                .map(BloodPressureObservation::getSystolicObservation)
                .collect(Collectors.toList());
        // Store in list of entries for plotting
        List<Entry> systolicEntries = new ArrayList<>();
        for (SystolicObservation systolicObservation : systolicObservations){
            BigDecimal systolicValue = systolicObservation.getValue();
            Date systolicDate = systolicObservation.getDateObserved();
            Entry entry = new Entry(systolicDate.getTime(), systolicValue.floatValue());
            systolicEntries.add(entry);
        }
        // Sort the entries based on timestamp
        Collections.sort(systolicEntries, (o1, o2) -> Math.round(o1.getX() - o2.getX()));

        List<DiastolicObservation> diastolicObservations = o.stream()
                .map(BloodPressureObservation::getDiastolicObservation)
                .collect(Collectors.toList());
        List<Entry> diastolicEntries = new ArrayList<>();
        for (DiastolicObservation diastolicObservation : diastolicObservations){
            BigDecimal systolicValue = diastolicObservation.getValue();
            Date systolicDate = diastolicObservation.getDateObserved();
            Entry entry = new Entry(systolicDate.getTime(), systolicValue.floatValue());
            diastolicEntries.add(entry);
        }
        Collections.sort(diastolicEntries, (o1, o2) -> Math.round(o1.getX() - o2.getX()));

        LineDataSet systolicDataSet = new LineDataSet(systolicEntries, "Systolic Blood Pressure");
        LineDataSet diastolicDataSet = new LineDataSet(diastolicEntries, "Diastolic Blood Pressure");
        systolicDataSet.setColor(Color.BLUE);
        diastolicDataSet.setColor(Color.BLACK);
        LineData lineData = new LineData(systolicDataSet, diastolicDataSet);
        lineChart.setData(lineData);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(false);
        lineChart.invalidate();
    }
}
