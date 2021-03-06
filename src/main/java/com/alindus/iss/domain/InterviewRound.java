package com.alindus.iss.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "interview_rounds")
public class InterviewRound implements Serializable {

	private static final long serialVersionUID = 3658763045503950886L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "round_id")
	@Valid
	private Round round;
	@Enumerated(EnumType.STRING)
	private InterviewStatus status;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "interview_type_id")
	private InterviewType interviewType;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "interview_date")
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date interviewDate;
	@Column(name = "interview_time")
	@Pattern(regexp = "^$|([\\d]{1,2}:[\\d]{1,2})", message = "Interview Time should follow pattern hh:mm")
	private String interviewTime;
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonBackReference
	private Interview interview;
	@OneToOne
	@JoinColumn
	private User callTaker;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "MM/dd/yyyy hh:mm:ss")
	private Date createdDate;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "MM/dd/yyyy hh:mm:ss")
	private Date updatedDate;

	public InterviewRound() {
	}

	public InterviewRound(Round round, InterviewStatus status, InterviewType interviewType, Date interviewDate,
			Interview interview, User callTaker) {
		this.round = round;
		this.status = status;
		this.interviewType = interviewType;
		this.interviewDate = interviewDate;
		this.interview = interview;
		this.callTaker = callTaker;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Round getRound() {
		return round;
	}

	public void setRound(Round round) {
		this.round = round;
	}

	public InterviewStatus getStatus() {
		return status;
	}

	public void setStatus(InterviewStatus status) {
		this.status = status;
	}

	public InterviewType getInterviewType() {
		return interviewType;
	}

	public void setInterviewType(InterviewType interviewType) {
		this.interviewType = interviewType;
	}

	public Date getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(Date interviewDate) {
		this.interviewDate = interviewDate;
	}

	public String getInterviewTime() {
		return interviewTime;
	}

	public void setInterviewTime(String interviewTime) {
		this.interviewTime = interviewTime;
	}

	public Interview getInterview() {
		return interview;
	}

	public void setInterview(Interview interview) {
		this.interview = interview;
	}

	public User getCallTaker() {
		return callTaker;
	}

	public void setCallTaker(User callTaker) {
		this.callTaker = callTaker;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public enum InterviewStatus {
		PENDING, APPROVED, CANCELED, FAILED
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		InterviewRound other = (InterviewRound) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InterviewRound [id=" + id + ", round=" + round + ", status=" + status + ", interviewType="
				+ interviewType + ", interviewDate=" + interviewDate + ", interview=" + interview + ", callTaker="
				+ callTaker + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + "]";
	}

}
