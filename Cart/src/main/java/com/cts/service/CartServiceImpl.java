package com.cts.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cts.clients.CartClient;
import com.cts.dtos.CartDto;
import com.cts.dtos.CartItemRequestDto;
import com.cts.dtos.MenuItemDto;
import com.cts.entities.Cart;
import com.cts.entities.CartItem;
import com.cts.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {
	
	CartRepository cartRepository;
	CartClient cartClient;
	
	public CartServiceImpl(CartRepository cartRepository, CartClient cartClient) {
		super();
		this.cartRepository = cartRepository;
		this.cartClient = cartClient;
	}

	@Override
	public List<CartDto> getAllCarts() {
	    try {
	        List<Cart> cartEntities = cartRepository.findAll();
	        System.out.println("📦 [CartService] Found " + cartEntities.size() + " carts in database");

	        return cartEntities.stream().map(cart -> {
	            CartDto dto = new CartDto();
	            dto.setCartId(cart.getCartId());
	            dto.setTotalCartPrice(cart.getTotalCartPrice());

	            if (cart.getItems() != null && !cart.getItems().isEmpty()) {
	                List<MenuItemDto> items = cart.getItems().stream().map(item -> {
	                    MenuItemDto menuItem = new MenuItemDto();
	                    menuItem.setItemId(item.getItemId());
	                    menuItem.setRestaurantId(item.getRestaurantId());
	                    menuItem.setCategoryId(item.getCategoryId());
	                    menuItem.setItemName(item.getItemName());
	                    menuItem.setDescription(item.getDescription());
	                    menuItem.setPrice(item.getPrice());
	                    menuItem.setIsavailable(item.getIsavailable());
	                    menuItem.setTotalItemPrice(item.getTotalItemPrice());
	                    menuItem.setQuantity(item.getQuantity());
	                    return menuItem;
	                }).collect(Collectors.toList());

	                dto.setItems(items);
	                
	                // Restore itemId and quantity at top level safely
	                CartItem firstItem = cart.getItems().get(0);
	                if (firstItem.getItemId() != null) {
	                    dto.setItemId(firstItem.getItemId().intValue());
	                }
	                dto.setQuantity(firstItem.getQuantity());
	            }

	            return dto;
	        }).collect(Collectors.toList());
	    } catch (Exception e) {
	        System.out.println("❌ [CartService] Error fetching all carts: " + e.getMessage());
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	
	@Override
	public CartDto getCartById(int cartId) {
	    try {
	        Optional<Cart> cartOpt = cartRepository.findById(cartId);
	        if (cartOpt.isEmpty()) {
	            System.out.println("⚠️ [CartService] Cart not found with ID: " + cartId);
	            return null;
	        }

	        Cart cart = cartOpt.get();
	        CartDto dto = new CartDto();
	        dto.setCartId(cart.getCartId());
	        dto.setTotalCartPrice(cart.getTotalCartPrice());

	        if (cart.getItems() != null && !cart.getItems().isEmpty()) {
	            List<MenuItemDto> items = cart.getItems().stream().map(item -> {
	                MenuItemDto menuItem = new MenuItemDto();
	                menuItem.setItemId(item.getItemId());
	                menuItem.setRestaurantId(item.getRestaurantId());
	                menuItem.setCategoryId(item.getCategoryId());
	                menuItem.setItemName(item.getItemName());
	                menuItem.setDescription(item.getDescription());
	                menuItem.setPrice(item.getPrice());
	                menuItem.setIsavailable(item.getIsavailable());
	                menuItem.setTotalItemPrice(item.getTotalItemPrice());
	                menuItem.setQuantity(item.getQuantity());
	                return menuItem;
	            }).collect(Collectors.toList());

	            dto.setItems(items);

	            // Optionally set itemId and quantity from the first item
	            CartItem firstItem = cart.getItems().get(0);
	            if (firstItem.getItemId() != null) {
	                dto.setItemId(firstItem.getItemId().intValue());
	            }
	            dto.setQuantity(firstItem.getQuantity());
	        }

	        return dto;
	    } catch (Exception e) {
	        System.out.println("❌ [CartService] Error fetching cart by ID " + cartId + ": " + e.getMessage());
	        e.printStackTrace();
	        return null;
	    }
	}



//	@Override
//	public CartDto addCart(CartDto cartDto) {
//	    List<CartItemRequestDto> inputItems = cartDto.getCartItems();
//
//	    if (inputItems == null || inputItems.isEmpty()) {
//	        throw new RuntimeException("Cart must contain at least one item.");
//	    }
//
//	    Cart cart = new Cart();
//	    List<CartItem> cartItemList = new ArrayList<>();
//	    double totalCartPrice = 0.0;
//
//	    List<MenuItemDto> enrichedItems = new ArrayList<>();
//
//	    for (CartItemRequestDto inputItem : inputItems) {
//	        Long itemId = inputItem.getItemId();
//	        int quantity = inputItem.getQuantity();
//
//	        MenuItemDto menuItem = cartClient.getMenuItemById(itemId);
//
//	        if (menuItem == null) {
//	            throw new RuntimeException("Menu item not found for ID: " + itemId);
//	        }
//
//	        double totalItemPrice = menuItem.getPrice() * quantity;
//
//	        CartItem item = new CartItem();
//	        item.setItemId(menuItem.getItemId());
//	        item.setCategoryId(menuItem.getCategoryId());
//	        item.setRestaurantId(menuItem.getRestaurantId());
//	        item.setName(menuItem.getName());
//	        item.setDescription(menuItem.getDescription());
//	        item.setPrice(menuItem.getPrice());
//	        item.setIsavailable(menuItem.getIsavailable());
//	        item.setQuantity(quantity);
//	        item.setTotalItemPrice(totalItemPrice);
//	        item.setCart(cart);
//
//	        cartItemList.add(item);
//	        totalCartPrice += totalItemPrice;
//
//	        // Enrich response
//	        menuItem.setTotalItemPrice(totalItemPrice);
//	        enrichedItems.add(menuItem);
//	    }
//
//	    cart.setItems(cartItemList);
//	    cart.setTotalCartPrice(totalCartPrice);
//
//	    Cart savedCart = cartRepository.save(cart);
//
//	    CartDto response = new CartDto();
//	    response.setCartId(savedCart.getCartId());
//	    response.setItems(enrichedItems);
//	    response.setTotalCartPrice(savedCart.getTotalCartPrice());
//
//	    return response;
//	}
	

	@Override
	public CartDto addCart(CartDto cartDto) {
	    List<CartItemRequestDto> inputItems = cartDto.getCartItems();
	
	    if (inputItems == null || inputItems.isEmpty()) {
	        throw new RuntimeException("Cart must contain at least one item.");
	    }
	
	    System.out.println("🔄 [CartService] Processing cart with " + inputItems.size() + " items");
	    System.out.println("📌 [CartService] Customer ID: " + cartDto.getCustomerId() + ", Restaurant ID: " + cartDto.getRestaurantId());
	    
	    Cart cart = new Cart();
	    cart.setCustomerId(cartDto.getCustomerId());
	    cart.setRestaurantId(cartDto.getRestaurantId());
	    
	    List<CartItem> cartItemList = new ArrayList<>();
	    double totalCartPrice = cartDto.getTotalCartPrice(); // Use the totalCartPrice from frontend
	    List<MenuItemDto> enrichedItems = new ArrayList<>();

	    for (CartItemRequestDto inputItem : inputItems) {
	        Long itemId = inputItem.getItemId();
	        int quantity = inputItem.getQuantity();
	        
	        System.out.println("🔄 [CartService] Processing item - itemId: " + itemId + ", quantity: " + quantity);

	        MenuItemDto menuItem = null;
	        
	        try {
	            // Try to fetch from Menu Service
	            menuItem = cartClient.getById(itemId);
	            System.out.println("✅ [CartService] Fetched menu item from service: " + (menuItem != null ? menuItem.getItemName() : "NULL"));
	        } catch (Exception e) {
	            System.out.println("⚠️ [CartService] Failed to fetch menu item from service: " + e.getMessage());
	            // If fetching fails, continue with basic item data
	            menuItem = null;
	        }

	        CartItem item = new CartItem();
	        item.setItemId(itemId);
	        item.setQuantity(quantity);
	        item.setCart(cart);
	        
	        // If we got menu item details from service, use them
	        if (menuItem != null) {
	            item.setCategoryId(menuItem.getCategoryId());
	            item.setRestaurantId(menuItem.getRestaurantId());
	            item.setItemName(menuItem.getItemName());
	            item.setDescription(menuItem.getDescription());
	            item.setPrice(menuItem.getPrice());
	            item.setIsavailable(menuItem.getIsavailable());
	            
	            double totalItemPrice = menuItem.getPrice() * quantity;
	            item.setTotalItemPrice(totalItemPrice);
	            
	            menuItem.setTotalItemPrice(totalItemPrice);
	            enrichedItems.add(menuItem);
	            
	            System.out.println("✅ [CartService] CartItem populated with full details: " + menuItem.getItemName());
	        } else {
	            // If we couldn't fetch details, still save the cart with itemId and quantity
	            // The item will be incomplete but the cart will be created
	            item.setTotalItemPrice(0.0); // Will be calculated later or from frontend data
	            System.out.println("⚠️ [CartService] CartItem saved with minimal data (itemId and quantity only)");
	        }

	        cartItemList.add(item);
	    }
	
	    cart.setItems(cartItemList);
	    cart.setTotalCartPrice(totalCartPrice);

	    System.out.println("💾 [CartService] Saving cart with " + cartItemList.size() + " items to database, total: ₹" + totalCartPrice);
	    
	    Cart savedCart = cartRepository.save(cart);
	    
	    System.out.println("✅ [CartService] Cart saved successfully with cartId: " + savedCart.getCartId() + ", customerId: " + savedCart.getCustomerId());

	    CartDto response = new CartDto();
	    response.setCartId(savedCart.getCartId());
	    response.setCustomerId(savedCart.getCustomerId());
	    response.setRestaurantId(savedCart.getRestaurantId());
	    response.setItems(enrichedItems);
	    response.setTotalCartPrice(savedCart.getTotalCartPrice());

	    return response;
	}@Override
	public CartDto updateCart(int cartId, CartDto cartDto) {
	    // Fetch the cart
	    Cart cart = cartRepository.findById(cartId)
	        .orElseThrow(() -> new RuntimeException("Cart not found with ID: " + cartId));

	    List<CartItemRequestDto> inputItems = cartDto.getCartItems();
	    if (inputItems == null || inputItems.isEmpty()) {
	        throw new RuntimeException("No items provided for update.");
	    }

	    List<CartItem> updatedItems = new ArrayList<>();
	    List<MenuItemDto> enrichedItems = new ArrayList<>();

	    for (CartItemRequestDto inputItem : inputItems) {
	        Long itemId = inputItem.getItemId();
	        int quantity = inputItem.getQuantity();

	        MenuItemDto menuItem = cartClient.getById(itemId);
	        if (menuItem == null) {
	            throw new RuntimeException("Menu item not found for ID: " + itemId);
	        }

	        Optional<CartItem> existingItemOpt = cart.getItems().stream()
	            .filter(i -> i.getItemId().equals(itemId))
	            .findFirst();

	        if (quantity == 0) {
	            // Remove item if quantity is 0
	            existingItemOpt.ifPresent(cart.getItems()::remove);
	            continue;
	        }

	        double totalItemPrice = menuItem.getPrice() * quantity;

	        if (existingItemOpt.isPresent()) {
	            // Update existing item
	            CartItem existingItem = existingItemOpt.get();
	            existingItem.setQuantity(quantity);
	            existingItem.setTotalItemPrice(totalItemPrice);
	            existingItem.setPrice(menuItem.getPrice());
	            existingItem.setItemName(menuItem.getItemName());
	            existingItem.setDescription(menuItem.getDescription());
	            existingItem.setIsavailable(menuItem.getIsavailable());
	            existingItem.setCategoryId(menuItem.getCategoryId());
	            existingItem.setRestaurantId(menuItem.getRestaurantId());
	            updatedItems.add(existingItem);
	        } else {
	            // Add new item
	            CartItem newItem = new CartItem();
	            newItem.setItemId(menuItem.getItemId());
	            newItem.setCategoryId(menuItem.getCategoryId());
	            newItem.setRestaurantId(menuItem.getRestaurantId());
	            newItem.setItemName(menuItem.getItemName());
	            newItem.setDescription(menuItem.getDescription());
	            newItem.setPrice(menuItem.getPrice());
	            newItem.setIsavailable(menuItem.getIsavailable());
	            newItem.setQuantity(quantity);
	            newItem.setTotalItemPrice(totalItemPrice);
	            newItem.setCart(cart);
	            cart.getItems().add(newItem);
	            updatedItems.add(newItem);
	        }

	        menuItem.setTotalItemPrice(totalItemPrice);
	        enrichedItems.add(menuItem);
	    }

	    // Recalculate total cart price
	    double updatedTotal = cart.getItems().stream()
	        .mapToDouble(CartItem::getTotalItemPrice)
	        .sum();
	    cart.setTotalCartPrice(updatedTotal);

	    // Save cart
	    Cart savedCart = cartRepository.save(cart);

	    // Prepare response
	    CartDto response = new CartDto();
	    response.setCartId(savedCart.getCartId());
	    response.setItems(enrichedItems);
	    response.setTotalCartPrice(savedCart.getTotalCartPrice());

	    return response;
	}
	
	@Override
	public void deleteCart(int cartId) {
		cartRepository.deleteById(cartId);
		
	}
	
//	@Override
//	public void deleteItemFromCart(int cartId, int itemId) {
//	    Cart cart = cartRepository.findById(cartId)
//	        .orElseThrow(() -> new RuntimeException("Cart not found with ID: " + cartId));
//
//	    // Find the item to remove
//	    CartItem itemToRemove = null;
//	    for (CartItem item : cart.getItems()) {
//	        if (item.getItemId().equals((long) itemId)) {
//	            itemToRemove = item;
//	            break;
//	        }
//	    }
//
//	    if (itemToRemove != null) {
//	        cart.getItems().remove(itemToRemove);
//	    }
//
//	    // Recalculate total cart price
//	    double updatedTotal = cart.getItems().stream()
//	        .mapToDouble(CartItem::getTotalItemPrice)
//	        .sum();
//
//	    cart.setTotalCartPrice(updatedTotal);
//
//	    // Save updated cart
//	    cartRepository.save(cart);
//	}
}
