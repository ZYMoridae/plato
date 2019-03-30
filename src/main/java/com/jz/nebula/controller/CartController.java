package com.jz.nebula.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jz.nebula.entity.Cart;
import com.jz.nebula.entity.Role;
import com.jz.nebula.service.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {
	@Autowired
	private CartService cartService;


	@GetMapping(value = "/user/{id}")
	@RolesAllowed({ Role.ROLE_ADMIN })
	public @ResponseBody Cart getCart(@PathVariable("id") long id) {
		return cartService.getCart(id);
	}
	
	@GetMapping(value = "/my")
	@RolesAllowed({ Role.ROLE_USER, Role.ROLE_VENDOR, Role.ROLE_ADMIN })
	public @ResponseBody Cart getMyCart() {
		return cartService.getMyCart();
	}
	
	@PostMapping(value="/finalise")
	@RolesAllowed({ Role.ROLE_USER, Role.ROLE_VENDOR, Role.ROLE_ADMIN })
	public @ResponseBody ResponseEntity<?> finalizeCart() {
		return ResponseEntity.ok().build();
	}
	
}