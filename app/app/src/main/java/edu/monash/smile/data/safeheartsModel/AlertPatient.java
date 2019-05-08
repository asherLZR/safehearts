package edu.monash.smile.data.safeheartsModel;

public class AlertPatient {
    private String alertDescription;
    private String patientName;

    public AlertPatient(
            String alertDescription,
            String patientName
    ) {
        this.alertDescription = alertDescription;
        this.patientName = patientName;
    }

    public String getAlertDescription() {
        return alertDescription;
    }

    public String getPatientName() {
        return patientName;
    }
}
