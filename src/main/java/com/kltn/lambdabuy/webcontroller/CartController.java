package com.kltn.lambdabuy.webcontroller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.kltn.SpringAPILambdaBuy.common.request.cart.CartDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ProductResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.ProfileResponseDto;
import com.example.kltn.SpringAPILambdaBuy.common.response.UserResponseDto;
import com.kltn.lambdabuy.service.CartItemService;
import com.kltn.lambdabuy.service.ProductService;
import com.kltn.lambdabuy.service.UserService;

@Controller
@RequestMapping
public class CartController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/shopping-cart/views")
	public String viewCart(Model model) {
		UserResponseDto user = userService.currentUser();
		if(user != null) {
			ProfileResponseDto profile = user.getProfile();
			model.addAttribute("profile", profile);
		}
		model.addAttribute("user", user);
		model.addAttribute("CART_ITEMS", cartItemService.getAllItems());
		model.addAttribute("TOTAL", cartItemService.getAmmount());
		int inCart = cartItemService.getCount();
    	model.addAttribute("inCart", inCart);
    	
		return "views/cart/view";
	}
	
	@GetMapping("/shopping-cart/add/{id}")
	public String addCart(@PathVariable("id") String id) {
		ProductResponseDto product = productService.findById(id);
		if(product != null) {
			CartDto item = new CartDto();
			item.setProductId(product.getId());
			item.setName(product.getName());
			item.setPrice(product.getUnitPrice());
			item.setQuantity(1);
			cartItemService.add(item);
		}
		return "redirect:/shop";
	}
	
	@GetMapping("/shopping-cart/clear")
	public String clearCart() {
		cartItemService.clear();
		return "redirect:/shopping-cart/views";
	}
	
	@GetMapping("/shopping-cart/del/{id}")
	public String removeCart(@PathVariable("id") String id) {
		cartItemService.remove(id);
		return "redirect:/shopping-cart/views";
	}
	
	@PostMapping("/shopping-cart/update")
	public String update(@RequestParam("productId") String productId, @RequestParam("quantity") int quantity) {
		cartItemService.update(productId, quantity);
		return "redirect:/shopping-cart/views";
	}
	
	@GetMapping("/authorize_payment/view")
	public String checkout(HttpServletRequest request) {
		double subTotal = cartItemService.getAmmount();
		double shipping = 5;
		double tax = 5;
		double total = subTotal + shipping + tax;

		Collection<CartDto> carts = cartItemService.getAllItems();
		
		
		request.setAttribute("carts", carts);
		request.setAttribute("subTotal", subTotal);
		request.setAttribute("shipping", shipping);
		request.setAttribute("tax", tax);
		request.setAttribute("total", total);
		
		UserResponseDto user = userService.currentUser();
		if(user != null) {
			ProfileResponseDto profile = user.getProfile();
			request.setAttribute("profile", profile);
		}
		request.setAttribute("user", user);
		
		return "views/checkout/checkout";
	}
//	@GetMapping("/")
//	public String viewCart(Model model) {
////		HashMap<String, Cart> cartItems = (HashMap<String, Cart>) session.getAttribute("myCartItem");
//		Cookie cookieCarts = cookieService.read("myCartItems");
//		if(cookieCarts != null) {
//			HashMap<String, Cart> cartItems = (HashMap<String, Cart>) JSONObject.stringToValue(cookieCarts.getValue());
//	    	if(cartItems == null) {
//	    		cartItems = new HashMap<>();
//	    	}
//	    	cookieService.create("myCartItems", cartItems.clone().toString(), 10);
//	    	cookieService.create("myCartTotal", JSONObject.valueToString(totalPrice(cartItems)), 10);
//	    	cookieService.create("myCartNum", JSONObject.valueToString(cartItems.size()), 10);
////	    	session.setAttribute("myCartItems", cartItems);
//	//
////			session.setAttribute("myCartTotal", totalPrice(cartItems));
////			session.setAttribute("myCartNum", cartItems.size());
//	    	model.addAttribute("myCartItems", cartItems);
//	    	model.addAttribute("myCartTotal", totalPrice(cartItems));
//	    	model.addAttribute("myCartItems", cartItems.size());
//	    	
//		} else {
//			cookieService.create("myCartItems", "", 10);
//			cookieService.create("myCartTotal", "", 10);
//	    	cookieService.create("myCartNum", "", 10);
//			model.addAttribute("myCartItems", new HashMap<>());
//	    	model.addAttribute("myCartTotal", 0);
//	    	model.addAttribute("myCartItems", 0);
//		}
//		return "views/cart/view";
//	}
//	
//    @GetMapping("/add/{productId}")
//    public String viewAdd(HttpSession session, @PathVariable("productId") String productId){
//    	HashMap<String, Cart> cartItems = (HashMap<String, Cart>) session.getAttribute("myCartItem");
//    	if(cartItems == null) {
//    		cartItems = new HashMap<>();
//    	}
//    	ProductResponseDto product = productService.findById(productId);
//		if(product != null) {
//			if(cartItems.containsKey(productId)) {
//			
//				Cart item = cartItems.get(product);
//				item.setProduct(product);
//				item.setQuantity(item.getQuantity() + 1);
//				cartItems.put(productId, item);
//			} else {
//				Cart item = new Cart();
//				item.setProduct(product);
//				item.setQuantity(1);
//				cartItems.put(productId, item);
//			}
//		}
//		session.setAttribute("myCartItems", cartItems);
//		session.setAttribute("myCartTotal", totalPrice(cartItems));
//		session.setAttribute("myCartNum", cartItems.size());
//
//    	return "redirect:/index";
//    }
//    
//    @GetMapping("/update/{productId}")
//    public String viewUpdate(Model model, HttpSession session, @PathVariable("productId") String productId) {
//    	HashMap<String, Cart> cartItems = (HashMap<String, Cart>) session.getAttribute("myCartItem");
//    	if(cartItems == null) {
//    		cartItems = new HashMap<>();
//    	}
//    	session.setAttribute("myCartItems", cartItems);
//    	return "views/cart/view";
//    }
//    
//    @GetMapping("/remove/{productId}")
//    public String viewRemove(Model model, HttpSession session, @PathVariable("productId") String productId) {
//    	HashMap<String, Cart> cartItems = (HashMap<String, Cart>) session.getAttribute("myCartItem");
//    	if(cartItems == null) {
//    		cartItems = new HashMap<>();
//    	}
//    	if(cartItems.containsKey(productId)) {
//    		cartItems.remove(productId);
//    	}
//    	session.setAttribute("myCartItems", cartItems);
//		session.setAttribute("myCartTotal", totalPrice(cartItems));
//		session.setAttribute("myCartNum", cartItems.size());
//
//    	return "views/cart/view";
//    }
//    
//    public double totalPrice(HashMap<String, Cart> cartItems) {
//    	int count = 0;
//    	for (Map.Entry<String, Cart> list : cartItems.entrySet()) {
//			count += list.getValue().getProduct().getUnitPrice() * list.getValue().getQuantity();
//		}
//    	return count;
//    }
}
