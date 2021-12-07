package com.nrlm.lakhpatikisaan.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MemberEntryEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String shgCode;
    public String shgMemberCode;
    public String entryYearCode;
    public String entryMonthCode;
    public String entryCreatedDate;
    public String sectorDate;
    public String activityCode;
    public String incomeFrequencyCode;
    public String incomeRangCode;
    public String flagBeforeAfterNrlm;
    public String flagSyncStatus;

    public String sectorName;
    public String activityName;
    public String incomeFrequencyName;
    public String incomeRangName;
    public String monthName;
    public String seccNumber;

    public String getSeccNumber() {
        return seccNumber;
    }

    public void setSeccNumber(String seccNumber) {
        this.seccNumber = seccNumber;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getIncomeFrequencyName() {
        return incomeFrequencyName;
    }

    public void setIncomeFrequencyName(String incomeFrequencyName) {
        this.incomeFrequencyName = incomeFrequencyName;
    }

    public String getIncomeRangName() {
        return incomeRangName;
    }

    public void setIncomeRangName(String incomeRangName) {
        this.incomeRangName = incomeRangName;
    }

    public String getShgCode() {
        return shgCode;
    }

    public void setShgCode(String shgCode) {
        this.shgCode = shgCode;
    }

    public String getShgMemberCode() {
        return shgMemberCode;
    }

    public void setShgMemberCode(String shgMemberCode) {
        this.shgMemberCode = shgMemberCode;
    }

    public String getEntryYearCode() {
        return entryYearCode;
    }

    public void setEntryYearCode(String entryYearCode) {
        this.entryYearCode = entryYearCode;
    }

    public String getEntryMonthCode() {
        return entryMonthCode;
    }

    public void setEntryMonthCode(String entryMonthCode) {
        this.entryMonthCode = entryMonthCode;
    }

    public String getEntryCreatedDate() {
        return entryCreatedDate;
    }

    public void setEntryCreatedDate(String entryCreatedDate) {
        this.entryCreatedDate = entryCreatedDate;
    }

    public String getSectorDate() {
        return sectorDate;
    }

    public void setSectorDate(String sectorDate) {
        this.sectorDate = sectorDate;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getIncomeFrequencyCode() {
        return incomeFrequencyCode;
    }

    public void setIncomeFrequencyCode(String incomeFrequencyCode) {
        this.incomeFrequencyCode = incomeFrequencyCode;
    }

    public String getIncomeRangCode() {
        return incomeRangCode;
    }

    public void setIncomeRangCode(String incomeRangCode) {
        this.incomeRangCode = incomeRangCode;
    }

    public String getFlagBeforeAfterNrlm() {
        return flagBeforeAfterNrlm;
    }

    public void setFlagBeforeAfterNrlm(String flagBeforeAfterNrlm) {
        this.flagBeforeAfterNrlm = flagBeforeAfterNrlm;
    }

    public String getFlagSyncStatus() {
        return flagSyncStatus;
    }

    public void setFlagSyncStatus(String flagSyncStatus) {
        this.flagSyncStatus = flagSyncStatus;
    }
}
