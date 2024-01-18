import java.util.ArrayList;
import java.util.Map;
import java.lang.Object;

public class ScrumMaster extends TeamMember{


    public ScrumMaster(int teamSize, int sprintCount) {
        super(teamSize, "ScrumMaster", sprintCount);
    }

    @Override
    public void operate() {

        Database db = new Database();
        System.out.println("Connecting database ... scrumMaster");


        System.out.println("Database connected! scrumMaster");

        ArrayList<Map<String, Object>> arrayList = db.getProductBacklog();

        for (int i = 2 ; i < teamSize ; i++){
            db.insertSprintBacklog(
                    (int) arrayList.get(i - 2).get("id"),
                    (String) arrayList.get(i - 2).get("taskName"),
                    (int) arrayList.get(i -2).get("backlogId"),
                    (int) arrayList.get(i - 2).get("priority"),
                    sprintId
            );
        }


        db.close();
        System.out.println("Connection closed! scrumMaster");


    }

    @Override
    public void run() {

        for (int i=1;i <= sprintCount; i++){

            try {
                thread.join(80);
                System.out.println(threadName + " (sprint"+i+")");
                operate();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(threadName + " finished...");
    }
}
