import React, { useCallback, useEffect, useState } from 'react';
import ProductGridCard from '../ProductGridCard';
import './style-prefix.scss';
import apiProductGrid from '../API/apiProductGrid';
import { toast } from 'react-toastify';
import { useSearchParams } from 'react-router-dom';
import apiBrand from '../API/apiBrand';
import apiFilterPrice from '../API/apiFilterPrice';
import apiGuestProduct from '../API/apiGuestProduct';

export default function ProductGridList() {
    const [isLoggedIn, setIsLoggedIn] = useState(true);
    const [products, setProducts] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    let [searchParams, setSearchParams] = useSearchParams();
    const selectedBrand = searchParams.get('brand');
    const [sortCriteria, setSortCriteria] = useState(null);
    const [sortOrder, setSortOrder] = useState('desc');

    useEffect(() => {
        const checkLoginStatus = () => {
            const access_token = localStorage.getItem('jwt');
            setIsLoggedIn(!!access_token);
        };

        checkLoginStatus();
    }, []);

    const fetchData = useCallback(async () => {
        try {
            setIsLoading(true);
            let response;

            if (selectedBrand) {
                response = await apiBrand.getProductByBrand(selectedBrand);
                setProducts(response?.data?.content);
            } else {
                // Chọn API dựa trên trạng thái đăng nhập
                response = await apiGuestProduct.getAllProduct();
                setProducts(response.data);
            }

            // Sorting logic
            if (sortCriteria) {
                response.data.content.sort((a, b) => {
                    if (sortOrder === 'asc') {
                        return a[sortCriteria] - b[sortCriteria];
                    } else {
                        return b[sortCriteria] - a[sortCriteria];
                    }
                });
            }
        } catch (error) {
            // toast.error(error?.message);
        } finally {
            setIsLoading(false);
        }
    }, [selectedBrand, sortCriteria, sortOrder]);
    const handleSort = useCallback(
        async (criteria) => {
            try {
                setIsLoading(true);

                // Update sorting criteria and order
                if (criteria === sortCriteria) {
                    // Chuyển đổi thứ tự sắp xếp nếu click vào cùng tiêu chí
                    setSortOrder((prevOrder) => (prevOrder === 'asc' ? 'desc' : 'asc'));
                } else {
                    // Set new sorting criteria
                    setSortCriteria(criteria);
                    setSortOrder('asc'); // Default to ascending order when changing criteria
                }

                // Gọi API tương ứng với sắp xếp theo giá
                if (criteria === 'price_low' || criteria === 'price_high') {
                    const priceSort = criteria === 'price_low' ? 'price_low' : 'price_high';
                    const response = await apiFilterPrice.getFilerPrice(priceSort);
                    setProducts(response.data.content);
                } else if (criteria === 'discountPersent') {
                    // Sắp xếp theo discountPersent
                    const sortedProducts = [...products].sort((a, b) => {
                        if (sortOrder === 'asc') {
                            return a.discountPersent - b.discountPersent;
                        } else {
                            return b.discountPersent - a.discountPersent;
                        }
                    });
                    setProducts(sortedProducts);
                } else if (criteria === 'default') {
                    // Nếu là sắp xếp mặc định, gọi API để lấy tất cả sản phẩm
                    const response = await apiProductGrid.getAllProduct();
                    setProducts(response.data.content);
                    console.log(response);
                }
            } catch (error) {
                toast.error(error?.message);
            } finally {
                setIsLoading(false);
            }
        },
        [sortCriteria, sortOrder, products],
    );

    useEffect(() => {
        fetchData();
    }, [fetchData, selectedBrand]);

    return (
        <section>
            <div className="product-main container-layout">
                <h2 className="title">Products</h2>
                <div style={{ margin: '10px 0' }}>
                    <label>Sort by:</label>
                    <select
                        style={{ margin: '0 10px', padding: '10px', borderRadius: '5px' }}
                        onChange={(e) => handleSort(e.target.value)}
                    >
                        <option value="default">Default</option>
                        <option value="price_low">Price Low</option>
                        <option value="price_high">Price High</option>
                        <option value="discountPersent">Discount Persent</option>
                    </select>
                </div>
                <div className="product-grid">
                    {isLoading ? (
                        <div className="brandCard-loading"></div>
                    ) : (
                        products.length > 0 &&
                        products.map((product) => <ProductGridCard key={product?.id} product={product} />)
                    )}
                </div>
            </div>
        </section>
    );
}
