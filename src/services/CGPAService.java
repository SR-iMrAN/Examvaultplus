package services;

public class CGPAService {

    private static final String[][] GRADE_TABLE = {
        {"80", "100", "A+", "4.00", "Outstanding"},
        {"75", "79",  "A",  "3.75", "Excellent"},
        {"70", "74",  "A-", "3.50", "Very Good"},
        {"65", "69",  "B+", "3.25", "Good"},
        {"60", "64",  "B",  "3.00", "Satisfactory"},
        {"55", "59",  "B-", "2.75", "Above Average"},
        {"50", "54",  "C+", "2.50", "Average"},
        {"45", "49",  "C",  "2.25", "Below Average"},
        {"40", "44",  "D",  "2.00", "Pass"},
        {"0",  "39",  "F",  "0.00", "Fail"}
    };

    public static String[] getGradeInfoByMarks(double marks) {
        for (String[] row : GRADE_TABLE) {
            double min = Double.parseDouble(row[0]);
            double max = Double.parseDouble(row[1]);
            if (marks >= min && marks <= max) {
                return new String[]{row[2], row[3], row[4]};
            }
        }
        return new String[]{"F", "0.00", "Invalid"};
    }

    public static String[] getGradeInfoByGrade(String grade) {
        for (String[] row : GRADE_TABLE) {
            if (row[2].equalsIgnoreCase(grade)) {
                return new String[]{row[2], row[3], row[4]};
            }
        }
        return null;
    }

    public static double cgpaToMarks(double cgpa) {
        if (cgpa >= 4.00) return 85;
        if (cgpa >= 3.75) return 77;
        if (cgpa >= 3.50) return 72;
        if (cgpa >= 3.25) return 67;
        if (cgpa >= 3.00) return 62;
        if (cgpa >= 2.75) return 57;
        if (cgpa >= 2.50) return 52;
        if (cgpa >= 2.25) return 47;
        if (cgpa >= 2.00) return 42;
        return 35;
    }

    public static double calculateCGPA(double[] gradePoints, double[] credits) {
        double totalCredits = 0, weighted = 0;
        for (int i = 0; i < gradePoints.length; i++) {
            totalCredits += credits[i];
            weighted += gradePoints[i] * credits[i];
        }
        return totalCredits > 0 ? weighted / totalCredits : 0;
    }

    public static String[][] getGradeTable() { return GRADE_TABLE; }
}
