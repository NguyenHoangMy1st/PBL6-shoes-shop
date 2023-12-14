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
		/**
		 * một người dùng tại các thời điểm khác nhau có thể ở các địa chỉ
		 * khác nhau:
		 * .....ví dụ: ngày 01/01/2023 ở Đà Nẵng, ngày 10/02/2023 ở Quảng Nam
		 * nhưng địa chỉ hiện tại thì chỉ có 1 -> chỉ có 1 địa chỉ giao hàng
		 * -> giải quyết vấn đề là thêm trường current_time vào Address
		 * 
		 * các trường hợp có thể xảy ra:
		 * 1, người dùng mới chưa mua đặt hàng lần nào nên chưa có địa chỉ
		 * hiện tại sinh sống (và địa chỉ giao hàng)
		 * -> 1.1 tạo Order mới và thêm mới địa chỉ giao hàng hiện tại
		 * (reqShippingAddress)
		 * -> 1.2 tạo mới địa chỉ trong Address
		 * 
		 * 2, người dùng đã đặt hàng lần trước (đã có địa chỉ hiện sinh
		 * sống hay địa chỉ giao hàng)
		 * ....2.1, người dùng đặt hàng với địa chỉ cũ đã đăng ký trước đó
		 * -> 2.1.1 giữ nguyên địa chỉ hiện tại sinh sống trong Address
		 * -> 2.1.2 tạo Order mới với địa chỉ giao hàng hiện tại
		 * (reqShippingAddress)
		 * ....2.2, người dùng đặt hàng với địa chỉ mới
		 * -> 2.2.1 thêm địa chỉ hiện tại sinh sống mới vào Address(List)
		 * -> 2.2.2 tạo Order mới với địa chỉ giao hàng hiện tại
		 * (reqShippingAddress)
		 * 
		 */
		Cart cart = cartService.findUserCart(user.getId());
		List<OrderItem> temporaryOrderItems = new ArrayList<>();

		List<Order> existingOrders = orderRepository.findByUser(user);
		if (existingOrders.isEmpty()) {
			// 1.1 Create a new Order with the current shipping address
			return createNewOrder(user, reqShippingAddress, cart, temporaryOrderItems);
		} else {
			// Check if the shipping address matches any existing address
			boolean isAddressMatching = user.getAddresses().stream()
					.anyMatch(address -> addressMatches(reqShippingAddress, address));

			if (isAddressMatching) {
				// 2.1.1 Keep the existing shipping address
				// 2.1.2 Create a new Order with the current shipping address
				return createNewOrder(user, reqShippingAddress, cart, temporaryOrderItems);
			} else {
				// 2.2.1 Add the new shipping address to the user's addresses
				Address newAddress = createNewAddress(user, reqShippingAddress);
				user.getAddresses().add(newAddress);
				userRepository.save(user);

				// 2.2.2 Create a new Order with the new shipping address
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

		Address shippingAddress = findOrCreateShippingAddress(user, reqShippingAddress);
		createdOrder.setShippingAddress(shippingAddress);

		Order savedOrder = orderRepository.save(createdOrder);

		// Update the OrderItem references
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

	private Address findOrCreateShippingAddress(User user, ShippingAddressRequest reqShippingAddress) {
		return user.getAddresses().stream()
				.filter(address -> addressMatches(reqShippingAddress, address))
				.findFirst()
				.orElseGet(() -> createNewAddress(user, reqShippingAddress));
	}

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
		newAddress.setCreationTime(LocalDateTime.now()); // Thêm currentTime

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
