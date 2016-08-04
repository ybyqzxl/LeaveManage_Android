package panda.li.leavemanage.entity;

import java.util.List;

/**
 * Created by xueli on 2016/7/21.
 */
public class Test {


    /**
     * id : 1
     * timeStamp : 1468845488057
     * state : 0
     * historyId : 6
     * userCode : 03029
     * userName : 董永峰
     * userDepart : 1
     * leaveWhy : 1
     * startTime : 5
     * endTime : 9
     * leaveInfo : 0
     * approval : 2
     * isSucceed : 0
     */

    private List<LeaveInfosBean> leaveInfos;

    public List<LeaveInfosBean> getLeaveInfos() {
        return leaveInfos;
    }

    public void setLeaveInfos(List<LeaveInfosBean> leaveInfos) {
        this.leaveInfos = leaveInfos;
    }

    public static class LeaveInfosBean {
        private int id;
        private long timeStamp;
        private int state;
        private int historyId;
        private String userCode;
        private String userName;
        private String userDepart;
        private String leaveWhy;
        private String startTime;
        private String endTime;
        private String leaveInfo;
        private int approval;
        private int isSucceed;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getHistoryId() {
            return historyId;
        }

        public void setHistoryId(int historyId) {
            this.historyId = historyId;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserDepart() {
            return userDepart;
        }

        public void setUserDepart(String userDepart) {
            this.userDepart = userDepart;
        }

        public String getLeaveWhy() {
            return leaveWhy;
        }

        public void setLeaveWhy(String leaveWhy) {
            this.leaveWhy = leaveWhy;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getLeaveInfo() {
            return leaveInfo;
        }

        public void setLeaveInfo(String leaveInfo) {
            this.leaveInfo = leaveInfo;
        }

        public int getApproval() {
            return approval;
        }

        public void setApproval(int approval) {
            this.approval = approval;
        }

        public int getIsSucceed() {
            return isSucceed;
        }

        public void setIsSucceed(int isSucceed) {
            this.isSucceed = isSucceed;
        }
    }
}
