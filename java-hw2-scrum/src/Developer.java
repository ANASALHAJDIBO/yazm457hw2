import java.util.Map;

public class Developer extends TeamMember{

    public Developer(int teamSize, String threadName, int sprintCount) {
        super(teamSize, threadName, sprintCount);
    }

    @Override
    public void run() {
        for (int i=1;i <= this.sprintCount; i++){

            try {
                thread.join(180);
                System.out.println(threadName + " (sprint"+i+")");
                operate();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(threadName + " finished...");
    }

    @Override
    public void operate() {

        Database db = new Database();
        Map<String, Object> map = db.getTask();

        db.insertBoard(
                (int) map.get("taskId"),
                (String) map.get("taskName"),
                (int) map.get("backlogId"),
                (int) map.get("priority"),
                sprintId,
                threadName
        );


    }
}
