package Connection;

        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.SQLException;



public class ConnectionBD {
    static String login = "puntuaciones";
    static String password = "marianao";
    static String url = "jdbc:mysql://192.168.2.248/puntuaciones_juego";
    static Connection connection = null;
    private static  void ConexionBD() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url,login,password);
            if (connection!=null){
                System.out.println("Conexión a base de datos OK\n");
            }
        }catch(SQLException e){
            System.err.println(e);
        }catch(ClassNotFoundException e){
            System.err.println(e);
        }catch(Exception e){
            System.err.println(e);
        }
    }
    public static  Connection getConnection() throws SQLException {
        if((connection == null)||(connection.isClosed())){
            ConexionBD();
        }else{
            return connection;
        }
        return connection;

    }

    public void desconectar() throws SQLException{
        connection.close();
    }
}
