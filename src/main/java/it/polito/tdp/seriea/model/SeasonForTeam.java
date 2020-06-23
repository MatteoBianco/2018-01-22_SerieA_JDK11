package it.polito.tdp.seriea.model;

public class SeasonForTeam implements Comparable<SeasonForTeam> {
	
	private String team;
	private Integer season;
	private Integer points;
	
	public SeasonForTeam(String team, Integer season, Integer points) {
		super();
		this.team = team;
		this.season = season;
		this.points = points;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public Integer getSeason() {
		return season;
	}

	public void setSeason(Integer season) {
		this.season = season;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}
	
	public void calculatePoints(Integer wins, Integer draws) {
		this.points = wins*3 + draws;
	}

	@Override
	public int compareTo(SeasonForTeam o) {
		return this.season.compareTo(o.season);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((season == null) ? 0 : season.hashCode());
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
		SeasonForTeam other = (SeasonForTeam) obj;
		if (season == null) {
			if (other.season != null)
				return false;
		} else if (!season.equals(other.season))
			return false;
		return true;
	}

	

}
