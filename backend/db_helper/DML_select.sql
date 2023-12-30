create database pbl6_shoes_shop;
drop database pbl6_shoes_shop;

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

-- ------------------------ top 3 -- ------------------------
-- top 3 new products
select p.`id` as p_id, 
	p.`color` as p_color, 
    p.`create_at` as p_create_at,
    p.`description` as p_description,
    p.`discount_persent` as p_discount_persent,
    p.`discounted_price` as p_discounted_price, 
	p.`image_url` as p_image_url,
    p.`price` as p_price,
    p.`quantity` as p_quantity,
	p.`title` as p_name, 
	b.`id` as b_id,
    b.`image_url` as b_image_url,
	b.`name` as b_name
from `product` p
join `brand` b
on p.`brand` = b.`id`
where p.`quantity` > 0
group by p.`id`
order by p.`create_at` desc
limit 3;


-- top 3 best selling products

with `order_details_information` as (
select 	
		oi.`quantity` as oi_quantity,
        oi.`product_id` as oi_product_id
	from `orders` o
	join `order_item` oi
	on o.`id` = oi.`order_id`
	where o.`order_status` = 'DELIVERED'
	group by oi.`id`
),
`top_3_best_selling_products` as (
	select odi.`oi_product_id` as product_id, sum(odi.`oi_quantity`) as total_quantity
    from `order_details_information` odi
    group by odi.`oi_product_id`
    order by total_quantity desc
	limit 0, 3
)
select p.`id` as p_id, 
	p.`color` as p_color, 
    p.`create_at` as p_create_at,
    p.`description` as p_description,
    p.`discount_persent` as p_discount_persent,
    p.`discounted_price` as p_discounted_price, 
	p.`image_url` as p_image_url,
    p.`price` as p_price,
    p.`quantity` as p_quantity,
	p.`title` as p_name,
	b.`id` as b_id,
    b.`image_url` as b_image_url,
	b.`name` as b_name,
	t3bsp.`total_quantity` as total_products_sold
from `product` p
join `brand` b
on p.`brand` = b.`id`
JOIN `top_3_best_selling_products` t3bsp ON p.`id` = t3bsp.`product_id`
where p.`id` in (
	select t3bsp.`product_id` from `top_3_best_selling_products` as t3bsp
)
group by p.`id`;

-- top 3 best rating products


with `detailed_product_star_rating` as (
	select
		r.`product_id` as product_id,
		avg(r.`rating`) as r_avg_rating 
	from `product` p
	join `review` r
	on p.`id` = r.`product_id`
	where p.`quantity` > 0
	group by r.`product_id`
),
`top_3_best_rating_products` as (
	select
		dpsr.product_id,
		dpsr.r_avg_rating 
	from `detailed_product_star_rating` dpsr
    order by dpsr.r_avg_rating desc
    limit 0,3
)
select p.`id` as p_id, 
	p.`color` as p_color, 
    p.`create_at` as p_create_at,
    p.`description` as p_description,
    p.`discount_persent` as p_discount_persent,
    p.`discounted_price` as p_discounted_price, 
	p.`image_url` as p_image_url,
    p.`price` as p_price,
    p.`quantity` as p_quantity,
	p.`title` as p_name,
	b.`id` as b_id,
    b.`image_url` as b_image_url,
	b.`name` as b_name,
	t3brp.r_avg_rating as avg_rating_product
from `product` p
join `brand` b
on p.`brand` = b.`id`
JOIN `top_3_best_rating_products` t3brp ON p.`id` = t3brp.`product_id`
where p.`id` in (
	select t3brp.`product_id` from `top_3_best_rating_products` as t3brp
)
group by p.`id`;
