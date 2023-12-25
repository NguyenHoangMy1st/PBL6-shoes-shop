use pbl6_shoes_shop;

select * from `user`;
select * from `address`;
select * from `cart`;
select * from `brand`;
select * from `product`;
select * from `cart_item`;
select * from `orders`;
select * from `order_item`;
select * from `payment_information`;
select * from `product_sizes`;
select * from `rating`;
select * from `review`;


-- --------------------- ADMIN DASHBOARD -----------------------

-- thống kê doanh thu, lợi nhuận theo từng giờ của một ngày nhất định được chọn

WITH HourReference AS (
    SELECT 0 AS hour_of_day
    UNION SELECT 1 UNION SELECT 2 UNION SELECT 3
    UNION SELECT 4 UNION SELECT 5 UNION SELECT 6
    UNION SELECT 7 UNION SELECT 8 UNION SELECT 9
    UNION SELECT 10 UNION SELECT 11 UNION SELECT 12
    UNION SELECT 13 UNION SELECT 14 UNION SELECT 15
    UNION SELECT 16 UNION SELECT 17 UNION SELECT 18
    UNION SELECT 19 UNION SELECT 20 UNION SELECT 21
    UNION SELECT 22 UNION SELECT 23

) SELECT
    hr.hour_of_day,
    COALESCE(SUM(revenue_subquery.total_discounted_price), 0) AS total_revenue,
    COALESCE(SUM(profit_subquery.total_profit), 0) AS total_profit
FROM
    HourReference hr
LEFT JOIN
    (
        SELECT
            DATE_FORMAT(o1.order_date, '%H') AS hour_of_day,
            o1.total_discounted_price
        FROM
            orders o1
        WHERE
            DATE_FORMAT(o1.order_date, '%d/%m/%Y') = '16/12/2023'
            AND o1.order_status = 'DELIVERED'
    ) revenue_subquery
ON
    hr.hour_of_day = revenue_subquery.hour_of_day
LEFT JOIN
    (
        SELECT
            DATE_FORMAT(o2.order_date, '%H') AS hour_of_day,
            SUM((p.discounted_price - p.warehouse_price) * oi.quantity) AS total_profit
        FROM
            orders o2
            JOIN order_item oi ON o2.id = oi.order_id
            JOIN product p ON oi.product_id = p.id
        WHERE
            DATE_FORMAT(o2.order_date, '%d/%m/%Y') = '16/12/2023'
             AND o2.order_status = 'DELIVERED'
        GROUP BY
            hour_of_day
    ) profit_subquery
ON
    hr.hour_of_day = profit_subquery.hour_of_day
GROUP BY
    hr.hour_of_day
ORDER BY
    hr.hour_of_day;

-- ------------
-- thống kê doanh thu lợi nhuận theo các ngày trong tháng được chọn

-- Tạo bảng tham chiếu cho tất cả các ngày trong tháng

WITH RECURSIVE DayReference AS (
    SELECT 1 AS day_of_month
    UNION
    SELECT day_of_month + 1
    FROM DayReference
    WHERE day_of_month < DAY(LAST_DAY('2023-12-01'))
) 
SELECT
    dr.day_of_month,
    COALESCE(SUM(revenue_subquery.total_revenue), 0) AS total_revenue,
    COALESCE(SUM(profit_subquery.total_profit), 0) AS total_profit
FROM
    DayReference dr
LEFT JOIN
    (
        SELECT
            DAY(o1.order_date) AS day_of_month,
            SUM(o1.total_discounted_price) AS total_revenue
        FROM 
            `orders` o1
        WHERE
            DATE_FORMAT(o1.order_date, '%m/%Y') = '12/2023'
            AND o1.order_status = 'DELIVERED'
        GROUP BY
            day_of_month
    ) revenue_subquery
ON dr.day_of_month = revenue_subquery.day_of_month
LEFT JOIN
    (
        SELECT
            DAY(o2.order_date) AS day_of_month,
            SUM((p.discounted_price - p.warehouse_price) * oi.quantity) AS total_profit
        FROM
            orders o2
            JOIN order_item oi ON o2.id = oi.order_id
            JOIN product p ON oi.product_id = p.id
        WHERE
            DATE_FORMAT(o2.order_date, '%m/%Y') = '12/2023'
            AND o2.order_status = 'DELIVERED'
        GROUP BY
            day_of_month
    ) profit_subquery
ON dr.day_of_month = profit_subquery.day_of_month
GROUP BY
    dr.day_of_month
ORDER BY
    dr.day_of_month;
-- -------------------------------- -- 
-- lấy doanh thu theo  một ngày được chọn 

SELECT DATE_FORMAT(o.`order_date`, '%d') AS selected_day,
		SUM(o.`total_discounted_price`) AS total_revenue
	FROM `orders` o
    WHERE
    DATE_FORMAT(o.`order_date`, '%Y-%m-%d') = '2023-12-17'
	AND o.`order_status` = 'DELIVERED'
	GROUP BY
    selected_day
	ORDER BY
    selected_day;
-- -------------------------------- -- 
-- lấy doanh thu theo  một tháng được chọn 
SELECT DATE_FORMAT(o.`order_date`, '%m') AS selected_month,
		SUM(o.`total_discounted_price`) AS total_revenue
	FROM `orders` o
    WHERE
    DATE_FORMAT(o.`order_date`, '%Y-%m') = '2023-12'
	AND o.`order_status` = 'DELIVERED'
	GROUP BY
    selected_month
	ORDER BY
    selected_month;


-- -------------------------------- -- 
-- lấy lợi nhuận theo  một ngày được chọn

SELECT
    DATE_FORMAT(o.`order_date`, '%d') AS selected_day,
    SUM((p.`discounted_price` - p.`warehouse_price`) * oi.`quantity`) AS total_profit
FROM
    `orders` o
    JOIN `order_item` oi ON o.`id` = oi.`order_id`
    JOIN `product` p ON oi.`product_id` = p.`id`
WHERE
    DATE_FORMAT(o.`order_date`, '%Y-%m-%d') = '2023-12-16'
	AND o.order_status = 'DELIVERED'
GROUP BY
    selected_day
ORDER BY
    selected_day;

-- -------------------------------- -- 
-- lấy lợi nhuận theo  một tháng được chọn
SELECT
    DATE_FORMAT(o.`order_date`, '%m') AS selected_month,
    SUM((p.`discounted_price` - p.`warehouse_price`) * oi.`quantity`) AS total_profit
FROM
    `orders` o
    JOIN `order_item` oi ON o.`id` = oi.`order_id`
    JOIN `product` p ON oi.`product_id` = p.`id`
WHERE
    DATE_FORMAT(o.`order_date`, '%Y-%m') = '2023-12'
	AND o.order_status = 'DELIVERED'
GROUP BY
    selected_month
ORDER BY
    selected_month;


select * from `orders`;
-- -------------------------------- -- 
-- lấy ra số lượng sản phẩm được đặt trong một ngày được chọn
SELECT DATE_FORMAT(o.`order_date`, '%d') AS selected_day,
		SUM(o.`total_item`) AS total_item
	FROM `orders` o
    WHERE
    DATE_FORMAT(o.`order_date`, '%Y-%m-%d') = '2023-12-17'
	AND o.`order_status` = 'PENDING'
	GROUP BY
    selected_day
	ORDER BY
    selected_day;
-- -------------------------------- -- 
-- lấy ra số lượng sản phẩm được đặt trong một tháng được chọn
SELECT DATE_FORMAT(o.`order_date`, '%M') AS selected_month,
		SUM(o.`total_item`) AS total_item
	FROM `orders` o
    WHERE
    DATE_FORMAT(o.`order_date`, '%Y-%m') = '2023-12'
	AND o.`order_status` = 'PENDING'
	GROUP BY
    selected_month
	ORDER BY
    selected_month;

-- -------------------------------- -- 
-- lấy ra số lượng người sử dụng trong một ngày được chọn

SELECT 
		COUNT(*) AS total_users
FROM `user` u
WHERE u.`role` = 'admin'
	AND DATE_FORMAT(u.`create_at`, '%d/%m/%Y %H:%i:%s') <= CONCAT('2023-12-16', ' 23:59:59')



-- -------------------------------- -- 
-- lấy ra số lượng người sử dụng trong một tháng được chọn
SELECT 
	COUNT(*) AS total_users
FROM `user` u
WHERE u.`role` = 'admin'
	AND DATE_FORMAT(u.`create_at`, '%d/%m') <= '2023/12'; 


-- lấy ra sản phẩm bán chạy nhất trong ngày hiện tại

select * from `product`;

SELECT
    oi.product_id,
    p.title,
    p.image_url,
    p.discounted_price,
    COUNT(*) AS appearance_count
FROM
    `orders` o
    JOIN `order_item` oi ON o.id = oi.order_id
    JOIN `product` p ON oi.product_id = p.id
WHERE
    DATE_FORMAT(o.order_date, '%Y-%m') = '2023-12'
    AND o.order_status = 'DELIVERED'
GROUP BY
    oi.product_id
ORDER BY
    appearance_count DESC
LIMIT 1;


