package com.dnanh01.backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dnanh01.backend.exception.OrderException;
import com.dnanh01.backend.model.Address;
import com.dnanh01.backend.model.Cart;
import com.dnanh01.backend.model.CartItem;
import com.dnanh01.backend.model.Order;
import com.dnanh01.backend.model.OrderItem;
import com.dnanh01.backend.model.Product;
import com.dnanh01.backend.model.Size;
import com.dnanh01.backend.model.User;
import com.dnanh01.backend.repository.AddressRepository;
import com.dnanh01.backend.repository.CartItemRepository;
import com.dnanh01.backend.repository.OrderItemRepository;
import com.dnanh01.backend.repository.OrderRepository;
import com.dnanh01.backend.repository.ProductRepository;
import com.dnanh01.backend.repository.UserRepository;
import com.dnanh01.backend.request.ShippingAddressRequest;

@Service
public class OrderServiceImplementation implements OrderService {

	private OrderRepository orderRepository;
	private CartService cartService;
	private AddressRepository addressRepository;
	private UserRepository userRepository;
	private OrderItemService orderItemService;
	private OrderItemRepository orderItemRepository;
	private ProductRepository productRepository;
	private CartItemRepository cartItemRepository;

	public OrderServiceImplementation(
			OrderRepository orderRepository,
			CartService cartService,
			AddressRepository addressRepository,
			UserRepository userRepository,
			OrderItemService orderItemService,
			OrderItemRepository orderItemRepository,
			ProductRepository productRepository,
			CartItemRepository cartItemRepository) {
		this.orderRepository = orderRepository;
		this.cartService = cartService;
		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
		this.orderItemService = orderItemService;
		this.orderItemRepository = orderItemRepository;
		this.productRepository = productRepository;
		this.cartItemRepository = cartItemRepository;
	}

	@Override
	public Order createOrder(User user, ShippingAddressRequest reqShippingAddress) {

		List<Order> existingOrders = orderRepository.findByUser(user);

		if (!existingOrders.isEmpty()) {
			return updateOrder(existingOrders.get(0), reqShippingAddress);
		}

		Address existingAddress = findExistingAddress(user, reqShippingAddress);
		Address address;

		if (existingAddress != null) {
			address = existingAddress;
		} else {
			address = createNewAddress(user, reqShippingAddress);
		}

		Cart cart = cartService.findUserCart(user.getId());

		List<OrderItem> temporaryOrderItems = new ArrayList<>(); // Use a temporary list

		for (CartItem item : cart.getCartItems()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setDeliveryDate(LocalDateTime.now().plusWeeks(1));
			orderItem.setDiscountedPrice(item.getDiscountedPrice());
			orderItem.setPrice(item.getPrice());
			orderItem.setQuantity(item.getQuantity());
			orderItem.setSize(item.getSize());
			orderItem.setUserId(item.getUserId());
			orderItem.setProduct(item.getProduct());

			OrderItem createdOrderItem = orderItemRepository.save(orderItem);
			temporaryOrderItems.add(createdOrderItem);
		}

		Order createdOrder = new Order();
		createdOrder.setCreateAt(LocalDateTime.now());
		createdOrder.setDeliveryDate(LocalDateTime.now().plusWeeks(1));
		createdOrder.setDiscount(cart.getDiscount());
		createdOrder.setOrderDate(LocalDateTime.now());
		createdOrder.setOrderStatus("PENDING");
		createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
		createdOrder.setTotalItem(cart.getTotalItem());
		createdOrder.setTotalPrice(cart.getTotalPrice());
		createdOrder.setShippingAddress(address);
		createdOrder.setUser(user);

		createdOrder.setOrderItems(temporaryOrderItems); // Set the list after iteration

		Order savedOrder = orderRepository.save(createdOrder);

		// Update the OrderItem references
		temporaryOrderItems.forEach(item -> item.setOrder(savedOrder));
		orderItemRepository.saveAll(temporaryOrderItems);

		return savedOrder;
	}

	private Order updateOrder(Order existingOrder, ShippingAddressRequest reqShippingAddress) {

		existingOrder.setShippingAddress(findExistingAddress(existingOrder.getUser(), reqShippingAddress));
		existingOrder.setOrderDate(LocalDateTime.now());
		existingOrder.setDeliveryDate(LocalDateTime.now().plusWeeks(1));
		existingOrder.setOrderStatus("PENDING");

		return orderRepository.save(existingOrder);
	}

	private Address createNewAddress(User user, ShippingAddressRequest reqShippingAddress) {
		Address saveAddress = new Address();
		saveAddress.setStreetAddress(reqShippingAddress.getStreetAddress());
		saveAddress.setCity(reqShippingAddress.getCity());
		saveAddress.setState(reqShippingAddress.getState());
		saveAddress.setZipCode(reqShippingAddress.getZipCode());
		saveAddress.setUser(user);

		Address address = addressRepository.save(saveAddress);

		user.getAddresses().add(address);
		userRepository.save(user);

		return address;
	}

	private Address findExistingAddress(User user, ShippingAddressRequest reqShippingAddress) {
		return user.getAddresses().stream()
				.filter(address -> address.getStreetAddress().equals(reqShippingAddress.getStreetAddress())
						&& address.getCity().equals(reqShippingAddress.getCity())
						&& address.getState().equals(reqShippingAddress.getState())
						&& address.getZipCode().equals(reqShippingAddress.getZipCode()))
				.findFirst()
				.orElse(null);
	}

	@Override
	public Order findOrderById(Long orderId) throws OrderException {
		Optional<Order> opt = orderRepository.findById(orderId);
		if (opt.isPresent()) {
			return opt.get();
		}
		throw new OrderException("Order not exist with id: " + orderId);
	}

	@Override
	public List<Order> usersOrderHistory(Long userId) {
		List<Order> orders = orderRepository.getUsersOrders(userId);
		return orders;
	}

	@Override
	public Order placedOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus("PLACED");

		return order;
	}

	@Override
	public Order confirmedOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus("CONFIRMED");
		return orderRepository.save(order);
	}

	@Override
	public Order shippedOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);

		if (!order.getOrderStatus().equals("CONFIRMED")) {
			throw new OrderException("Order must be in CONFIRMED state before shipping.");
		}

		order.setOrderStatus("SHIPPED");
		clearCartItems(order.getUser().getId());

		return orderRepository.save(order);
	}

	private void clearCartItems(Long userId) {

		List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

		cartItemRepository.deleteAll(cartItems);
	}

	@Override
	public Order deliveredOrder(Long orderId) throws OrderException {

		Order order = findOrderById(orderId);

		List<OrderItem> orderItems = order.getOrderItems();

		for (OrderItem item : orderItems) {
			int quantity = item.getQuantity();
			String sizeName = item.getSize();
			Long productId = item.getProduct().getId();
			Product product = productRepository.findById(productId)
					.orElseThrow(() -> new OrderException("Product not found"));

			Optional<Size> optionalSize = product.getSizes().stream()
					.filter(size -> size.getName().equals(sizeName))
					.findFirst();

			optionalSize.ifPresent(size -> {
				int updatedQuantity = size.getQuantity() - quantity;
				size.setQuantity(updatedQuantity);

				product.setQuantity(product.getQuantity() - quantity);

				productRepository.save(product);
			});
		}

		order.setOrderStatus("DELIVERED");

		return orderRepository.save(order);
	}

	@Override
	public Order canceledOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus("CANCELED");
		return orderRepository.save(order);
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public void deleteOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		orderRepository.delete(order);
	}

	// --------------------dashboard admin--------------------

}
