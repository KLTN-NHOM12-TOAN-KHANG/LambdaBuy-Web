package com.kltn.lambdabuy.service;

import java.util.List;

import com.example.kltn.SpringAPILambdaBuy.common.request.checkout.CheckoutDto;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;

public interface PaymentService {

	Payment getPaymentDetails(String paymentId) throws PayPalRESTException;

	Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;

	String getApprovalLink(Payment approvedPayment);

	List<Transaction> getTransactionInformation(CheckoutDto checkout);

	RedirectUrls getRedirectURLs();

	Payer getPayerInformation();

	String authorizePayment(CheckoutDto checkout) throws PayPalRESTException;

}
