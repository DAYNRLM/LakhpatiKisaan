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
}
