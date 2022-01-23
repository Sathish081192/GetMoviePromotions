package pojo;

import java.util.List;

public class Properties {

	private String year;
	private String programType;
	private String currency;
	private String programAvailabilityId;
	private String duration;
	private List<String> categories;
	private List<String> genre;
	private ProgramDescription programDescription;
	private Rating rating;
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getProgramType() {
		return programType;
	}
	public void setProgramType(String programType) {
		this.programType = programType;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getProgramAvailabilityId() {
		return programAvailabilityId;
	}
	public void setProgramAvailabilityId(String programAvailabilityId) {
		this.programAvailabilityId = programAvailabilityId;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	public List<String> getGenre() {
		return genre;
	}
	public void setGenre(List<String> genre) {
		this.genre = genre;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public ProgramDescription getProgramDescription() {
		return programDescription;
	}
	public void setProgramDescription(ProgramDescription programDescription) {
		this.programDescription = programDescription;
	}
	public Rating getRating() {
		return rating;
	}
	public void setRating(Rating rating) {
		this.rating = rating;
	}	
	
	
}
