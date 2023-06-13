package com.whiteminnow.fhirclient.library;

import com.whiteminnow.fhirclient.model.NdiRequestLine;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;

import java.util.List;

public class ResourceMapper {
  public static NdiRequestLine patientToNdiRequestLine(Patient patient) {
    NdiRequestLine result = new NdiRequestLine();

    HumanName humanName =  patient.getNameFirstRep();
    if (humanName.hasFamily()) {
      result.setLastName(humanName.getFamily());
    }
    if (humanName.hasGiven()) {
      List<StringType> given = humanName.getGiven();
      if (given.size() > 0) {
        StringType firstName = given.get(0);
        result.setFirstName(firstName.getValue());

        if (given.size() > 1) {
          StringType middleName = given.get(1);
          String middleInitial = middleName.getValue().substring(0, 1);
          result.setMiddleInitial(middleInitial);
        }
      }
    }

    if (patient.hasBirthDateElement()) {
      DateType dateType = patient.getBirthDateElement();
      Integer day = dateType.getDay();
      if (day != null) {
        String rawDay = String.valueOf(day.intValue());
        String formattedDay = rawDay.length() == 1 ? "0" + rawDay : rawDay;
        result.setDayOfBirth(formattedDay);
      }
      Integer month = dateType.getMonth();
      if (month != null) {
        String rawMonth = String.valueOf(month.intValue() + 1);
        String formattedMonth = rawMonth.length() == 1 ? "0" + rawMonth : rawMonth;
        result.setMonthOfBirth(formattedMonth);
      }
      Integer year = dateType.getYear();
      if (year != null) {
        String formattedYear = String.valueOf(year);
        result.setYearOfBirth(formattedYear);
      }
    }

    if (patient.hasGender()) {
      Enumerations.AdministrativeGender gender = patient.getGender();
      if (gender == Enumerations.AdministrativeGender.MALE) {
        result.setSex("M");
      }
      if (gender == Enumerations.AdministrativeGender.FEMALE) {
        result.setSex("F");
      }
    }

    return result;
  }
}
