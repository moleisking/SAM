package me.minitrabajo.controller;

/**
 * Created by Scott on 14/10/2016.
 */
public interface ResponsePay
{
    void paymentSuccess(Double value);
    void paymentFail();
}
