import java.util.Random;

public class ProductOwner extends TeamMember{


    public static class Task{
        String name;
        int priority;
        int backlogId;

        public Task(String name, int priority , int backlogId){
            this.name = name;
            this.priority = priority;
            this.backlogId = backlogId;
        }

        public static Task generateTask(){
            String[] tasks = {"testing", "documenting", "coding", "fix bug", "implement feature" , "review code"};
            Random ran = new Random();

            int index = ran.nextInt(6);
            String taskName = tasks[index];

            int priority = ran.nextInt(10) + 1;
            int backlogId = 200;

            return new Task(taskName, priority, backlogId + sprintId);
        }

        @Override
        public String toString() {
            return "(name:%s, p:%d)".formatted(name, priority);
        }
    }

    public ProductOwner(int teamSize, int sprintCount) {
        super(teamSize, "ProductOwner", sprintCount);
    }

    @Override
    public void operate() {
        Database db = new Database();
        System.out.println("Connecting database ... productOwner");

        System.out.println("Database connected! productOwner");

        for (int i = 2; i < teamSize ; i++){
            Task task = Task.generateTask();
            db.insertProductBacklog(task.name, task.backlogId , task.priority);
        }

        db.close();
        System.out.println("Connection closed! productOwner");
    }

    @Override
    public void run() {

        for (int i=1;i <= sprintCount; i++){
            sprintId ++;

            try {
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
