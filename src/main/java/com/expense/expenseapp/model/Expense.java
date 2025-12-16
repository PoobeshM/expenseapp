package com.expense.expenseapp.model;
import com.expense.expenseapp.model.model.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
@Entity
@Table(name = "expenses")
public class Expense extends BaseEntity {
    private String title;
    private String category;
    private Double amount;
    private String paymentMode; // CASH, CARD, UPI
    private String notes;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getPaymentMode() {
        return paymentMode;
    }
    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
