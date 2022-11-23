package com.kltn.lambdabuy.webcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kltn.lambdabuy.service.PaypalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@Controller
public class PaypalController {
//	@Autowired
//	private PaypalService service;
//
//	public static final String SUCCESS_URL = "pay/success";
//	public static final String CANCEL_URL = "pay/cancel";
//
//	@GetMapping("/")
//	public String home() {
//		return "views/payment/home";
//	}
//
////	@PostMapping("/pay")
////	public String payment(@ModelAttribute("order") OrderDetailDto order) {
////		try {
////			Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
////					order.getIntent(), order.getDescription(), "http://localhost:7070/" + CANCEL_URL,
////					"http://localhost:7070/" + SUCCESS_URL);
////			for (Links link : payment.getLinks()) {
////				if (link.getRel().equals("approval_url")) {
////					return "redirect:" + link.getHref();
////				}
////			}
////
////		} catch (PayPalRESTException e) {
////
////			e.printStackTrace();
////		}
////		return "redirect:/";
////	}
//
//	@GetMapping(value = CANCEL_URL)
//	public String cancelPay() {
//		return "views/payment/cancel";
//	}
//
//	@GetMapping(value = SUCCESS_URL)
//	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
//		try {
//			Payment payment = service.executePayment(paymentId, payerId);
//			System.out.println(payment.toJSON());
//			if (payment.getState().equals("approved")) {
//				return "views/payment/success";
//			}
//		} catch (PayPalRESTException e) {
//			System.out.println(e.getMessage());
//		}
//		return "redirect:/";
//	}
}
