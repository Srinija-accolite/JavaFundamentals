package javaBasic;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateMerger {
	public static void main(String[] args) {

	    ArrayList<DateMerger.Interval> list = new ArrayList<DateMerger.Interval>();


	    list.add(new DateMerger.Interval("20190101", "20190115"));
	    list.add(new DateMerger.Interval("20190115", "20190215"));
	    list.add(new DateMerger.Interval("20190310", "20190412"));
	    list.add(new DateMerger.Interval("20190410", "20190515"));


	for (Iterator iterator = mergeInterval(list).iterator(); iterator.hasNext();) {
	    Interval interval = (Interval) iterator.next();

	    System.out.println(interval.getStart()+ "==="+interval.getEnd());
	}

	}

	public static List<Interval>  mergeInterval(ArrayList<DateMerger.Interval> list)
	{
	    Collections.sort(list);
	    Set<DateMerger.Interval> resultlist = new TreeSet<DateMerger.Interval>();

	    List<DateMerger.Interval> mergedIntervals = new ArrayList<DateMerger.Interval>();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    if(list.size() == 1){
	        return list;
	    }
	    if(list.size() > 1){
	        Interval mergeInterval = list.get(0);
	        for(int i=1; i< list.size() ; i++){

	            Interval interval2 = list.get(i);
	            try{

	                Date startDate1  = sdf.parse(mergeInterval.getStart());
	                Date endDate1  = sdf.parse(mergeInterval.getEnd());


	                Date startDate2  = sdf.parse(interval2.getStart());
	                Date endDate2  = sdf.parse(interval2.getEnd());

	                if(startDate2.compareTo(endDate1) < 0 ){

	                    if(endDate2.compareTo(endDate1) > 0 ){

	                        mergeInterval.setEnd(interval2.getEnd());

	                    }
	                }else{
	                     mergeInterval = interval2;

	                }
	                resultlist.add(mergeInterval);
	            }
	            catch(Exception ex){
	                ex.printStackTrace();
	            }

	        }

	    }
	    mergedIntervals.addAll(resultlist);
	    return mergedIntervals;

	}

	public static class Interval implements Comparable<Interval>{

	    private String start;
	    private String end;

	    public String getStart() {
	        return start;
	    }
	    public void setStart(String start) {
	        this.start = start;
	    }
	    public String getEnd() {
	        return end;
	    }
	    public void setEnd(String end) {
	        this.end = end;
	    }
	    public Interval(String start,String end){

	        this.start = start;
	        this.end = end;

	    }
	        @Override
	        public boolean equals(Object obj) {
	            // TODO Auto-generated method stub
	            Interval inteval = (Interval)obj;
	            return this.getStart().equals(inteval.getStart()) && this.getEnd().equals(inteval.getEnd()) ;
	        }

	    @Override
	    public int compareTo(Interval o) {
	        // TODO Auto-generated method stub

	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	        try{
	            Date startDate  = sdf.parse(start);
	            Date endDate  = sdf.parse(end);
	            Date pstartDate  = sdf.parse(o.start);
	            Date pendDate  = sdf.parse(o.end);

	            return startDate.compareTo(pstartDate);

	        }catch(Exception ex){
	            ex.printStackTrace();
	        }
	        return 0;
	    }


	}

}
