package com.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.model.entity.Users;
import com.model.logic.BookingManager;
import com.model.logic.CategoryManager;
import com.model.logic.CityManager;
import com.model.logic.ContactManager;
import com.model.logic.EventManager;
import com.model.logic.EventTypeManager;
import com.model.logic.NewsManager;
import com.model.logic.PayManager;
import com.model.logic.RolesManager;
import com.model.logic.SeatsManager;
import com.model.logic.UsersManager;
import com.model.logic.impl.BookingManagerImpl;
import com.model.logic.impl.CategoryManagerImpl;
import com.model.logic.impl.CityManagerImpl;
import com.model.logic.impl.ContactManagerImpl;
import com.model.logic.impl.EventManagerImpl;
import com.model.logic.impl.EventTypeManagerImpl;
import com.model.logic.impl.NewsManagerImpl;
import com.model.logic.impl.PayManagerImpl;
import com.model.logic.impl.RolesManagerImpl;
import com.model.logic.impl.SeatManagerImpl;
import com.model.logic.impl.UsersManagerImpl;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.process.StringFormat;
import com.model.entity.Booking;
import com.model.entity.Cart;
import com.model.entity.Category;
import com.model.entity.Contact;
import com.model.entity.Event;
import com.model.entity.EventType;
import com.model.entity.City;
import com.model.entity.News;
import com.model.entity.Payment;
import com.model.entity.Roles;
import com.model.entity.Seat;

public class HomeController extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String error;
	private String success;
	private String username;
	private String password;
	private String message;

	private int eventId;
	private Event event;
	private int seatId;
	private double discount;
	private double price;
	private double totalMoney;
	private int stt;
	private int number;
	private int payId;
	private int cateId;
	private int newId;

	private String email;
	private String address;
	private String birthday;
	private String fullName;
	private String phone;
	private String confirmPassword;

	private Contact contact;
	private Users user;
	private News newsItem;

	// manager
	private UsersManager userMng;
	private EventManager eventMng;
	private CityManager cityMng;
	private EventTypeManager typeMng;
	private SeatsManager seatMng;
	private RolesManager roleMng;
	private PayManager payMng;
	private BookingManager bookingMng;
	private ContactManager contMng;
	private NewsManager newMng;
	private CategoryManager cateMng;
	// search property
	private int type;
	private String search;
	private int p; // page number
	private int pcount; // number page
	private String cityId;
	private String orderBy;
	private int o; // orderby
	private int itemCount;
	private int eventCount;
	private int prevPage;
	private int nextPage;

	private List<Event> events;
	private List<EventType> types;
	private List<City> cities;
	private List<Seat> seats;
	private List<Cart> carts;
	private List<Category> categories;
	private List<News> news;
	private List<Booking> bookings;

	private List<Payment> pays;
	// site map
	private String actionName;
	private String actionUrl;

	private HttpServletRequest servletRequest;
	private Map<String, Object> session;

	public HomeController() {
		userMng = new UsersManagerImpl();
		eventMng = new EventManagerImpl();
		cityMng = new CityManagerImpl();
		typeMng = new EventTypeManagerImpl();
		types = typeMng.getEventTypes();
		seatMng = new SeatManagerImpl();
		roleMng = new RolesManagerImpl();
		payMng = new PayManagerImpl();
		setBookingMng(new BookingManagerImpl());
		contMng = new ContactManagerImpl();
		newMng = new NewsManagerImpl();
		cateMng = new CategoryManagerImpl();
	}

	public String Login() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		if (session != null && session.get("user") != null) {
			return "success";
		}
		if (username != null && password != null) {
			Users user = userMng.getUser(username,
					StringFormat.encryptMD5(password));
			if (user != null) {
				session.put("user", user);
				session.put("viewtime", new Date());
				return "success";
			} else
				error = "Username or password is invalid!";
		}
		return "input";
	}

	public boolean validateRegister() {
		boolean flag = true;
		if (username == null || password == null || address == null
				|| birthday == null || email == null || fullName == null
				|| phone == null || confirmPassword == null) {
			flag = false;
			error = "fill all blank text box";
		}
		if (!password.equals(confirmPassword)) {
			flag = false;
			error = "confirm password mismatch password";
		}
		return flag;
	}

	public String register() {
		if (validateRegister()) {

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Users u = new Users();
			u.setAddress(address);
			try {
				u.setBirthDay(sdf.parse(birthday));
			} catch (ParseException e) {
				error = "birthday format fail";
				return "input";
			}
			u.setCreateDate(new Date());
			u.setEmail(email);
			u.setFullName(StringFormat.SimpleFormat(fullName));
			u.setPassword(StringFormat.encryptMD5(password));
			u.setPhone(phone);
			Roles role = roleMng.getRole(2);
			u.setUserName(username);
			u.setRole(role);

			userMng.insert(u);
			setSuccess("register success, please login to system");
			return "input";
		}
		return "input";

	}

	public String Logout() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		if (session != null && session.get("user") != null) {
			session.remove("user");
			session.remove("viewtime");
		}
		return "success";
	}

	public String index() {

		Map<String, Object> session = ActionContext.getContext().getSession();
		if (session != null && session.get("count") == null)
			session.put("count", 0);
		itemCount = 9;
		if (cityId == null)
			cityId = "";
		if (p == 0)
			p = 1;
		if (o == 2) {
			orderBy = "event.createTime asc";
		} else {
			o = 1;
			orderBy = "event.createTime desc";
		}
		events = eventMng.findRange(search, cityId, type, orderBy, (p - 1)
				* itemCount, itemCount);
		events = buildData(events);
		setCities(cityMng.getCities());

		List<Event> list = eventMng.getEvents();
		eventCount = list.size();
		if (eventCount > 0) {
			pcount = eventCount / itemCount;
			if(pcount ==0)
				 pcount = 1;
			else if (pcount % 2 != 0)
				pcount++;
		} else
			pcount = 1;

		return "success";
	}

	public String news() {
		if (p == 0)
			p = 1;
		itemCount = 10;
		categories = cateMng.getCategories();

		news = newMng.find(cateId, (p - 1) * itemCount, itemCount);

		itemCount = newMng.getNews().size();
		if (itemCount > 0) {
			pcount = eventCount / itemCount;
			if(pcount == 0)
				pcount =1;
			else if (pcount % 2 != 0)
				pcount++;
		} else
			pcount = 1;

		return "success";
	}

	public String newDetail() {
		categories = cateMng.getCategories();
		if (newId > 0) {
			newsItem = newMng.getNew(newId);
			news = newMng.find(cateId, 0, 5);
		}
		return "success";

	}

	public String about() {
		actionName = "About";
		actionUrl = "about.html";

		categories = cateMng.getCategories();
		return "success";
	}

	public String contact() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		Users u = (Users) session.get("user");
		System.out.println("Go contact pages");
		session = ActionContext.getContext().getSession();
		if (session != null && session.get("user") != null) {
			return "success";
		} else {
			return "input";
		}
	}

	public String sendMessage() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		Users u = (Users) session.get("user");
		Date currentTime = new Date();
		try {
			contact.setUser(u);
			contact.setSendTime(currentTime);
			contMng.insert(contact);
			message = "message's sended to admin";
			return "success";
		} catch (Exception e) {
			message = "Send message fail";
			return "input";
		}
	}

	public String setProfile() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		user = (Users) session.get("user");
		if (user != null)
			return "success";
		else
			return "input";
	}

	public String updateProfile() {
		try {
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			Users profile = (Users) session.get("user");
			user.setId(profile.getId());
			user.setUserName(profile.getUserName());
			user.setPassword(profile.getPassword());
			user.setBirthDay(profile.getBirthDay());
			user.setCreateDate(profile.getCreateDate());
			user.setRole(profile.getRole());
			userMng.update(user);
			session.put("user", user);
			return "success";
		} catch (Exception e) {
			return "input";
		}
	}

	private List<Event> buildData(List<Event> list) {
		for (Event item : list) {
			String starttime = new SimpleDateFormat("dd/MM/yyyy hh:mm")
					.format(item.getStartTime());
			String endtime = new SimpleDateFormat("dd/MM/yyyy hh:mm")
					.format(item.getEndTime());
			item.setStartTimeBuild(starttime);
			item.setEndTimeBuild(endtime);
			List<Seat> seats = seatMng.getSeatsByEvent(item.getId());
			if (seats.size() > 0)
				item.setDiscount(seats.get(0).getDiscount());
			else {
				item.setDiscount(0);
			}
			item.setDiscount(0);
		}
		return list;
	}

	private Event buildData(Event e) {
		String starttime = new SimpleDateFormat("dd/MM/yyyy hh:mm").format(e
				.getStartTime());
		String endtime = new SimpleDateFormat("dd/MM/yyyy hh:mm").format(e
				.getEndTime());
		e.setStartTimeBuild(starttime);
		e.setEndTimeBuild(endtime);
		List<Seat> seats = seatMng.getSeatsByEvent(e.getId());
		if (seats.size() > 0)
			e.setDiscount(seats.get(0).getDiscount());
		else {
			e.setDiscount(0);
		}
		e.setDiscount(0);

		return e;
	}

	public String event() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		if (session != null && session.get("count") == null)
			session.put("count", 0);
		itemCount = 9;
		if (cityId == null)
			cityId = "";
		if (p == 0)
			p = 1;
		if (o == 2) {
			orderBy = "event.createTime asc";
		} else {
			o = 1;
			orderBy = "event.createTime desc";
		}
		if (type > 0) {
			EventType t = typeMng.getEventType(type);
			actionName = t.getName();
			actionUrl = "event/" + t.getId();
		} else {
			actionName = "Event";
			actionUrl = "event.html";
		}
		events = eventMng.findRange(search, cityId, type, orderBy, (p - 1) * 9,
				itemCount);
		if (events != null)
			events = buildData(events);
		setCities(cityMng.getCities());

		List<Event> list = eventMng.getEvents();
		itemCount = list.size();
		pcount = itemCount / 9;
		if (pcount % 2 != 0)
			pcount++;
		pcount++;
		return "success";
	}

	public String detail() {
		if (eventId > 0) {
			event = eventMng.getEvent(eventId);
			seats = seatMng.getSeatsByEvent(eventId);
			event = buildData(event);
		}
		return "success";
	}

	public String getPriceSeat() {
		Seat s = seatMng.getById(seatId);
		price = s.getPrice();
		discount = price - price * s.getDiscount() / 100;
		return "success";
	}

	public String cart() {
		session = ActionContext.getContext().getSession();
		if (session != null && session.get("cart") != null) {
			carts = (List<Cart>) session.get("cart");
			session.put("count", carts.size());
			totalMoney = 0;
			for (Cart c : carts) {
				totalMoney += c.getMoney();
			}
			pays = payMng.getPayments();
		}
		return "success";
	}

	public String deleteCart() {
		if (isAuthorize()) {
			if (stt >= 1) {
				session = ActionContext.getContext().getSession();
				if (session != null && session.get("cart") != null) {
					carts = (List<Cart>) session.get("cart");

					carts.remove(stt - 1);
					session.put("cart", carts);

				}
			}
		}
		return "success";
	}

	public String updateCart() {
		try {
			session = ActionContext.getContext().getSession();
			if (session != null && session.get("cart") != null) {
				carts = (List<Cart>) session.get("cart");
				Cart c = carts.get(stt - 1);
				c.setNumber(number);
				carts.remove(stt - 1);
				carts.add(c);
				session.put("cart", carts);

			}
			return "success";
		} catch (Exception e) {
			return "success";
		}
	}

	public String pay() {
		if (isAuthorize()) {
			Payment p = payMng.getPayment(payId);

			session = ActionContext.getContext().getSession();
			Users u = (Users) session.get("user");
			List<Cart> carts = (List<Cart>) session.get("cart");
			for (Cart c : carts) {
				Booking b = new Booking();
				b.setUsers(u);
				b.setSeat(c.getSeat());
				b.setPayment(p);
				b.setPrice(c.getMoney());
				b.setCount(c.getNumber());
				bookingMng.insert(b);
			}
			return "success";
		}
		return "login";
		
	}

	public String booking() {
		if (isAuthorize()) {
			Cart cart = new Cart();
			if (seatId > 0) {
				Seat s = seatMng.getById(seatId);
				cart.setSeat(s);
				cart.setEvent(s.getEvent());
			} else if (eventId > 0) {
				Event e = eventMng.getEvent(eventId);
				cart.setEvent(e);
				cart.setSeat(seatMng.getSeatsByEvent(e.getId()).get(0));
			}
			cart.setNumber(1);

			session = ActionContext.getContext().getSession();
			if (session != null && session.get("cart") != null) {
				List<Cart> list = (List<Cart>) session.get("cart");
				for (Cart c : list) {
					if (c.getSeat().getSeat().equals(cart.getSeat().getSeat())) {
						c.setNumber(c.getNumber() + 1);
						session.put("cart", list);
						return "success";
					}
				}
				list.add(cart);
				session.put("cart", list);
				return "success";
			} else {
				List<Cart> list = new ArrayList<Cart>();
				list.add(cart);
				session.put("cart", list);
				return "success";
			}

		}
		return "login";
	}

	public String history()
	{
		if(isAuthorize())
		{
			session = ActionContext.getContext().getSession();
			Users u =(Users) session.get("user");
			setBookings(bookingMng.getBookingByUser(u.getId())); 
			return "success";
		}
		return "login";
	}
	
	private boolean isAuthorize() {
		session = ActionContext.getContext().getSession();
		if (session != null && session.get("user") != null) {
			return true;
		}
		return false;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public List<EventType> getTypes() {
		return types;
	}

	public void setTypes(List<EventType> types) {
		this.types = types;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public int getPcount() {
		return pcount;
	}

	public void setPcount(int pcount) {
		this.pcount = pcount;
	}

	public UsersManager getUserMng() {
		return userMng;
	}

	public void setUserMng(UsersManager userMng) {
		this.userMng = userMng;
	}

	public EventManager getEventMng() {
		return eventMng;
	}

	public void setEventMng(EventManager eventMng) {
		this.eventMng = eventMng;
	}

	public CityManager getCityMng() {
		return cityMng;
	}

	public void setCityMng(CityManager cityMng) {
		this.cityMng = cityMng;
	}

	public EventTypeManager getTypeMng() {
		return typeMng;
	}

	public void setTypeMng(EventTypeManager typeMng) {
		this.typeMng = typeMng;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy
	 *            the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public SeatsManager getSeatMng() {
		return seatMng;
	}

	public void setSeatMng(SeatsManager seatMng) {
		this.seatMng = seatMng;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public int getPrevPage() {
		if (p > 1)
			return p - 1;
		else
			return p;
	}

	public void setPrevPage(int prevPage) {
		this.prevPage = prevPage;
	}

	public int getNextPage() {
		if (p < pcount)
			return p + 1;
		else
			return p;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	/**
	 * @return the cityId
	 */
	public String getCityId() {
		return cityId;
	}

	/**
	 * @param cityId
	 *            the cityId to set
	 */
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public int getEventCount() {
		return eventCount;
	}

	public void setEventCount(int eventCount) {
		this.eventCount = eventCount;
	}

	public int getO() {
		return o;
	}

	public void setO(int o) {
		this.o = o;
	}

	/**
	 * @return the eventId
	 */
	public int getEventId() {
		return eventId;
	}

	/**
	 * @param eventId
	 *            the eventId to set
	 */
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public int getSeatId() {
		return seatId;
	}

	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<Cart> getCarts() {
		return carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}

	public List<Payment> getPays() {
		return pays;
	}

	public void setPays(List<Payment> pays) {
		this.pays = pays;
	}

	public PayManager getPayMng() {
		return payMng;
	}

	public void setPayMng(PayManager payMng) {
		this.payMng = payMng;
	}

	public double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getPayId() {
		return payId;
	}

	public void setPayId(int payId) {
		this.payId = payId;
	}

	/**
	 * @return the stt
	 */
	public int getStt() {
		return stt;
	}

	/**
	 * @param stt
	 *            the stt to set
	 */
	public void setStt(int stt) {
		this.stt = stt;
	}

	/**
	 * @return the roleMng
	 */
	public RolesManager getRoleMng() {
		return roleMng;
	}

	/**
	 * @param roleMng
	 *            the roleMng to set
	 */
	public void setRoleMng(RolesManager roleMng) {
		this.roleMng = roleMng;
	}

	public BookingManager getBookingMng() {
		return bookingMng;
	}

	public void setBookingMng(BookingManager bookingMng) {
		this.bookingMng = bookingMng;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public ContactManager getContMng() {
		return contMng;
	}

	public void setContMng(ContactManager contMng) {
		this.contMng = contMng;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<News> getNews() {
		return news;
	}

	public void setNews(List<News> news) {
		this.news = news;
	}

	public int getNewId() {
		return newId;
	}

	public void setNewId(int newId) {
		this.newId = newId;
	}

	public News getNewsItem() {
		return newsItem;
	}

	public void setNewsItem(News newsItem) {
		this.newsItem = newsItem;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

}
