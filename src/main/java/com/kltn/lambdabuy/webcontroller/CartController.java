package com.kltn.lambdabuy.webcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.kltn.SpringAPILambdaBuy.common.request.cart.Cart;
import com.example.kltn.SpringAPILambdaBuy.common.response.ProductResponseDto;
import com.kltn.lambdabuy.service.CartItemService;
import com.kltn.lambdabuy.service.ProductService;
import com.kltn.lambdabuy.service.impl.CookieService;

@Controller
@RequestMapping("/shopping-cart")
public class CartController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@GetMapping("/views")
	public String viewCart(Model model) {
		model.addAttribute("CART_ITEMS", cartItemService.getAllItems());
		model.addAttribute("TOTAL", cartItemService.getAmmount());
		return "views/cart/view";
	}
	
	@GetMapping("/add/{id}")
	public String addCart(@PathVariable("id") String id) {
		ProductResponseDto product = productService.findById(id);
		if(product != null) {
			Cart item = new Cart();
			item.setProductId(product.getId());
			item.setName(product.getName());
			item.setPrice(product.getUnitPrice());
			item.setQuantity(1);
			cartItemService.add(item);
		}
		return "redirect:/shopping-cart/views";
	}
	
	@GetMapping("/clear")
	public String clearCart() {
		cartItemService.clear();
		return "redirect:/shopping-cart/views";
	}
	
	@GetMapping("/del/{id}")
	public String removeCart(@PathVariable("id") String id) {
		cartItemService.remove(id);
		return "redirect:/shopping-cart/views";
	}
	
	@PostMapping("/update")
	public String update(@RequestParam("productId") String productId, @RequestParam("quantity") int quantity) {
		cartItemService.update(productId, quantity);
		return "redirect:/shopping-cart/views";
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
