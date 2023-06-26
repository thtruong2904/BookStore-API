package com.tranhuutruong.BookStoreAPI.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tranhuutruong.BookStoreAPI.Model.User.UserInfoModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userinfo_id")
    private UserInfoModel userInfoModel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private StatusModel statusModel;

    @Column(name = "note")
    private String note;

    @Column(name = "feeShip")
    private Long feeShip;

    @Column(name = "date")
    private String date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receive_id")
    private ReceiveModel receiveModel;

    @Column(name = "address")
    private String address;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "orderModel")
    @JsonManagedReference
    private List<OrderDetailModel> orderDetailModels;

    public OrderModel copy() {
        OrderModel newOrder = new OrderModel();
        newOrder.userInfoModel = null;
        newOrder.setStatusModel(this.statusModel);
        newOrder.setNote(this.note);
        newOrder.setFeeShip(this.feeShip);
        newOrder.setDate(this.date);
        newOrder.setReceiveModel(this.receiveModel);
        newOrder.setOrderDetailModels(this.orderDetailModels);
        newOrder.setAddress(this.address);
        return newOrder;
    }

    public Integer getTotalPrices(){
        Double total= 0.0;
        if(orderDetailModels!=null)
            for (OrderDetailModel o: this.getOrderDetailModels() ) {
                total+=o.getPrice()*o.getAmount();
            }
        return total.intValue();
    }
}
