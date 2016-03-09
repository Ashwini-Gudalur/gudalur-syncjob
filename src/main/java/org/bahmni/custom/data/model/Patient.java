package org.bahmni.custom.data.model;

import org.bahmni.custom.util.Utils.GENDER;

import java.sql.Timestamp;
import java.util.Date;

public class Patient
{
	private int id;
	private String bahmniId;
	private String patientUuid;
	private GENDER gender;
	private Timestamp birthDate;

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	private Timestamp date;

	@Override
	public String toString() {
		return "SickleCellPatient{" +
				"id=" + id +
				", bahmniId='" + bahmniId + '\'' +
				", patientUuid='" + patientUuid + '\'' +
				", gender=" + gender +
				'}';
	}

	public String getPatientUuid() {
		return patientUuid;
	}

	public void setPatientUuid(String patientUuid) {
		this.patientUuid = patientUuid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBahmniId() {
		return bahmniId;
	}

	public void setBahmniId(String bahmniId) {
		this.bahmniId = bahmniId;
	}

	public GENDER getGender() {
		return gender;
	}

	public void setGender(GENDER gender) {
		this.gender = gender;
	}

	public Patient() {

	}

	public Timestamp getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Timestamp birthDate) {
		this.birthDate = birthDate;
	}
}
