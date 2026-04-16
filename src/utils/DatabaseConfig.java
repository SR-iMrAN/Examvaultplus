package utils;

public class DatabaseConfig {
    // Default Oracle FreeSQL connection values. Replace the password with your current schema password.
    public static final String URL = System.getProperty("DB_URL", "jdbc:oracle:thin:@db.freesql.com:1521/23ai_34ui2");
    public static final String USER = System.getProperty("DB_USER", "SRI_DIU_23_SCHEMA_9617Q");
    public static final String PASSWORD = System.getProperty("DB_PASSWORD", "FYyA13K20SRN2RC720W$P7HNJKNKWI");

    // Use the environment or JVM system property if you want to avoid storing credentials in source.
    // Example VM args: -DDB_PASSWORD=YourPasswordHere
}
