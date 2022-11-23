package com.kltn.lambdabuy.webcontroller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.kltn.SpringAPILambdaBuy.common.request.cart.CartDto;
import com.example.kltn.SpringAPILambdaBuy.common.request.checkout.CheckoutDto;
import com.example.kltn.SpringAPILambdaBuy.common.request.order.CreateOrderDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.OrderResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ProductResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ProfileResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.UserResponseDto;
import com.example.kltn.SpringAPILambdaBuy.entities.OrderDetailEntity;
import com.example.kltn.SpringAPILambdaBuy.entities.OrderEntity;
import com.example.kltn.SpringAPILambdaBuy.entities.ProductEntity;
import com.kltn.lambdabuy.service.CartItemService;
import com.kltn.lambdabuy.service.OrderDetailService;
import com.kltn.lambdabuy.service.OrderService;
import com.kltn.lambdabuy.service.PaymentService;
import com.kltn.lambdabuy.service.ProductService;
import com.kltn.lambdabuy.service.UserService;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;

@Controller
public class PaymentController extends HttpServlet {
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderDetailService orderDetailService;
	
	@Autowired
	private UserService userService;
	
	private static final long serialVersionUID = 1L;

	public PaymentController() {
	}

	@PostMapping("/authorize_payment")
	protected String authorizePayment(HttpServletRequest request) {
		String product = "";
		
		
		Collection<CartDto> carts = cartItemService.getAllItems();
		for (CartDto cart : carts) {
			product += cart.getName() + ", ";
		}
		double subTotal = cartItemService.getAmmount();
		double shipping = 5;
		double tax = 5;
		double total = subTotal + shipping + tax;
		
		String subTotalString = String.valueOf(subTotal);
		String shippingString = String.valueOf(shipping);
		String taxString = String.valueOf(tax);
		String totalString = String.valueOf(total);
		
		CheckoutDto checkout = new CheckoutDto(product.substring(0, product.length() -2), subTotalString, shippingString, taxString, totalString);

		try {
			String approvalLink = paymentService.authorizePayment(checkout);

			return "redirect:" + approvalLink;
			
		} catch (PayPalRESTException ex) {
			request.setAttribute("errorMessage", ex.getMessage());
			ex.printStackTrace();
			return "views/checkout/error";
		}
	}
	
	@GetMapping("/review_payment")
	protected String reviewPayment(HttpServletRequest request) {
		UserResponseDto user = userService.currentUser();
		request.setAttribute("user", user);
    	if(user != null) {
    		ProfileResponseDto profile = user.getProfile();
    		request.setAttribute("profile", profile);
    	}
    	
    	int inCart = cartItemService.getCount();
    	request.setAttribute("inCart", inCart);
    	
		String paymentId = request.getParameter("paymentId");
		String payerId = request.getParameter("PayerID");
		try {
			Payment payment = paymentService.getPaymentDetails(paymentId);
			
			PayerInfo payerInfo = payment.getPayer().getPayerInfo();
			Transaction transaction = payment.getTransactions().get(0);
			ShippingAddress shippingAddress = transaction.getItemList().getShippingAddress();
						
			request.setAttribute("payer", payerInfo);
			request.setAttribute("transaction", transaction);
			request.setAttribute("shippingAddress", shippingAddress);
			
			request.setAttribute("paymentId", paymentId);
			request.setAttribute("PayerID", payerId);
			
			
			String url = "views/payment/review";
			
			return url;
		} catch (PayPalRESTException ex) {
			request.setAttribute("errorMessage", ex.getMessage());
			ex.printStackTrace();
			return "views/checkout/error";
		}		
	}
	
	@PostMapping("/execute_payment")
	protected String executePayment(HttpServletRequest request) {
		UserResponseDto user = userService.currentUser();
		request.setAttribute("user", user);
    	if(user != null) {
    		ProfileResponseDto profile = user.getProfile();
    		request.setAttribute("profile", profile);
    	}
    	
    	int inCart = cartItemService.getCount();
    	request.setAttribute("inCart", inCart);
		
		String paymentId = request.getParameter("paymentId");
		String payerId = request.getParameter("PayerID");
		
		try {
			Payment payment = paymentService.executePayment(paymentId, payerId);
			
			PayerInfo payerInfo = payment.getPayer().getPayerInfo();
			Transaction transaction = payment.getTransactions().get(0);
			ShippingAddress shippingAddress = transaction.getItemList().getShippingAddress();
			
			//Create order
			CreateOrderDto createOrderDto = new CreateOrderDto();
			createOrderDto.setSubTotal(Double.parseDouble(transaction.getAmount().getDetails().getSubtotal()));
			createOrderDto.setShipping(Double.parseDouble(transaction.getAmount().getDetails().getShipping()));
			createOrderDto.setTax(Double.parseDouble(transaction.getAmount().getDetails().getTax()));
			createOrderDto.setTotal(Double.parseDouble(transaction.getAmount().getTotal()));
			
			OrderResponseDto createOrder = orderService.create(createOrderDto);
			
			//Create Order detail
			OrderEntity orderEntity = orderService.findOrderEntityById(createOrder.getId());
			
			String[] listProductName = transaction.getDescription().split(", ");
			List<ProductEntity> listProduct = new ArrayList<>();
			for (String name : listProductName) {
				CartDto cart = cartItemService.findByName(name);
				ProductEntity product = productService.findProductEntityById(cart.getProductId());
				if(product != null) {
					listProduct.add(product);
				}
			}
			
			for (ProductEntity product : listProduct) {
				OrderDetailEntity orderDetail = new OrderDetailEntity(product, orderEntity, transaction.getDescription(), payerInfo.getFirstName(), payerInfo.getLastName(), payerInfo.getEmail(), shippingAddress.getRecipientName(), shippingAddress.getLine1(), shippingAddress.getCity(), shippingAddress.getCountryCode(), Double.parseDouble(shippingAddress.getPostalCode()));
				orderDetailService.create(orderDetail);
			}
			
			request.setAttribute("payer", payerInfo);
			request.setAttribute("transaction", transaction);	
			
			return "views/payment/receipt";			
		} catch (PayPalRESTException ex) {
			request.setAttribute("errorMessage", ex.getMessage());
			ex.printStackTrace();
			return "views/checkout/error";
		}
	}
	
	
}
