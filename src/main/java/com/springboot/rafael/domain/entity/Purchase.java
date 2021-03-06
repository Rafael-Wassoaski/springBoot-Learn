package com.springboot.rafael.domain.entity;

import javax.persistence.*;
import javax.persistence.Id;

import com.springboot.rafael.domain.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "CLIENT_ID")
    private Client client;
    
    @Column
    private LocalDate datePurchase;
    
    @Column(precision = 20, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "purchaseId")
    private List<ItemPurchase> item;
    
    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    public Integer getId() {
        return id;
    }
    
    public void setStatus(StatusPedido status) {
    	this.status = status;
    }
    
    public StatusPedido getStatus() {
    	return this.status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDate getDatePurchase() {
        return datePurchase;
    }

    public void setDatePurchase(LocalDate datePurchase) {
        this.datePurchase = datePurchase;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<ItemPurchase> getItemPurchases() {
        return item;
    }

    public void setItemPurchases(List<ItemPurchase> itemPurchases) {
        this.item = itemPurchases;
    }

}
