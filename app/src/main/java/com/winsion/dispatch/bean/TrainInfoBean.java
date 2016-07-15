package com.winsion.dispatch.bean;

import java.util.List;

/**
 * Created by yalong on 2016/6/13.
 */
public class TrainInfoBean {
    private String date;

    private String waitRoom;

    private String trainNumber;

    private String startStationName;

    private String endStationName;

    private String arriveTime;

    private String departTime;

    private String checkPort;

    private String platform;

    private String currentTrainStatus;

    private boolean onTime;

    public boolean getOnTime() {
        return onTime;
    }

    public void setOnTime(boolean onTime) {

        this.onTime = onTime;
    }

    private List<JobStepTimePlan> jobStepTimePlan;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public void setWaitRoom(String waitRoom) {
        this.waitRoom = waitRoom;
    }

    public String getWaitRoom() {
        return this.waitRoom;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainNumber() {
        return this.trainNumber;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    public String getStartStationName() {
        return this.startStationName;
    }

    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }

    public String getEndStationName() {
        return this.endStationName;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getArriveTime() {
        return this.arriveTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    public String getDepartTime() {
        return this.departTime;
    }

    public void setCheckPort(String checkPort) {
        this.checkPort = checkPort;
    }

    public String getCheckPort() {
        return this.checkPort;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setCurrentTrainStatus(String currentTrainStatus) {
        this.currentTrainStatus = currentTrainStatus;
    }

    public String getCurrentTrainStatus() {
        return this.currentTrainStatus;
    }

    public void setJobStepTimePlan(List<JobStepTimePlan> jobStepTimePlan) {
        this.jobStepTimePlan = jobStepTimePlan;
    }

    public List<JobStepTimePlan> getJobStepTimePlan() {
        return this.jobStepTimePlan;
    }

    public class JobStepTimePlan {
        private String startTime;

        private String jobStepId;

        private String jobStepName;

        private int ordinal;

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getStartTime() {
            return this.startTime;
        }

        public void setJobStepId(String jobStepId) {
            this.jobStepId = jobStepId;
        }

        public String getJobStepId() {
            return this.jobStepId;
        }

        public void setJobStepName(String jobStepName) {
            this.jobStepName = jobStepName;
        }

        public String getJobStepName() {
            return this.jobStepName;
        }

        public void setOrdinal(int ordinal) {
            this.ordinal = ordinal;
        }

        public int getOrdinal() {
            return this.ordinal;
        }

    }
}
