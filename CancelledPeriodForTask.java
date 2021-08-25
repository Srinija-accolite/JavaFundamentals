package javaBasic;
import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.*;

class Plan implements Comparable<Plan>
{
    Integer taskId;
    LocalDate startDate;
    LocalDate endDate;
    public Plan()
    {
        this.taskId=0;
        this.startDate=this.endDate=null;
    }


    Integer getTaskId() { return this.taskId;}

    void setTaskId( Integer taskId){ this.taskId = taskId; }

    LocalDate getStartDate(){ return this.startDate;}

    void setStartDate(LocalDate startDate){ this.startDate = startDate;}

    LocalDate getEndDate(){return this.endDate;}

    void setEndDate(LocalDate endDate){ this.endDate= endDate;}

    @Override
    public String toString()
    {
        return ""+this.taskId+" "+this.startDate+" "+this.endDate+"\n";
    }

    public int compareTo(Plan other){
        return other.endDate.compareTo(this.endDate);
    }

}



public class CancelledPeriodForTask
{

    public static List<Plan> readPlan() throws Exception
    {

        List<Plan> lp= new LinkedList<Plan>();

        Scanner sc=new Scanner(System.in);

        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

        String s;
        while ( (s=br.readLine()) != null && !s.equals("")&&!s.equals("\n"))
        {
            Plan p= new Plan();
            String[] list = s.split("\\s+");
            p.setTaskId(Integer.parseInt(list[0]) );
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMMyyyy");
            p.setStartDate(LocalDate.parse(list[1].replace("-",""), formatter));
            p.setEndDate(LocalDate.parse(list[2].replace("-",""), formatter));
            lp.add(p);
        }
        return lp;
    }


    public static List<Plan> getCancelledPeriodsForTask(List<Plan> oldPlan, List<Plan> newPlan)
    {
        List<Plan> cancel_list= new ArrayList<Plan>();
        HashMap<Integer,ArrayList<Plan>> oldmap=new HashMap<>();
        HashMap<Integer,ArrayList<Plan>> newmap=new HashMap<>();

        for( Plan oldplan: oldPlan){
          if(!oldmap.containsKey(oldplan.taskId)){
              oldmap.put(oldplan.taskId,new ArrayList<>());
          }
            oldmap.get(oldplan.taskId).add(oldplan);
        }


        for( Plan newplan: newPlan){
            if(!newmap.containsKey(newplan.taskId)){
                newmap.put(newplan.taskId,new ArrayList<>());
            }
            newmap.get(newplan.taskId).add(newplan);
        }


        for(Map.Entry<Integer,ArrayList<Plan>> row:newmap.entrySet()){
            ArrayList<Plan> list=row.getValue();
            Collections.sort(list);
            Plan oldplan=oldmap.get(row.getKey()).get(0);
            for(Plan current:list){
                if(oldplan.endDate.isBefore(current.startDate)){
                    Plan cancelplan=new Plan();
                    cancelplan.taskId=oldplan.taskId;
                    cancelplan.startDate=oldplan.startDate;
                    cancelplan.endDate=oldplan.endDate;
                    cancel_list.add(cancelplan);
                    oldplan.endDate=oldplan.startDate;
                    continue;
                }
              if(oldplan.endDate.isAfter(current.endDate)){

                  Plan cancelplan=new Plan();
                  cancelplan.taskId=oldplan.taskId;
                  cancelplan.endDate=oldplan.endDate;
                  if(oldplan.startDate.isBefore(current.endDate)) {
                      cancelplan.startDate=current.endDate.plusDays(1);
                      oldplan.endDate=current.startDate;
                  }
                  else{
                      cancelplan.startDate=oldplan.startDate;
                      oldplan.endDate=oldplan.startDate;
              }
                  cancel_list.add(cancelplan);
              }

              if(oldplan.endDate.isBefore(current.endDate)){
                  oldplan.endDate=current.startDate.minusDays(1);
              }
              if(current.startDate.isBefore(oldplan.startDate)){
                  oldplan.endDate=oldplan.startDate;
              }

            }
            if(!oldplan.endDate.equals(oldplan.startDate)){

                Plan cancelplan=new Plan();
                cancelplan.taskId=oldplan.taskId;
                cancelplan.startDate=oldplan.startDate;
                cancelplan.endDate=oldplan.endDate;
                cancel_list.add(cancelplan);
            }


        }

        return cancel_list;

    }
    public static void main(String args[]) throws Exception
    {
        List<Plan> oldPlan = readPlan();
        List<Plan> newPlan = readPlan();
        List<Plan> cp = getCancelledPeriodsForTask(oldPlan, newPlan);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        System.out.println("Task_ID Start_Date  End_Date");
        for (Plan tmp : cp) {

            System.out.println(tmp.taskId +"  "+tmp.startDate.format(formatter)+"  "+tmp.endDate.format(formatter));
        }
    }
}
