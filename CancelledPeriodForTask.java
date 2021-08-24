package javaBasic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CancelledPeriodForTask {

  public static void main(String[] args) throws Exception {

    List<Plan> oldPlanList = new ArrayList<>();
    oldPlanList.add(new Plan(101, LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 20)));
    oldPlanList.add(new Plan(102, LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 20)));
    oldPlanList.add(new Plan(103, LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 20)));
    oldPlanList.add(new Plan(104, LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 20)));
    oldPlanList.add(new Plan(105, LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 20)));
    oldPlanList.add(new Plan(106, LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 20)));
    oldPlanList.add(new Plan(107, LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 20)));

    List<Plan> newPlanList = new ArrayList<>();
    newPlanList.add(new Plan(101, LocalDate.of(2019, 5, 1), LocalDate.of(2019, 5, 5)));
    newPlanList.add(new Plan(102, LocalDate.of(2019, 5, 5), LocalDate.of(2019, 5, 15)));
    newPlanList.add(new Plan(103, LocalDate.of(2019, 5, 5), LocalDate.of(2019, 5, 25)));
    newPlanList.add(new Plan(104, LocalDate.of(2019, 5, 15), LocalDate.of(2019, 5, 18)));
    newPlanList.add(new Plan(105, LocalDate.of(2019, 5, 15), LocalDate.of(2019, 5, 25)));
    newPlanList.add(new Plan(106, LocalDate.of(2019, 5, 25), LocalDate.of(2019, 5, 30)));
    newPlanList.add(new Plan(107, LocalDate.of(2019, 5, 12), LocalDate.of(2019, 5, 13)));
    newPlanList.add(new Plan(107, LocalDate.of(2019, 5, 16), LocalDate.of(2019, 5, 18)));

    List<Plan> cancelledPlanPeriods = getCancelledPeriodsForTask(oldPlanList, newPlanList);

    System.out.println("--CANCELLED PERIODS>>");
    System.out.println(cancelledPlanPeriods);
  }
  public static List<Plan> getCancelledPeriodsForTask(List<Plan> oldPlanList, List<Plan> newPlanList) {
    List<Plan> cancelledPlanPeriods = new ArrayList<>();
    List<Plan> cancelledPlanPeriodsForTask = new ArrayList<>();

    oldPlanList.stream().map(Plan::getTaskId).distinct().forEach(taskId -> {
      List<Plan> oldPlans = oldPlanList.stream().filter(oldPlan -> oldPlan.getTaskId().equals(taskId)).collect(Collectors.toList());
      List<Plan> newPlans = newPlanList.stream().filter(newPlan -> newPlan.getTaskId().equals(taskId)).collect(Collectors.toList());

      oldPlans.stream().forEach(oldPlan -> {
        newPlans.stream().forEach(newPlan -> {
          if (oldPlan.getStartDate().isAfter(newPlan.getEndDate()) || oldPlan.getEndDate().isBefore(newPlan.getStartDate())) {
            cancelledPlanPeriodsForTask.add(new Plan(taskId, oldPlan.getStartDate(), oldPlan.getEndDate()));
          }
          else {
            if (newPlan.getStartDate().isAfter(oldPlan.getStartDate()) && newPlan.getStartDate().isBefore(oldPlan.getEndDate())) {
              cancelledPlanPeriodsForTask.add(new Plan(taskId, oldPlan.getStartDate(), newPlan.getStartDate().minusDays(1L)));
            }
            if (newPlan.getEndDate().isAfter(oldPlan.getStartDate()) && newPlan.getEndDate().isBefore(oldPlan.getEndDate())) {
              cancelledPlanPeriodsForTask.add(new Plan(taskId, newPlan.getEndDate().plusDays(1L), oldPlan.getEndDate()));
            }
          }
        });
        List<LocalDate> dates = cancelledPlanPeriodsForTask.stream().map(Plan::getStartAndEndDate).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        Collections.sort(dates);
        for (int index = 0; index < dates.size(); index = index + 2) {
          cancelledPlanPeriods.add(new Plan(taskId, dates.get(index), dates.get(index + 1)));
        }
        cancelledPlanPeriodsForTask.clear();
      });
    });
    return cancelledPlanPeriods;
  }
}

 class Plan {

	  private Integer taskId;

	  private LocalDate startDate;

	  private LocalDate endDate;

	  public Plan(Integer taskId, LocalDate startDate, LocalDate endDate) {
	    this.taskId = taskId;
	    this.startDate = startDate;
	    this.endDate = endDate;
	  }

	  public Integer getTaskId() {
	    return taskId;
	  }

	  public void setTaskId(Integer taskId) {
	    this.taskId = taskId;
	  }

	  public LocalDate getStartDate() {
	    return startDate;
	  }

	  public void setStartDate(LocalDate startDate) {
	    this.startDate = startDate;
	  }

	  public LocalDate getEndDate() {
	    return endDate;
	  }

	  public void setEndDate(LocalDate endDate) {
	    this.endDate = endDate;
	  }

	  public List<LocalDate> getStartAndEndDate() {
	    return Arrays.asList(this.getStartDate(), this.getEndDate());
	  }

	  @Override
	  public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
	    result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
	    result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
	    return result;
	  }

	  @Override
	  public boolean equals(Object obj) {
	    if (this == obj)
	      return true;
	    if (obj == null)
	      return false;
	    if (getClass() != obj.getClass())
	      return false;
	    Plan other = (Plan) obj;
	    if (endDate == null) {
	      if (other.endDate != null)
	        return false;
	    }
	    else if (!endDate.equals(other.endDate))
	      return false;
	    if (startDate == null) {
	      if (other.startDate != null)
	        return false;
	    }
	    else if (!startDate.equals(other.startDate))
	      return false;
	    if (taskId == null) {
	      if (other.taskId != null)
	        return false;
	    }
	    else if (!taskId.equals(other.taskId))
	      return false;
	    return true;
	  }

	  @Override
	  public String toString() {
	    return "\n[taskId=" + taskId + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	  }
	}
