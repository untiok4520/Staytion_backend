package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "amount")
    private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "method", columnDefinition = "ENUM('CREDIT_CARD','CASH') DEFAULT 'CASH'")
    private PaymentMethod method = PaymentMethod.CASH;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('PAID','UNPAID','CANCELED') DEFAULT 'UNPAID'")
    private PaymentStatus status = PaymentStatus.UNPAID;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    
    public Payment() {}

    public Payment(BigDecimal amount, PaymentMethod method, PaymentStatus status, LocalDateTime createdAt) {
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.createdAt = createdAt;
    }
//	-------------------------------------
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;   
 
//	-------------------------------------
    public enum PaymentMethod {
        CREDIT_CARD,
        CASH
    }

    public enum PaymentStatus {
        PAID,
        UNPAID,
        CANCELED
    }

//	-------------------------------------
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public PaymentMethod getMethod() {
		return method;
	}

	public void setMethod(PaymentMethod method) {
		this.method = method;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
}
