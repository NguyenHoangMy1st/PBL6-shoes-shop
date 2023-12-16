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

		Cart cart = cartService.findUserCart(user.getId());
		List<OrderItem> temporaryOrderItems = new ArrayList<>();

		List<Order> existingOrders = orderRepository.findByUser(user);
		if (existingOrders.isEmpty()) {

			return createNewOrder(user, reqShippingAddress, cart, temporaryOrderItems);
		} else {

			boolean isAddressMatching = user.getAddresses().stream()
					.anyMatch(address -> addressMatches(reqShippingAddress, address));

			if (isAddressMatching) {

				return createNewOrder(user, reqShippingAddress, cart, temporaryOrderItems);
			} else {

				Address newAddress = createNewAddress(user, reqShippingAddress);
				user.getAddresses().add(newAddress);
				userRepository.save(user);

				return createNewOrder(user, reqShippingAddress, cart, temporaryOrderItems);
			}
		}
	}

	private Order createNewOrder(User user, ShippingAddressRequest reqShippingAddress,
			Cart cart, List<OrderItem> temporaryOrderItems) {
		for (CartItem item : cart.getCartItems()) {
			OrderItem orderItem = createOrderItem(item);
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
		createdOrder.setOrderItems(temporaryOrderItems);
		createdOrder.setUser(user);

		Address shippingAddress = createNewAddress(user, reqShippingAddress);

		createdOrder.setShippingAddress(shippingAddress);

		Order savedOrder = orderRepository.save(createdOrder);

		temporaryOrderItems.forEach(item -> item.setOrder(savedOrder));
		orderItemRepository.saveAll(temporaryOrderItems);

		orderRepository.save(savedOrder);
		return savedOrder;
	}

	private OrderItem createOrderItem(CartItem cartItem) {
		OrderItem orderItem = new OrderItem();
		orderItem.setDeliveryDate(LocalDateTime.now().plusWeeks(1));
		orderItem.setDiscountedPrice(cartItem.getDiscountedPrice());
		orderItem.setPrice(cartItem.getPrice());
		orderItem.setQuantity(cartItem.getQuantity());
		orderItem.setSize(cartItem.getSize());
		orderItem.setUserId(cartItem.getUserId());
		orderItem.setProduct(cartItem.getProduct());
		return orderItem;
	}

	// private Address findOrCreateShippingAddress(User user, ShippingAddressRequest
	// reqShippingAddress) {
	// return user.getAddresses().stream()
	// .filter(address -> addressMatches(reqShippingAddress, address))
	// .findFirst()
	// .orElseGet(() -> createNewAddress(user, reqShippingAddress));
	// }

	private boolean addressMatches(ShippingAddressRequest reqShippingAddress, Address address) {
		return address.getStreetAddress().equals(reqShippingAddress.getStreetAddress())
				&& address.getCity().equals(reqShippingAddress.getCity())
				&& address.getState().equals(reqShippingAddress.getState())
				&& address.getZipCode().equals(reqShippingAddress.getZipCode());
	}

	private Address createNewAddress(User user, ShippingAddressRequest reqShippingAddress) {
		Address newAddress = new Address();
		newAddress.setStreetAddress(reqShippingAddress.getStreetAddress());
		newAddress.setCity(reqShippingAddress.getCity());
		newAddress.setState(reqShippingAddress.getState());
		newAddress.setZipCode(reqShippingAddress.getZipCode());
		newAddress.setCreationTime(LocalDateTime.now());
		newAddress.setUser(user);

		return addressRepository.save(newAddress);
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

				// Đảm bảo số lượng không âm
				if (updatedQuantity < 0) {
					try {
						throw new OrderException("Not enough quantity in size: ");
					} catch (OrderException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				size.setQuantity(updatedQuantity);

				// Cập nhật tổng số lượng của sản phẩm
				product.setQuantity(product.getQuantity() - quantity);

				// Lưu cập nhật vào cơ sở dữ liệu
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
