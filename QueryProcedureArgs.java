import java.sql.*;
public class QueryProcedureArgs {
  public static void main(String[] args) throws Exception {
    Class.forName("oracle.jdbc.driver.OracleDriver");
    String url = System.getProperty("DB_URL", "jdbc:oracle:thin:@db.freesql.com:1521/23ai_34ui2");
    String user = System.getProperty("DB_USER", "SRI_DIU_23_SCHEMA_9617Q");
    String pass = System.getProperty("DB_PASSWORD", "FYyA13K20SRN2RC720W$P7HNJKNKWI");
    try (Connection conn = DriverManager.getConnection(url, user, pass)) {
      String[] names = {"ADD_QUESTION", "ADD_OPTION", "SET_CORRECT_OPTION"};
      for (String name : names) {
        System.out.println("--- " + name + " ---");
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT ARGUMENT_NAME, POSITION, DATA_TYPE, IN_OUT FROM USER_ARGUMENTS WHERE OBJECT_NAME = ? ORDER BY POSITION")) {
          ps.setString(1, name);
          try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
              System.out.println(rs.getInt("POSITION") + ": " + rs.getString("ARGUMENT_NAME") + " " + rs.getString("DATA_TYPE") + " " + rs.getString("IN_OUT"));
            }
          }
        }
      }
    }
  }
}
