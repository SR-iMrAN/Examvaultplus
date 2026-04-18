package models;

public class CGPARecordModel {
    private final String cgpaId;
    private final String calcMode;
    private final double calculatedGpa;
    private final String calcDate;
    private final String subjectReference;
    private final double marks;
    private final double creditHours;
    private final double gradePoint;

    public CGPARecordModel(String cgpaId, String calcMode, double calculatedGpa, String calcDate,
                           String subjectReference, double marks, double creditHours, double gradePoint) {
        this.cgpaId = cgpaId;
        this.calcMode = calcMode;
        this.calculatedGpa = calculatedGpa;
        this.calcDate = calcDate;
        this.subjectReference = subjectReference;
        this.marks = marks;
        this.creditHours = creditHours;
        this.gradePoint = gradePoint;
    }

    public String getCgpaId() { return cgpaId; }
    public String getCalcMode() { return calcMode; }
    public double getCalculatedGpa() { return calculatedGpa; }
    public String getCalcDate() { return calcDate; }
    public String getSubjectReference() { return subjectReference; }
    public double getMarks() { return marks; }
    public double getCreditHours() { return creditHours; }
    public double getGradePoint() { return gradePoint; }
}
