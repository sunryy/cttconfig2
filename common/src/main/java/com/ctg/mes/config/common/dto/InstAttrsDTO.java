package com.ctg.mes.config.common.dto;

/**
 * @author xiongzy
 */
public class InstAttrsDTO {
    private String regionId;
    private Integer billMode;
    private String engineVersion;
    private String instanceName;
    private String diskType;
    private String diskSpace;
    private String secgroups;
    private String subnetId;
    private Integer cycleCnt;
    private String vpcId;
    private Integer orderNum;
    private String cycleType;
    private String outerSpuInstId;
    private String virtualRouterId;

    private String expireDate;

    private String securityGroupName;
    private String subnetName;

    public String getSecurityGroupName() {
        return securityGroupName;
    }

    public void setSecurityGroupName(String securityGroupName) {
        this.securityGroupName = securityGroupName;
    }

    public String getSubnetName() {
        return subnetName;
    }

    public void setSubnetName(String subnetName) {
        this.subnetName = subnetName;
    }

    public String getHostNum() {
        return hostNum;
    }

    public void setHostNum(String hostNum) {
        this.hostNum = hostNum;
    }

    public String getVpcName() {
        return vpcName;
    }

    public void setVpcName(String vpcName) {
        this.vpcName = vpcName;
    }

    public String getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(String memorySize) {
        this.memorySize = memorySize;
    }

    public String getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(String cpuCores) {
        this.cpuCores = cpuCores;
    }

    public String getDiskSpace() {
        return diskSpace;
    }

    public void setDiskSpace(String diskSpace) {
        this.diskSpace = diskSpace;
    }

    private String hostNum;
    private String vpcName;
    private String memorySize;
    private String cpuCores;


    @Override
    public String toString() {
        return "InstAttrsDTO{" +
                "regionId='" + regionId + '\'' +
                ", billMode=" + billMode +
                ", engineVersion='" + engineVersion + '\'' +
                ", instanceName='" + instanceName + '\'' +
                ", diskType=" + diskType +
                ", secgroups='" + secgroups + '\'' +
                ", subnetId='" + subnetId + '\'' +
                ", cycleCnt=" + cycleCnt +
                ", vpcId='" + vpcId + '\'' +
                ", orderNum=" + orderNum +
                ", cycleType='" + cycleType + '\'' +
                '}';
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public Integer getBillMode() {
        return billMode;
    }

    public void setBillMode(Integer billMode) {
        this.billMode = billMode;
    }

    public String getEngineVersion() {
        return engineVersion;
    }

    public void setEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }


    public String getDiskType() {
        return diskType;
    }

    public void setDiskType(String diskType) {
        this.diskType = diskType;
    }


    public String getSecgroups() {
        return secgroups;
    }

    public void setSecgroups(String secgroups) {
        this.secgroups = secgroups;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    public Integer getCycleCnt() {
        return cycleCnt;
    }

    public void setCycleCnt(Integer cycleCnt) {
        this.cycleCnt = cycleCnt;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public String getOuterSpuInstId() {
        return outerSpuInstId;
    }

    public void setOuterSpuInstId(String outerSpuInstId) {
        this.outerSpuInstId = outerSpuInstId;
    }

    public String getVirtualRouterId() {
        return virtualRouterId;
    }

    public void setVirtualRouterId(String virtualRouterId) {
        this.virtualRouterId = virtualRouterId;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
