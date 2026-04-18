package repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import models.CGPARecordModel;
import utils.OracleDatabase;

public class CGPARepository {
    private static final String INSERT_CGPA_RECORD_SQL =
            "INSERT INTO CGPA_RECORDS (CGPA_ID, STUDENT_ID, CALC_MODE, CALCULATED_GPA, CALC_DATE) " +
            "VALUES (?, ?, ?, ?, SYSDATE)";

    private static final String INSERT_CGPA_SUBJECT_SQL =
            "INSERT INTO CGPA_SUBJECTS (CGPA_SUB_ID, CGPA_ID, SUBJECT_ID, MARKS, CREDIT_HOURS, GRADE_POINT) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String GET_SUBJECT_ID_SQL =
            "SELECT SUBJECT_ID FROM SUBJECTS WHERE LOWER(SUBJECT_NAME) = LOWER(?)";

    private static final String GET_HISTORY_SQL =
            "SELECT r.CGPA_ID, r.CALC_MODE, r.CALCULATED_GPA, r.CALC_DATE, " +
            "       s.SUBJECT_ID, s.MARKS, s.CREDIT_HOURS, s.GRADE_POINT " +
            "FROM CGPA_RECORDS r " +
            "LEFT JOIN CGPA_SUBJECTS s ON r.CGPA_ID = s.CGPA_ID " +
            "WHERE r.STUDENT_ID = ? " +
            "ORDER BY r.CALC_DATE DESC, r.CGPA_ID, s.CGPA_SUB_ID";

    public static class SubjectEntry {
        private final String subjectName;
        private final double marks;
        private final double creditHours;
        private final double gradePoint;

        public SubjectEntry(String subjectName, double marks, double creditHours, double gradePoint) {
            this.subjectName = subjectName;
            this.marks = marks;
            this.creditHours = creditHours;
            this.gradePoint = gradePoint;
        }

        public String getSubjectName() { return subjectName; }
        public double getMarks() { return marks; }
        public double getCreditHours() { return creditHours; }
        public double getGradePoint() { return gradePoint; }
    }

    public static boolean saveMarksRecord(String studentId, double calculatedGpa) {
        if (!OracleDatabase.isAvailable()) {
            System.out.println("[CGPA] Oracle database unavailable. Cannot save CGPA history.");
            return false;
        }
        String cgpaId = generateId(10);
        try (Connection conn = OracleDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_CGPA_RECORD_SQL)) {
            ps.setString(1, cgpaId);
            ps.setString(2, studentId);
            ps.setString(3, "MARKS_BASED");
            ps.setDouble(4, calculatedGpa);
            int rows = ps.executeUpdate();
            System.out.println("[CGPA] Saved MARKS CGPA record: " + rows + " rows.");
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("[CGPA] Failed to save MARKS CGPA record: " + e.getMessage());
            return false;
        }
    }

    public static boolean saveSubjectRecord(String studentId, double calculatedGpa, List<SubjectEntry> subjects) {
        if (!OracleDatabase.isAvailable()) {
            System.out.println("[CGPA] Oracle database unavailable. Cannot save CGPA history.");
            return false;
        }
        if (subjects == null || subjects.isEmpty()) {
            System.out.println("[CGPA] No subject entries provided. Skipping save.");
            return false;
        }

        String cgpaId = generateId(10);
        try (Connection conn = OracleDatabase.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psRecord = conn.prepareStatement(INSERT_CGPA_RECORD_SQL);
                 PreparedStatement psSubject = conn.prepareStatement(INSERT_CGPA_SUBJECT_SQL)) {
                psRecord.setString(1, cgpaId);
                psRecord.setString(2, studentId);
                psRecord.setString(3, "SUBJECT_BASED");
                psRecord.setDouble(4, calculatedGpa);
                psRecord.executeUpdate();

                for (int i = 0; i < subjects.size(); i++) {
                    SubjectEntry subject = subjects.get(i);
                    String subjectId = findSubjectId(conn, subject.getSubjectName());
                    String cgpaSubId = generateId(10);
                    psSubject.setString(1, cgpaSubId);
                    psSubject.setString(2, cgpaId);
                    if (subjectId != null && !subjectId.isEmpty()) {
                        psSubject.setString(3, subjectId);
                    } else {
                        psSubject.setNull(3, java.sql.Types.VARCHAR);
                    }
                    psSubject.setDouble(4, subject.getMarks());
                    psSubject.setDouble(5, subject.getCreditHours());
                    psSubject.setDouble(6, subject.getGradePoint());
                    psSubject.executeUpdate();
                }
                conn.commit();
                System.out.println("[CGPA] Saved SUBJECT CGPA record with " + subjects.size() + " subject rows.");
                return true;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("[CGPA] Failed to save SUBJECT CGPA record: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("[CGPA] DB connection error while saving subject CGPA: " + e.getMessage());
            return false;
        }
    }

    private static String findSubjectId(Connection conn, String subjectName) {
        if (subjectName == null || subjectName.isEmpty()) {
            return null;
        }
        try (PreparedStatement ps = conn.prepareStatement(GET_SUBJECT_ID_SQL)) {
            ps.setString(1, subjectName.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("SUBJECT_ID");
                }
            }
        } catch (SQLException e) {
            System.out.println("[CGPA] Failed to lookup subject ID for '" + subjectName + "': " + e.getMessage());
        }
        return null;
    }

    private static String generateId(int length) {
        long value = Math.abs(System.nanoTime() % (long) Math.pow(10, Math.min(length, 18)));
        String formatted = String.format("%0" + length + "d", value);
        return formatted.substring(formatted.length() - length);
    }

    public static List<CGPARecordModel> getHistory(String studentId) {
        List<CGPARecordModel> history = new ArrayList<>();
        if (!OracleDatabase.isAvailable()) {
            System.out.println("[CGPA] Oracle database unavailable. Cannot load CGPA history.");
            return history;
        }
        try (Connection conn = OracleDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_HISTORY_SQL)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String cgpaId = rs.getString("CGPA_ID");
                    String calcMode = rs.getString("CALC_MODE");
                    double calculatedGpa = rs.getDouble("CALCULATED_GPA");
                    Timestamp date = rs.getTimestamp("CALC_DATE");
                    String calcDate = date != null ? date.toString() : "";
                    String subjectReference = rs.getString("SUBJECT_ID");
                    double marks = rs.getDouble("MARKS");
                    double creditHours = rs.getDouble("CREDIT_HOURS");
                    double gradePoint = rs.getDouble("GRADE_POINT");
                    history.add(new CGPARecordModel(cgpaId, calcMode, calculatedGpa, calcDate,
                            subjectReference != null ? subjectReference : "", marks, creditHours, gradePoint));
                }
            }
        } catch (SQLException e) {
            System.out.println("[CGPA] Failed to load history: " + e.getMessage());
        }
        return history;
    }
}
