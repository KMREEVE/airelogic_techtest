import java.time.LocalDateTime;
import java.util.LinkedList;

public class Patient {

    // Required:
    // - Air or Oxygen
    // | Enum(Integer) | 0 if air, 2 if on oxygen
    // ####
    // - Level of consciousness or new confusion (whether the patient is newly confused, disorientated or agitated)
    // | Enum(Integer) | 0 if alert, non-zero if CVPU
    // ####
    // - Respiration Rate
    // | Integer
    // ####
    // - Oxygen Saturation (SpO2)
    // | Integer
    // ####
    // - Temperature
    // | Float | This should be rounded to a single decimal place.

    // -- Declarations --
    public String myName;
    enum AirType {                  // Air or Oxygen
        AIR(0), OXYGEN(2);
        private final int value;
        AirType(int value) { this.value = value; }
        public int getValue() { return value; }
    }
    public AirType currentAirType;

    enum ConsciousnessLevel {        // Level of Consciousness
        ALERT(0), CVPU(3);
        private final int value;
        ConsciousnessLevel(int value) { this.value = value; }
        public int getValue() { return value; }
    }
    public ConsciousnessLevel currentConsciousnessLevel;

    enum LastMeal {     // For CBG
        WITHIN_2HRS,
        FASTING,
    }
    public LastMeal lastMeal;

    public Integer respRate;               // Respiration Rate
    public Integer SpO2;                   // Oxygen Saturation
    public Float temperature;              // Temperature
    public Float cbgLevel;                 // Capillary Blood Glucose

    // -- Constructor --
    public Patient(String myName, AirType myAirType, ConsciousnessLevel myConsciousnessLevel, Integer myRespRate, Integer mySpO2, Float myTemperature, LastMeal myLastMeal, Float myCbgLevel) {
        this.myName = myName;
        this.currentAirType = myAirType;
        this.currentConsciousnessLevel = myConsciousnessLevel;
        this.respRate = myRespRate;
        this.SpO2 = mySpO2;
        this.temperature = Math.round(myTemperature * 10.0f) / 10.0f;
        this.lastMeal = myLastMeal;
        this.cbgLevel = myCbgLevel;
    }

    // Time Stamped Score Entry
    private static class TStampedMediScore {
        int score;
        LocalDateTime timeStamp;

        TStampedMediScore(int score, LocalDateTime timeStamp){
            this.score = score;
            this.timeStamp = timeStamp;
        }
    }

    private LinkedList<TStampedMediScore> mediScoreHistory = new LinkedList<>();

    public void AddMediScore() {
        Integer newScore = CalculateMediScore();
        LocalDateTime now = LocalDateTime.now();

        // Removes Medi Scores older than 24 hours
        mediScoreHistory.removeIf(entry -> entry.timeStamp.isBefore(now.minusHours(24)));

        // Alerts if score has increased by more than two points
        if (!mediScoreHistory.isEmpty() && (newScore - mediScoreHistory.getFirst().score) > 2) {
            System.out.println("ALERT !! Medi Score for " + myName + " has increased by more than 2 points in 24 hours!!!");
        }

        // Adds the new score
        mediScoreHistory.add(new TStampedMediScore(newScore, now));
    }

    // -- Main --
    // Calculate Medi Score
    public Integer CalculateMediScore(){
        System.out.println(GetAirScore(currentAirType));
        System.out.println(GetConciousnessScore(currentConsciousnessLevel));
        System.out.println(GetRespScore(respRate));
        System.out.println(GetSpO2Score(SpO2, currentAirType));
        System.out.println(GetTempScore(temperature));
        System.out.println(GetCBGScore(cbgLevel, lastMeal)); // New CBG score calculation
        Integer mediScore =
                GetAirScore(currentAirType)
                        + GetConciousnessScore(currentConsciousnessLevel)
                        + GetRespScore(respRate)
                        + GetSpO2Score(SpO2, currentAirType)
                        + GetTempScore(temperature)
                        + GetCBGScore(cbgLevel, lastMeal);
        return mediScore;
    }

    // -- Getting of score functions --
    // Get Air Score
    public static Integer GetAirScore(AirType currentAirType){
        return currentAirType.getValue();
    }

    // Get Conciousness Score
    public static Integer GetConciousnessScore(ConsciousnessLevel currentConciousnessLevel){
        return currentConciousnessLevel.getValue();
    }

    // Get Respiration Rate Score
    public static Integer GetRespScore(Integer currentRespRate){
        if (currentRespRate <= 8 || currentRespRate >= 25) {
            return 3;
        } else if (currentRespRate >= 9 && currentRespRate <= 11) {
            return 1;
        } else if (currentRespRate >= 12 && currentRespRate <= 20) {
            return 0;
        } else if (currentRespRate >= 21 && currentRespRate <= 24) {
            return 2;
        }
        return 0;
    }

    // Get Oxygen Saturation Score
    public static Integer GetSpO2Score(Integer currentSpO2, AirType currentAirType){
        if (currentSpO2 <= 83) return 3;
        if (currentSpO2 >= 97 && currentAirType == AirType.OXYGEN) return 3;
        if ((currentSpO2 == 84 || currentSpO2 == 85) || ((currentSpO2 == 95 || currentSpO2 == 96) && currentAirType == AirType.OXYGEN)) return 2;
        if ((currentSpO2 == 86 || currentSpO2 == 87) || ((currentSpO2 == 93 || currentSpO2 == 94) && currentAirType == AirType.OXYGEN)) return 1;
        if ((currentSpO2 >= 88 && currentSpO2 <= 92) || (currentSpO2 >= 93 && currentAirType == AirType.AIR)) return 0;
        return 0;
    }

    // Get Temperature Score
    public static Integer GetTempScore(Float currentTemp){
        if (currentTemp < 35.0f) return 3;
        if (currentTemp > 39.1f) return 2;
        if (currentTemp >= 38.1f && currentTemp <= 39.0f) return 1;
        if (currentTemp >= 36.1f && currentTemp <= 38.0f) return 0;
        return 0;
    }

    // Get CBG Score
    public static Integer GetCBGScore(Float cbgLevel, LastMeal lastMeal){
        if (lastMeal == LastMeal.FASTING){
            if (cbgLevel <= 3.4f || cbgLevel >= 6.0f) return 3;
            if (cbgLevel >= 5.5f && cbgLevel <= 5.9f) return 2;
            if (cbgLevel >= 3.5f && cbgLevel <= 3.9f) return 2;
            if (cbgLevel >= 4.0f && cbgLevel <= 5.4f) return 0;
        }else {     // Has eaten within 2 hours
            if (cbgLevel <= 4.5f || cbgLevel >= 9.0f) return 3;
            if (cbgLevel >= 7.9f && cbgLevel <= 8.9f) return 2;
            if (cbgLevel >= 4.5f && cbgLevel <= 5.8f) return 2;
            if (cbgLevel >= 5.9f && cbgLevel <= 7.8f) return 0;
        }
        return 0;
    }
}
