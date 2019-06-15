package com.demo.hotel_management.dto;

import com.demo.hotel_management.utils.custom_validators.ValidateRoommateAllowance;
import com.demo.hotel_management.utils.custom_validators.ValidateVacation;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@ValidateVacation
@ValidateRoommateAllowance
public class VacationDto {

    public VacationDto() {
    }

    private Long vacationId;
    @NotNull(message = "{validation.vacation.client.message}")
    private Long clientId;
    private String clientName;
    private String clientOtherInfo;
    private String clientPhoneNumber;
    @NotNull(message = "{validation.vacation.arrival_date.message}")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate arrivalDate;
    @NotNull
    private Integer arrivalDayPart;
    @NotNull(message = "{validation.vacation.leave_date.message}")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate leaveDate;
    @NotNull
    private Integer leaveDayPart;
    @NotEmpty(message = "{validation.vacation.rooms.message}")
    private Set<Integer> roomNumbers = new HashSet<>();

    private Integer residentsCount;
    @NotNull
    private Boolean hasSharedRooms;
    private Set<Integer> sharedRoomNumbers = new HashSet<>();
    @NotNull
    private Boolean inactive = false;
    @NotNull
    private Boolean approved = false;

    private Integer pricePerDay;
    @NotNull(message = "{validation.vacation.totalPrice.message}")
    private Integer totalPrice;

    private Boolean dummyVarForAlert;

    public Long getVacationId() {
        return vacationId;
    }

    public void setVacationId(Long vacationId) {
        this.vacationId = vacationId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientOtherInfo() {
        return clientOtherInfo;
    }

    public void setClientOtherInfo(String clientOtherInfo) {
        this.clientOtherInfo = clientOtherInfo;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Integer getArrivalDayPart() {
        return arrivalDayPart;
    }

    public void setArrivalDayPart(Integer arrivalDayPart) {
        this.arrivalDayPart = arrivalDayPart;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Integer getLeaveDayPart() {
        return leaveDayPart;
    }

    public void setLeaveDayPart(Integer leaveDayPart) {
        this.leaveDayPart = leaveDayPart;
    }

    public Set<Integer> getRoomNumbers() {
        return roomNumbers;
    }

    public void setRoomNumbers(Set<Integer> roomNumbers) {
        this.roomNumbers = roomNumbers;
    }

    public Integer getResidentsCount() {
        return residentsCount;
    }

    public void setResidentsCount(Integer residentsCount) {
        this.residentsCount = residentsCount;
    }

    public Boolean getHasSharedRooms() {
        return hasSharedRooms;
    }

    public void setHasSharedRooms(Boolean hasSharedRooms) {
        this.hasSharedRooms = hasSharedRooms;
    }

    public Set<Integer> getSharedRoomNumbers() {
        return sharedRoomNumbers;
    }

    public void setSharedRoomNumbers(Set<Integer> sharedRoomNumbers) {
        this.sharedRoomNumbers = sharedRoomNumbers;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Integer getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Integer pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getDummyVarForAlert() {
        return dummyVarForAlert;
    }

    public void setDummyVarForAlert(Boolean dummyVarForAlert) {
        this.dummyVarForAlert = dummyVarForAlert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VacationDto that = (VacationDto) o;
        return Objects.equals(vacationId, that.vacationId) &&
                Objects.equals(clientId, that.clientId) &&
                Objects.equals(clientName, that.clientName) &&
                Objects.equals(clientOtherInfo, that.clientOtherInfo) &&
                Objects.equals(clientPhoneNumber, that.clientPhoneNumber) &&
                Objects.equals(arrivalDate, that.arrivalDate) &&
                Objects.equals(arrivalDayPart, that.arrivalDayPart) &&
                Objects.equals(leaveDate, that.leaveDate) &&
                Objects.equals(leaveDayPart, that.leaveDayPart) &&
                Objects.equals(roomNumbers, that.roomNumbers) &&
                Objects.equals(residentsCount, that.residentsCount) &&
                Objects.equals(hasSharedRooms, that.hasSharedRooms) &&
                Objects.equals(sharedRoomNumbers, that.sharedRoomNumbers) &&
                Objects.equals(inactive, that.inactive) &&
                Objects.equals(approved, that.approved) &&
                Objects.equals(pricePerDay, that.pricePerDay) &&
                Objects.equals(totalPrice, that.totalPrice) &&
                Objects.equals(dummyVarForAlert, that.dummyVarForAlert);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vacationId, clientId, clientName, clientOtherInfo, clientPhoneNumber, arrivalDate, arrivalDayPart, leaveDate, leaveDayPart, roomNumbers, residentsCount, hasSharedRooms, sharedRoomNumbers, inactive, approved, pricePerDay, totalPrice, dummyVarForAlert);
    }

    @Override
    public String toString() {
        return "VacationDto{" +
                "vacationId=" + vacationId +
                ", clientId=" + clientId +
                ", clientName='" + clientName + '\'' +
                ", clientOtherInfo='" + clientOtherInfo + '\'' +
                ", clientPhoneNumber='" + clientPhoneNumber + '\'' +
                ", arrivalDate=" + arrivalDate +
                ", arrivalDayPart=" + arrivalDayPart +
                ", leaveDate=" + leaveDate +
                ", leaveDayPart=" + leaveDayPart +
                ", roomNumbers=" + roomNumbers +
                ", residentsCount=" + residentsCount +
                ", hasSharedRooms=" + hasSharedRooms +
                ", sharedRoomNumbers=" + sharedRoomNumbers +
                ", inactive=" + inactive +
                ", approved=" + approved +
                ", pricePerDay=" + pricePerDay +
                ", totalPrice=" + totalPrice +
                ", dummyVarForAlert=" + dummyVarForAlert +
                '}';
    }
}
