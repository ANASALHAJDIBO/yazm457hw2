import java.sql.*;
import java.lang.Object;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private String url = "jdbc:mysql://localhost:3306/yazm457hw2";
    private String username = "root";
    private String password = "";
    private Connection connection;
    private Statement statement ;


    public Database (){
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close(){
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertProductBacklog(String taskName, int backlogId, int priority){
        String sql = "INSERT INTO  product_backlog(taskName, backlogId ,priority) VALUES ('%s', %d, %d)"
                .formatted(taskName, backlogId, priority);
        executeStatement(sql);
    }

    public void insertSprintBacklog(int taskId, String taskName, int backlogId, int priority, int sprintId){
        String sql = "INSERT INTO sprint_backlog(taskId, backlogId, priority, taskName, sprintId) VALUES (%d, %d, %d, '%s', %d)"
                .formatted(taskId,backlogId, priority, taskName, sprintId);
        executeStatement(sql);
    }

    public void insertBoard(int taskId, String taskName, int backlogId, int priority, int sprintId, String developerName){
        String sql = "INSERT INTO board (taskId, taskName, backlogId, priority, sprintId, developerName) VALUES (%d, '%s', %d, %d, %d, '%s')"
                .formatted(taskId, taskName, backlogId, priority, sprintId, developerName);
        executeStatement(sql);
    }

    public ArrayList getProductBacklog(){
        String sql = "SELECT * FROM product_backlog\n" +
                "WHERE id NOT IN (SELECT taskId FROM sprint_backlog) ORDER BY priority;";

        ResultSet resultSet;
        ArrayList<Map<String, Object>> list = new ArrayList<>() ;

        try {
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                Map<String , Object> map = new HashMap<>();

                map.put("id",resultSet.getInt("id"));
                map.put("taskName",resultSet.getString("taskName"));
                map.put("backlogId" , resultSet.getInt("backlogId"));
                map.put("priority" , resultSet.getInt("priority"));

                list.add(map);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    // getTask(): Fetches a task from sprint backlog table.
    public synchronized static Map<String, Object> getTask(){
        Database db = new Database();

        String sql = "SELECT * FROM sprint_backlog\n" +
                "WHERE taskId NOT IN (SELECT taskId FROM board)" +
                "AND available = 1";

        ResultSet resultSet;
        Map<String, Object> map = new HashMap<>();

        try {
            resultSet = db.statement.executeQuery(sql);

            resultSet.next();
            map.put("taskId",resultSet.getInt("taskId"));
            map.put("taskName",resultSet.getString("taskName"));
            map.put("backlogId" , resultSet.getInt("backlogId"));
            map.put("priority" , resultSet.getInt("priority"));

            db.statement.execute("UPDATE sprint_backlog SET available = 0 WHERE taskId = %d;".formatted(map.get("taskId")));
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

        return map;
    }



    private void executeStatement(String sql){
        // receives sql query to execute it
        try {
            if (statement.execute(sql)){
                System.out.println("failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clean(){
        // clean database tables
        Database db = new Database();

        db.executeStatement("DELETE FROM board");
        db.executeStatement("DELETE FROM sprint_backlog");
        db.executeStatement("DELETE FROM product_backlog");
    }

}
