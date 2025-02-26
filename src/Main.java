public class Main {

    // Simple Brief --
    // Write a function to calculate the score for a patient. This is a simple rule-of-thumb score used to identify ill patients.
    //  ---
    // External classes:
    // - Patient
    // -- Contains respirationRate, o2Saturation, level of consciousness or new confusion, and temperature.
    // ####
    // This class drives the program by creating 3 objects representing different patients and printing their mediscore
    // TO-DO
    // - BONUS TASK 1 -
    // Alerting for trends in the Medi score -
    // While the score is useful on its own to assess the urgency of treatment,
    // an increasing score over a short period of time would be worth notifying someone about.
    // Can your system flag up an additional risk if a score has raised by more than 2 points within a 24 hour period?
    // - BONUS TASK 2 -
    //Capillary Blood Glucose is another metric that is regularly recorded, but its range changes depending on when the patient last ate.
    // The ranges (in mmol/L) and scores are as follows:
    // (Check table: https://github.com/airelogic/tech-test-portal/tree/main/Medi-Score-Calculation)


    public static void main(String[] args) {
        // If this were real, the values would be inputted by the user with the timestamp automatically gathered from the system.
        // Patient 1 from Examples
        Patient patient1 = new Patient("Patient 1", Patient.AirType.AIR, Patient.ConsciousnessLevel.ALERT, 15, 95, 37.1f, Patient.LastMeal.WITHIN_2HRS, 5.9f);

        // Patient 2 from Examples
        Patient patient2 = new Patient("Patient 2", Patient.AirType.OXYGEN, Patient.ConsciousnessLevel.ALERT, 17, 95, 37.1f,Patient.LastMeal.FASTING, 3.6f);

        // Patient 3 from Examples
        Patient patient3 = new Patient("Patient 3", Patient.AirType.OXYGEN, Patient.ConsciousnessLevel.CVPU, 23, 88, 38.5f, Patient.LastMeal.FASTING, 6.0f);

        System.out.println("Medi Score of Patient 1 is: " + patient1.CalculateMediScore());
        patient1.AddMediScore();
        System.out.println("\nMedi Score of Patient 2 is: " + patient2.CalculateMediScore());
        patient1.AddMediScore();
        System.out.println("\nMedi Score of Patient 3 is: " + patient3.CalculateMediScore());
        patient3.AddMediScore();

        // Debugging for 24 hour alert
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); } // Simulate delay

        patient1.SpO2 = 83;
        System.out.println("Medi Score of Patient 1 is: " + patient1.CalculateMediScore());
        patient1.AddMediScore();

        System.out.println("\nMedi Score of Patient 2 is: " + patient2.CalculateMediScore());
        patient2.AddMediScore();


        patient3.SpO2 = 96;
        System.out.println("\nMedi Score of Patient 3 is: " + patient3.CalculateMediScore());
        patient3.AddMediScore();
    }
}