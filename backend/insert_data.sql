USE `pbl6_shoes_shop`;
INSERT INTO `pbl6_shoes_shop`.`user` (
        `id`,
        `create_at`,
        `email`,
        `first_name`,
        `last_name`,
        `mobile`,
        `password`,
        `role`
    )
VALUES (
        1,
        '2023-11-15 12:30:00',
        'admin1@email.com',
        'John',
        'Doe',
        '123456789',
        'hashed_password_1',
        'user'
    ),
    (
        2,
        '2023-11-15 12:45:00',
        'admin2@email.com',
        'Jane',
        'Doe',
        '987654321',
        'hashed_password_2',
        'admin'
    ),
    (
        3,
        '2023-11-15 13:00:00',
        'user1@email.com',
        'Bob',
        'Smith',
        '555555555',
        'hashed_password_3',
        'user'
    ),
    (
        4,
        '2023-11-15 14:30:00',
        'user2@email.com',
        'Alice',
        'Johnson',
        '111111111',
        'hashed_password_4',
        'user'
    ),
    (
        5,
        '2023-11-15 15:00:00',
        'user3@email.com',
        'Charlie',
        'Brown',
        '999999999',
        'hashed_password_5',
        'admin'
    );
INSERT INTO `pbl6_shoes_shop`.`address` (
        `id`,
        `city`,
        `mobile`,
        `state`,
        `street_address`,
        `zip_code`,
        `user_id`
    )
VALUES (
        1,
        'City1',
        '123456789',
        'State1',
        '123 Street',
        '12345',
        1001
    ),
    (
        2,
        'City2',
        '987654321',
        'State2',
        '456 Street',
        '67890',
        1002
    ),
    (
        3,
        'City3',
        '111222333',
        'State3',
        '789 Street',
        '98765',
        1003
    ),
    (
        4,
        'City4',
        '444555666',
        'State4',
        '4567 Street',
        '54321',
        1004
    ),
    (
        5,
        'City5',
        '555666777',
        'State5',
        '5678 Street',
        '67890',
        1005
    );
INSERT INTO `pbl6_shoes_shop`.`cart` (
        `discount`,
        `total_discounted_price`,
        `total_item`,
        `total_price`,
        `user_id`
    )
VALUES (10, 500, 3, 1500.50, 1),
    (5, 300, 2, 800.75, 2),
    (15, 750, 5, 2000.25, 3),
    (10, 700, 4, 1100.50, 4),
    (5, 600, 2, 800.75, 5);
INSERT INTO `pbl6_shoes_shop`.`category` (`id`, `level`, `name`, `parent_category_id`)
VALUES (1, 1, 'Giày nam', NULL);
INSERT INTO `pbl6_shoes_shop`.`category` (`id`, `level`, `name`, `parent_category_id`)
VALUES (2, 1, 'Giày nữ', NULL);
INSERT INTO `pbl6_shoes_shop`.`category` (`id`, `level`, `name`, `parent_category_id`)
VALUES (3, 2, 'Giày chạy', null);
INSERT INTO `pbl6_shoes_shop`.`product` (
        `id`,
        `brand`,
        `color`,
        `create_at`,
        `description`,
        `discount_persent`,
        `discounted_price`,
        `image_url`,
        `num_ratings`,
        `price`,
        `quantity`,
        `title`,
        `category_id`
    )
VALUES (
        1,
        'Nike',
        'Black',
        '2023-01-01 12:00:00',
        'High-quality sports shoes',
        10,
        90,
        'https://dl.dropboxusercontent.com/scl/fi/hg4g0nckvqzq1dn1k7d4r/shoes1.png?rlkey=0g7eiloof3dbzp5kealds6ngz&dl=0&fbclid=IwAR2my2NJrwlTdRm5L4S1CdEXq-7WXtJ8HRmPvjPny5_i7v294308VLRmLoM',
        50,
        100,
        20,
        'Nike Air Max',
        1
    ),
    (
        2,
        'Adidas',
        'White',
        '2023-01-02 14:30:00',
        'Comfortable casual shoes',
        15,
        80,
        'https://dl.dropboxusercontent.com/scl/fi/s4gtldvcycux8ew8ai7oi/shoes2.png?rlkey=7qx67asf6o6pjn12ez7h8kq5b&dl=0',
        30,
        120,
        15,
        'Adidas Originals',
        2
    ),
    (
        3,
        'Puma',
        'Blue',
        '2023-01-03 10:45:00',
        'Fashionable running shoes',
        20,
        70,
        'https://dl.dropboxusercontent.com/scl/fi/0bsy1tin277f2qu84j2gm/shoes3.png?rlkey=idjr9q2otcovji65rsm2tp8vx&dl=0',
        25,
        80,
        30,
        'Puma Ignite',
        1
    ),
    (
        4,
        'Reebok',
        'Red',
        '2023-01-04 08:15:00',
        'Lightweight athletic shoes',
        15,
        85,
        'https://dl.dropboxusercontent.com/scl/fi/h7tyuis2kv5ilvrt4kd9u/shoes4.png?rlkey=mqdjce3l2aao0beb0g6ly7ig8&dl=0',
        40,
        110,
        25,
        'Reebok Nano',
        2
    ),
    (
        5,
        'New Balance',
        'Gray',
        '2023-01-05 15:30:00',
        'Comfortable walking shoes',
        10,
        95,
        'https://dl.dropboxusercontent.com/scl/fi/0v5p7wmc7grdwooso36kc/shoes5.png?rlkey=q53yx0ul4qe6kjlxe7lbh0rqp&dl=0',
        35,
        90,
        22,
        'New Balance Fresh Foam',
        1
    ),
    (
        6,
        'Converse',
        'White',
        '2023-01-06 11:30:00',
        'Classic canvas sneakers',
        0,
        100,
        'https://dl.dropboxusercontent.com/scl/fi/lo9ufamiqemo01vakrbxl/shoes6.png?rlkey=x2b9ag7h0aculxqygamyo6hyt&dl=0',
        15,
        60,
        18,
        'Converse Chuck Taylor',
        3
    ),
    (
        7,
        'Vans',
        'Black',
        '2023-01-07 09:45:00',
        'Skateboarding-inspired shoes',
        25,
        75,
        'https://dl.dropboxusercontent.com/scl/fi/h2s8lsscd59by5ml1wkek/shoes7.png?rlkey=kiz7gcs4tb7recfvwchv2iti9&dl=0',
        20,
        70,
        28,
        'Vans Old Skool',
        3
    ),
    (
        8,
        'Under Armour',
        'Green',
        '2023-01-08 14:00:00',
        'Performance sports shoes',
        15,
        85,
        'https://dl.dropboxusercontent.com/scl/fi/dq4u2on2xbhdq557tzu99/shoes8.png?rlkey=r54nllj6ff44e1wws2xerbmc4&dl=0',
        30,
        120,
        24,
        'Under Armour HOVR',
        2
    );
INSERT INTO `pbl6_shoes_shop`.`cart_item` (
        discounted_price,
        price,
        quantity,
        size,
        user_id,
        cart_id,
        product_id
    )
VALUES (50, 100, 2, 'M', 1, 1, 101),
    (30, 75, 1, 'L', 2, 2, 102),
    (40, 90, 3, 'S', 1, 1, 103);
INSERT INTO `pbl6_shoes_shop`.`orders` (
        id,
        create_at,
        delivery_date,
        discount,
        order_date,
        order_id,
        order_status,
        total_discounted_price,
        total_item,
        total_price,
        shipping_address_id,
        user_id
    )
VALUES (
        1,
        '2023-11-15 12:00:00',
        '2023-11-20 12:00:00',
        10,
        '2023-11-15 12:00:00',
        'ORDER123',
        'PROCESSING',
        90,
        3,
        300.50,
        1,
        2
    ),
    (
        2,
        '2023-11-16 12:00:00',
        '2023-11-22 12:00:00',
        5,
        '2023-11-16 12:00:00',
        'ORDER456',
        'SHIPPED',
        80,
        2,
        150.25,
        2,
        3
    ),
    (
        3,
        '2023-11-17 12:00:00',
        '2023-11-24 12:00:00',
        15,
        '2023-11-17 12:00:00',
        'ORDER789',
        'DELIVERED',
        120,
        5,
        450.75,
        3,
        1
    );
INSERT INTO `pbl6_shoes_shop`.`order_item` (
        `delivery_date`,
        `discounted_price`,
        `price`,
        `quantity`,
        `size`,
        `user_id`,
        `order_id`,
        `product_id`
    )
VALUES (
        '2023-11-15 12:30:00',
        150,
        200,
        2,
        'M',
        1,
        100,
        500
    ),
    (
        '2023-11-16 14:45:00',
        120,
        180,
        3,
        'L',
        2,
        101,
        501
    ),
    (
        '2023-11-17 10:15:00',
        200,
        250,
        1,
        'S',
        3,
        102,
        502
    );
INSERT INTO `pbl6_shoes_shop`.`payment_information` (
        `user_id`,
        `card_number`,
        `cardholder_name`,
        `cvv`,
        `expiration_date`
    )
VALUES (
        1,
        '1234567890123456',
        'John Doe',
        '123',
        '2023-01-01'
    ),
    (
        2,
        '9876543210987654',
        'Jane Smith',
        '456',
        '2023-02-01'
    ),
    (
        3,
        '1111222233334444',
        'Bob Johnson',
        '789',
        '2023-03-01'
    );
INSERT INTO `pbl6_shoes_shop`.`product_sizes` (`product_id`, `name`, `quantity`)
VALUES (1, 'Size 36', 50),
    (1, 'Size 37', 100),
    (1, 'Size 38', 75),
    (2, 'Size 39', 60),
    (2, 'Size 40', 90);
INSERT INTO `pbl6_shoes_shop`.`rating` (`create_at`, `rating`, `product_id`, `user_id`)
VALUES ('2023-11-15 12:30:00', 4.5, 1, 101),
    ('2023-11-15 13:45:00', 3.8, 2, 102),
    ('2023-11-15 14:20:00', 5.0, 1, 103),
    ('2023-11-15 15:10:00', 4.2, 3, 104);
INSERT INTO `pbl6_shoes_shop`.`review` (
        `id`,
        `create_at`,
        `review`,
        `product_id`,
        `user_id`
    )
VALUES (
        1,
        '2023-11-15 12:00:00',
        'Nice product!',
        1001,
        2001
    ),
    (
        2,
        '2023-11-16 13:30:00',
        'Good service!',
        1002,
        2002
    ),
    (
        3,
        '2023-11-17 14:45:00',
        'Excellent quality!',
        1003,
        2003
    );