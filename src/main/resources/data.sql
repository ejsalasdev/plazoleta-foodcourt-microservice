-- Sample data for foodcourt-microservice (restaurants, categories, dishes)

-- Categories
INSERT INTO categories (id, name, description) VALUES (1, 'Burgers', 'All types of burgers') ON CONFLICT (id) DO NOTHING;
INSERT INTO categories (id, name, description) VALUES (2, 'Pizzas', 'Delicious pizzas') ON CONFLICT (id) DO NOTHING;
INSERT INTO categories (id, name, description) VALUES (3, 'Drinks', 'Beverages and soft drinks') ON CONFLICT (id) DO NOTHING;
INSERT INTO categories (id, name, description) VALUES (4, 'Desserts', 'Sweet desserts') ON CONFLICT (id) DO NOTHING;

-- Restaurants
INSERT INTO restaurants (id, name, nit, address, phone_number, url_logo, owner_id) VALUES (1, 'Burger House', '900123456', 'Main St 123', '+573001234567', 'https://cdn.example.com/logo/burgerhouse.png', 1) ON CONFLICT (id) DO NOTHING;
INSERT INTO restaurants (id, name, nit, address, phone_number, url_logo, owner_id) VALUES (2, 'Pizza World', '900654321', '2nd Ave 45', '+573002345678', 'https://cdn.example.com/logo/pizzaworld.png', 2) ON CONFLICT (id) DO NOTHING;

-- Dishes (10 examples, various categories/restaurants)
INSERT INTO dishes (id, name, price, description, url_image, category_id, restaurant_id, active) VALUES (1, 'Classic Burger', 15000, 'Juicy beef burger with cheddar cheese', 'https://cdn.example.com/img/burger1.jpg', 1, 1, true) ON CONFLICT (id) DO NOTHING;
INSERT INTO dishes (id, name, price, description, url_image, category_id, restaurant_id, active) VALUES (2, 'Double Burger', 18000, 'Double beef, double cheese', 'https://cdn.example.com/img/burger2.jpg', 1, 1, true) ON CONFLICT (id) DO NOTHING;
INSERT INTO dishes (id, name, price, description, url_image, category_id, restaurant_id, active) VALUES (3, 'Veggie Burger', 14000, 'Vegetarian burger with fresh veggies', 'https://cdn.example.com/img/burger3.jpg', 1, 1, true) ON CONFLICT (id) DO NOTHING;
INSERT INTO dishes (id, name, price, description, url_image, category_id, restaurant_id, active) VALUES (4, 'Pepperoni Pizza', 22000, 'Classic pepperoni pizza', 'https://cdn.example.com/img/pizza1.jpg', 2, 2, true) ON CONFLICT (id) DO NOTHING;
INSERT INTO dishes (id, name, price, description, url_image, category_id, restaurant_id, active) VALUES (5, 'Hawaiian Pizza', 21000, 'Pizza with ham and pineapple', 'https://cdn.example.com/img/pizza2.jpg', 2, 2, true) ON CONFLICT (id) DO NOTHING;
INSERT INTO dishes (id, name, price, description, url_image, category_id, restaurant_id, active) VALUES (6, 'Margherita Pizza', 20000, 'Classic margherita with mozzarella', 'https://cdn.example.com/img/pizza3.jpg', 2, 2, true) ON CONFLICT (id) DO NOTHING;
INSERT INTO dishes (id, name, price, description, url_image, category_id, restaurant_id, active) VALUES (7, 'Coke', 5000, 'Cold Coca-Cola', 'https://cdn.example.com/img/coke.jpg', 3, 1, true) ON CONFLICT (id) DO NOTHING;
INSERT INTO dishes (id, name, price, description, url_image, category_id, restaurant_id, active) VALUES (8, 'Lemonade', 4500, 'Fresh lemonade', 'https://cdn.example.com/img/lemonade.jpg', 3, 2, true) ON CONFLICT (id) DO NOTHING;
INSERT INTO dishes (id, name, price, description, url_image, category_id, restaurant_id, active) VALUES (9, 'Brownie', 7000, 'Chocolate brownie with ice cream', 'https://cdn.example.com/img/brownie.jpg', 4, 1, true) ON CONFLICT (id) DO NOTHING;
INSERT INTO dishes (id, name, price, description, url_image, category_id, restaurant_id, active) VALUES (10, 'Cheesecake', 7500, 'Classic cheesecake', 'https://cdn.example.com/img/cheesecake.jpg', 4, 1, true) ON CONFLICT (id) DO NOTHING;
