import java.util.*;

public class WorkloadReport {
	private String title;
	private List<TeacherWorkload> workloads;
	private boolean approved;
	private Long approvedBy;
	private int totalHours;
	public WorkloadReport(String title) {
		this.title = title;
		this.workloads = new ArrayList<>();
		this.approved = false;
		this.totalHours = 0;
	}
	
	public String getTitle() {return title;}
	public List<TeacherWorkload> getWorkloads(){return workloads;}
	public boolean isApproved() {return approved;}
	public Long getApprovedBy() {return approvedBy;}
	public int getTotalHours() {return totalHours;}
	
	public void addWorkload(TeacherWorkload workload) {
		workloads.add(workload);
		totalHours += workload.getTotalHours();
	}
	
	public void approve(Long userId) {
		this.approved = true;
		this.approvedBy = userId;
	}
	
	@Override
	public String toString() {
		return "WorkloadReport{title: " + title + 
				", teachers=" + workloads.size() + 
				", totalHours=" + totalHours + 
				", approved=" + approved + "}";
	}
}